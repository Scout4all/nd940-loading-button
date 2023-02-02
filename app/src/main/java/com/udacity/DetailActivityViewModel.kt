package com.udacity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailActivityViewModel : ViewModel() {
      val fileName = MutableLiveData<String>("Repo")
       val fileStatus = MutableLiveData<String>("Pending")
}