package com.admin.lectionlearn.data.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmJoke: RealmObject() {
    @PrimaryKey
    var id: Int = -1
    var text: String = ""
    var punchline: String = ""
    var type: String = ""
}