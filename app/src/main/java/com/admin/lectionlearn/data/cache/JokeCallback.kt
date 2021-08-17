package com.admin.lectionlearn.data.cache

import com.admin.lectionlearn.presentation.Joke

interface JokeCallback {
    fun provide(joke: Joke)
}