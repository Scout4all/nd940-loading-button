package com.udacity.helper

import android.app.DownloadManager
import android.database.Cursor


class DownloadManagerHelper {
    fun getDownloadStatus( downloadManager : DownloadManager,   downloadID : Long): String {
        var status = "pending"
        val c : Cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))

        if (c.moveToFirst()) {
            val downloadStatus = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (downloadStatus) {
                DownloadManager.STATUS_PAUSED -> status= "Paused"
                DownloadManager.STATUS_PENDING -> status= "Pending"
                DownloadManager.STATUS_RUNNING -> status= "Running"
                DownloadManager.STATUS_SUCCESSFUL -> status= "Success"
                DownloadManager.STATUS_FAILED -> status= "Fail"
            }
        }
        return status
    }
    companion object{
        val help = DownloadManagerHelper()
    }
}