package com.admin.lectionlearn.presentation

import androidx.annotation.DrawableRes
import com.admin.lectionlearn.data.cache.JokeCallback
import com.admin.lectionlearn.data.net.CloudDataSource
import com.admin.lectionlearn.domain.Model

class ViewModel(
    private val model: Model<Joke,FailedJoke>,
) {

    private var dataCallback: DataCallback? = null
    private val jokeCallback = object : JokeCallback {
        override fun provide(joke: Joke) {
            dataCallback?.let {
                joke.map(it)
            }
        }
    }

    fun getJoke() {
        model.getJoke()
    }

    fun init(callBack: DataCallback) {
        this.dataCallback = callBack

        model.init(object: Model.ResultCallback<Joke, FailedJoke> {
            override fun provide(data: Joke) {
                dataCallback?.let {
                    data.map(it)
                }
            }

            override fun provideError(failedJoke: FailedJoke){
                dataCallback?.let {
                    failedJoke.map(it)
                }
            }
        })
    }

    fun clear() {
        model.clear()
        dataCallback = null
    }

    fun changeJokeStatus() {
        model.changeJokeStatus(jokeCallback)
    }

    fun chooseFavorite(favorites: Boolean) {
        model.chooseFavorite(favorites)
    }

    interface DataCallback {
        fun provideText(text: String)

        fun provideIconRes(@DrawableRes id: Int)
    }
}