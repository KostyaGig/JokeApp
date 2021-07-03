package com.admin.lectionlearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.admin.lectionlearn.presentation.ViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = (application as JokeApp).viewModel

        viewModel.init(object : ViewModel.DataCallback {
            override fun provideText(text: String) {
                //Current thread != main
                log(text)
            }
        })
    }

    fun AppCompatActivity.log(text: String) {
        Log.d("MainActivity",text)
    }

    override fun onStop() {
        super.onStop()
        viewModel.clear()
    }
}