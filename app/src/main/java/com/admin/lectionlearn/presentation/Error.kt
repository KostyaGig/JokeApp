package com.admin.lectionlearn.presentation

import com.admin.lectionlearn.R
import com.admin.lectionlearn.base.ResourceProvider

interface Error {
    fun getMessage(): String

    class NoConnection(private val resourceProvider: ResourceProvider): Error {
        override fun getMessage(): String  = resourceProvider.provide(R.string.no_connection_error_text)
    }

    class ServiceUnavailable(private val resourceProvider: ResourceProvider): Error {
        override fun getMessage(): String = resourceProvider.provide(R.string.service_unavailable_error_text)
    }

    class NoCachedJoke(private val resourceProvider: ResourceProvider): Error {
        override fun getMessage(): String = resourceProvider.provide(R.string.no_cached_joke_text)
    }
}