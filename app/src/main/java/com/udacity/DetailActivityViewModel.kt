/*
 * Copyright (c) 2023.
 *
 *  Developed by : Bigad Aboubakr
 *  Developer website : http://bigad.me
 *  Developer github : https://github.com/Scout4all
 *  Developer Email : bigad@bigad.me
 */

package com.udacity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailActivityViewModel : ViewModel() {
    val fileName = MutableLiveData<String>("Repo")
    val fileStatus = MutableLiveData<String>("Pending")
}