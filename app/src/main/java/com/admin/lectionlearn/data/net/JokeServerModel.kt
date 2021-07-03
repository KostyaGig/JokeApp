package com.admin.lectionlearn.data.net

import com.admin.lectionlearn.data.cache.CacheDataSource
import com.admin.lectionlearn.data.cache.RealmJoke
import com.admin.lectionlearn.presentation.BaseJoke
import com.admin.lectionlearn.presentation.FavoriteJoke
import com.google.gson.annotations.SerializedName

data class JokeServerModel(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("type")
    private val type: String,
    @SerializedName("text")
    private val text: String,
    @SerializedName("punchline")
    private val punchline: String,
) {
    fun toBaseJoke() = BaseJoke(text,punchline)
    fun toFavoriteJoke() = FavoriteJoke(text,punchline)
    fun toRealmJoke() = RealmJoke().also { realmJoke ->
        realmJoke.id = id
        realmJoke.type = type
        realmJoke.text = text
        realmJoke.punchline = punchline
    }

    fun change(cacheDataSource: CacheDataSource) = cacheDataSource.addOrRemove(id,this)
}