package com.udacity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


//class MainViewModelFactory(private val context: Application) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
//            return MainActivityViewModel(context) as T
//        }
//        throw java.lang.IllegalArgumentException("Un known View class")
//    }
//
//}