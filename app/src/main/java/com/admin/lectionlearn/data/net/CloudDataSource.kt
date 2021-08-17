package com.admin.lectionlearn.data.net

import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

interface CloudDataSource {

    fun getJoke(callback: JokeCloudCallback)

    interface JokeCloudCallback {
        fun provide(joke: JokeServerModel)
        fun fail(error: ErrorType)

        enum class ErrorType {
            NO_CONNECTION,
            OTHER
        }
    }

    class BaseCloudDataSource(private val service: JokeService): CloudDataSource {
        override fun getJoke(callback: JokeCloudCallback) {
            service.getJoke().enqueue(object : retrofit2.Callback<JokeServerModel>{
                override fun onResponse(call: Call<JokeServerModel>, response: Response<JokeServerModel>) {
                    if (response.isSuccessful) {
                        callback.provide(response.body()!!)
                    } else {
                        callback.fail(JokeCloudCallback.ErrorType.OTHER)
                    }
                }
                override fun onFailure(call: Call<JokeServerModel>, t: Throwable) {
                    val error = when(t) {
                        is UnknownHostException -> JokeCloudCallback.ErrorType.NO_CONNECTION
                        else -> JokeCloudCallback.ErrorType.OTHER
                    }
                    callback.fail(error)
                }
            })
        }
    }
}