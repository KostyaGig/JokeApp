package com.admin.lectionlearn.domain

import com.admin.lectionlearn.base.ResourceProvider
import com.admin.lectionlearn.data.cache.CacheDataSource
import com.admin.lectionlearn.data.cache.JokeCallback
import com.admin.lectionlearn.data.net.CloudDataSource
import com.admin.lectionlearn.data.net.CloudDataSource.JokeCloudCallback
import com.admin.lectionlearn.data.net.JokeServerModel
import com.admin.lectionlearn.presentation.Error
import com.admin.lectionlearn.presentation.FailedJoke
import com.admin.lectionlearn.presentation.Joke

interface Model<S,E> {
    fun init(callback: ResultCallback<S,E>)
    fun getJoke()
    fun clear()
    fun changeJokeStatus(jokeCallback: JokeCallback)
    fun chooseFavorite(favorites: Boolean)

    interface ResultCallback<S,E> {

        fun provide(data: S)

        fun provideError(error: E)
    }

    class BaseModel(
        private val resourceProvider: ResourceProvider,
        private val cloudDataSource: CloudDataSource,
        private val cacheDataSource: CacheDataSource
        ): Model<Joke,FailedJoke> {

        private var callback: ResultCallback<Joke,FailedJoke>? = null
        private var cachedJokeServerModel: JokeServerModel? = null
        private var getJokeFromCached:Boolean = false

        private val noConnectionError by lazy { Error.NoConnection(resourceProvider) }
        private val serviceUnavailableError by lazy { Error.ServiceUnavailable(resourceProvider) }
        private val noCachedJoke by lazy { Error.NoCachedJoke(resourceProvider) }

        override fun init(callback: ResultCallback<Joke,FailedJoke>) {
            this.callback = callback
        }

        override fun getJoke() {
            Thread {
                if (getJokeFromCached) {
                    cacheDataSource.getJoke(object : CacheDataSource.JokeCachedCallback{
                        override fun provide(jokeServerModel: JokeServerModel) {
                            callback?.provide(jokeServerModel.toFavoriteJoke())
                        }

                        override fun fail() {
                            callback?.provide(FailedJoke(noCachedJoke.getMessage()))
                        }

                    })
                } else {
                    cloudDataSource.getJoke(object : JokeCloudCallback{
                        override fun provide(joke: JokeServerModel) {
                            cachedJokeServerModel = joke
                            callback?.provide(joke.toBaseJoke())
                        }

                        override fun fail(error: JokeCloudCallback.ErrorType) {
                            cachedJokeServerModel = null
                            val error =
                                when (error) {
                                    JokeCloudCallback.ErrorType.NO_CONNECTION -> noConnectionError
                                    else -> serviceUnavailableError
                                }
                            val failedJoke = FailedJoke(error.getMessage())
                            callback?.provideError(failedJoke)
                        }
                    })
                }
            }.start()
        }

        override fun changeJokeStatus(jokeCallback: JokeCallback) {
            cachedJokeServerModel?.change(cacheDataSource)?.let {
                jokeCallback.provide(it)
            }
        }

        override fun clear() {
            callback = null
        }

        override fun chooseFavorite(favorites: Boolean) {
            getJokeFromCached = favorites
        }

    }
}