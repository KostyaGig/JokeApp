package com.admin.lectionlearn.data.net

import org.junit.Assert.*
import org.junit.Test

class CloudDataSourceTest {

    class TestCloudDataSource: CloudDataSource {
        private var count = 0
        override fun getJoke(callback: CloudDataSource.JokeCloudCallback) {
            val joke = JokeServerModel(count,"testType $count","testText $count","testPunchline $count")
            callback.provide(joke)
            count++
        }
    }
}