package com.udacity

import android.app.Application
import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.dummy.Helper
import com.udacity.notification.sendNotification
import java.text.SimpleDateFormat
import java.util.*


class MainActivityViewModel(private val app: Application) : AndroidViewModel(app) {
    val url = listOf(
        "https://github.com/bumptech/glide/archive/refs/tags/v4.14.2.zip",
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
        "https://github.com/square/retrofit/archive/refs/tags/2.9.0.zip"
    )

    private val _selectedLibrary = MutableLiveData(-1)
    val selectedLibrary: LiveData<Int>
        get() = _selectedLibrary

    private val _downloaded= MutableLiveData(false)
    val downloaded: LiveData<Boolean>
        get() = _downloaded

    private var downloadID: Long = 0
    lateinit var downloadManager: DownloadManager

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
_downloaded.value=true
            _downloaded.value=false

            notificationManager = ContextCompat.getSystemService(
                app,
                NotificationManager::class.java
            ) as NotificationManager
            if (id != -1L) {
                notificationManager.sendNotification("downloaded", app, selectedLibrary.value!!.toInt())

            } else {
                notificationManager.sendNotification("error", app, selectedLibrary.value!!.toInt())

            }
        }
    }

    init {

        app.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


    }


    fun download() {
        val downloadUrl = selectedLibrary.value?.let { url.get(it) }
        Log.w("viewModel", downloadUrl.toString())

        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .mkdirs();
        val request =
            DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle(app.getString(R.string.app_name))
                .setDescription(app.getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    app.getString(R.string.app_name)
                )



        downloadManager =
            app.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager

        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        detectCursor()

    }



    fun setUrl(value: Int) {
        _selectedLibrary.value = value
    }
    fun detectCursor() :Cursor{
        val c: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))

        if (c == null) {
            Toast.makeText(app, "Download not found!", Toast.LENGTH_LONG).show();
        } else {
            c.moveToFirst()
            Log.d(
                javaClass.name, "COLUMN_ID: " +
                        c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID))
            )
            Log.d(
                javaClass.name, "COLUMN_BYTES_DOWNLOADED_SO_FAR: " +
                        c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            )
            val timestamp =  c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP))

            Log.d(
                javaClass.name, "COLUMN_LAST_MODIFIED_TIMESTAMP: " +
                        convertLongToTime(timestamp)
            )
            Log.d(
                javaClass.name, "COLUMN_LOCAL_URI: " +
                        c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            )
            Log.d(
                javaClass.name, "COLUMN_STATUS: " +
                        c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            )
            Log.d(
                javaClass.name, "COLUMN_REASON: " +
                        c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON))
            )
//            Toast.makeText(app.applicationContext, statusMessage(c), Toast.LENGTH_SHORT).show()
        }
        return c
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

}