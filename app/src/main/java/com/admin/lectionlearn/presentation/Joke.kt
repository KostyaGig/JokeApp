package com.admin.lectionlearn.presentation

import androidx.annotation.DrawableRes


abstract class Joke(
    private val text: String,
    private val punchline: String
) {

    fun getJokeUi() = "$text\n$punchline"

    @DrawableRes
    abstract fun getIconResId(): Int

    fun map(callback: ViewModel.DataCallback) = callback.run {
        provideIconRes(getIconResId())
        provideText(getJokeUi())
    }
}


//Don't liked joke
class BaseJoke(text: String,punchline: String): Joke(text,punchline) {
    override fun getIconResId(): Int = TODO("base icon joke")
}

class FavoriteJoke(text: String,punchline: String): Joke(text,punchline) {
    override fun getIconResId(): Int = TODO("favorite icon joke")
}

class FailedJoke(text: String): Joke(text,"") {
    override fun getIconResId(): Int = 0
}