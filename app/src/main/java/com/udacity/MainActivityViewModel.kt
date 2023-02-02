package com.udacity

import android.app.Application
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.helper.DownloadManagerHelper
import com.udacity.notification.sendNotification
import kotlin.collections.ArrayList


class MainActivityViewModel(private val app: Application) : AndroidViewModel(app) {

    val filesList : ArrayList<DownloadFileData> = arrayListOf(
        DownloadFileData(app.resources.getString(R.string.glide_radio_text) ,"https://github.com/bumptech/glide/archive/refs/tags/v4.14.2.zip",0),
        DownloadFileData(app.resources.getString(R.string.load_app_radio_text),"https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",1),
        DownloadFileData( app.resources.getString(R.string.retrofit_radio_text),"https://github.com/square/retrofit/archive/refs/tags/2.9.0.zip",2),
    )

    private val _selectedFile = MutableLiveData<DownloadFileData>()
    val selectedFile: LiveData<DownloadFileData>
        get() = _selectedFile

     var customUrl = MutableLiveData<String> ("https://github.com/Scout4all/nd940-asteroid-radar/archive/refs/tags/v1.0-stable.zip")

    private val _fileDownloadedState = MutableLiveData(false)
    val fileDownloadedState: LiveData<Boolean>
        get() = _fileDownloadedState

    var downloadStatus: String = "Pending"


    private var downloadID: Long = 0
    lateinit var downloadManager: DownloadManager

    private lateinit var notificationManager: NotificationManager


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            downloadStatus = DownloadManagerHelper.help.getDownloadStatus(downloadManager,downloadID)

//            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            _fileDownloadedState.value = true
            notificationManager = ContextCompat.getSystemService(
                app,
                NotificationManager::class.java
            ) as NotificationManager
                notificationManager.sendNotification(
                    app,
                      downloadStatus,

                    selectedFile.value!!.fileName,
                    selectedFile.value!!.fileIndex
                )

            _fileDownloadedState.value = false
        }
    }

    init {
        _selectedFile.value = DownloadFileData(fileIndex = -1)
        app.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    fun download() {
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .mkdirs();
        val request =
            DownloadManager.Request(Uri.parse(selectedFile.value?.fileUrl))
                .setTitle(selectedFile.value?.fileName)
                .setDescription(app.getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    selectedFile.value?.fileName
                )

        downloadManager =
            app.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager

        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.



    }


    fun getViewB(radioGroup : RadioGroup , selectedId : Int) {
        when(selectedId){
            R.id.glide_radio_btn -> _selectedFile.value = filesList.get(0)
            R.id.udacity_repo_radio_btn -> _selectedFile.value =filesList.get(1)
            R.id.retrofit_radio_btn -> _selectedFile.value = filesList.get(2)
            R.id.other_link_radio_btn -> {
                if(!customUrl.value.isNullOrEmpty()) {
                    filesList.add(3, DownloadFileData("Custom file", customUrl.value!!, 3))
                    _selectedFile.value = filesList.get(3)
                }else{
                    _selectedFile.value = DownloadFileData(fileIndex = -1)
                }
                Log.w("custom file", _selectedFile.value?.fileUrl!!)
            }
            else ->  _selectedFile.value = DownloadFileData(fileIndex = -1)
        }

    }



    override fun onCleared() {
        super.onCleared()
        _selectedFile.value = DownloadFileData(fileIndex = -1)

      downloadStatus  = "Pending"
    }

}
data class DownloadFileData(val fileName:String="", val fileUrl :String="",val fileIndex :Int = -1)