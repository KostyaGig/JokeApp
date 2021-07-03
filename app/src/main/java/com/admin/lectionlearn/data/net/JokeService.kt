package com.admin.lectionlearn.data.net

import retrofit2.Call
import retrofit2.http.GET

// FIXME: 03.07.2021 paste verify link
private const val JOKE_URL = "LINK"

interface JokeService {

    @GET(JOKE_URL)
    fun getJoke(): Call<JokeServerModel>

}