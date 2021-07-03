package com.admin.lectionlearn.base

import android.content.Context
import androidx.annotation.StringRes

interface ResourceProvider {
    fun provide(@StringRes resId: Int): String

    class BaseResourceProvider(private val context: Context): ResourceProvider {
        override fun provide(@StringRes resId: Int): String  = context.getString(resId)
    }
}