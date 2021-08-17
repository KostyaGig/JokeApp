package com.admin.lectionlearn

import android.app.Application
import com.admin.lectionlearn.base.ResourceProvider
import com.admin.lectionlearn.data.cache.CacheDataSource
import com.admin.lectionlearn.data.cache.RealmProvider
import com.admin.lectionlearn.data.net.CloudDataSource
import com.admin.lectionlearn.data.net.JokeService
import com.admin.lectionlearn.domain.Model
import com.admin.lectionlearn.presentation.ViewModel
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApp: Application() {

    lateinit var viewModel: ViewModel

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        // FIXME: 03.07.2021 Change base url
        val service = Retrofit.Builder()
            .baseUrl("Link")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JokeService::class.java)

        val defaultRealmProvider = RealmProvider.DefaultRealmProvider()

        viewModel = ViewModel(
            Model.BaseModel(
                ResourceProvider.BaseResourceProvider(this),
                CloudDataSource.BaseCloudDataSource(service),
                CacheDataSource.BaseCacheDataSource(defaultRealmProvider.provide()))
        )
    }
}