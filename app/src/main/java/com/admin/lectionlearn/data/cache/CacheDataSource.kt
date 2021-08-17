package com.admin.lectionlearn.data.cache

import com.admin.lectionlearn.data.net.JokeServerModel
import com.admin.lectionlearn.presentation.Joke
import io.realm.Realm

interface CacheDataSource {
    fun addOrRemove(id: Int,jokeServerModel: JokeServerModel): Joke

    fun getJoke(jokeCachedCallBack: JokeCachedCallback)

    interface JokeCachedCallback {
        fun provide (jokeServerModel: JokeServerModel)
        fun fail()
    }
    class BaseCacheDataSource(
        private val realm: Realm
    ): CacheDataSource {

        override fun addOrRemove(id: Int, jokeServerModel: JokeServerModel): Joke {
            realm.use {realm->
                val jokeRealm = realm.where(RealmJoke::class.java).equalTo("id",id).findFirst()
                return if (jokeRealm == null) {
                    val newJoke = jokeServerModel.toRealmJoke()
                    realm.executeTransactionAsync {transaction->
                        transaction.insert(newJoke)
                    }
                    jokeServerModel.toFavoriteJoke()
                } else {
                    realm.executeTransaction {
                        jokeRealm.deleteFromRealm()
                    }
                    jokeServerModel.toBaseJoke()
                }
            }

        }

        override fun getJoke(jokeCachedCallBack: JokeCachedCallback) {
            realm.use {realm->
                val jokes = realm.where(RealmJoke::class.java).findAll()
                if (jokes.isEmpty()) {
                    jokeCachedCallBack.fail()
                } else {
                    jokes.random().let { jokeRealm->
                        jokeCachedCallBack.provide(
                            JokeServerModel(
                                jokeRealm.id,
                                jokeRealm.type,
                                jokeRealm.text,
                                jokeRealm.punchline
                            )
                        )
                    }
                }
            }
        }
    }
}