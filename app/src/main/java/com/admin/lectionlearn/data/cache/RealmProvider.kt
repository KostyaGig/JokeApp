package com.admin.lectionlearn.data.cache

import io.realm.Realm

interface RealmProvider {
    fun provide(): Realm

    class DefaultRealmProvider(): RealmProvider {
        override fun provide(): Realm = Realm.getDefaultInstance()
    }
}