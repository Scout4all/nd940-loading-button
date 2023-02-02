/*
 * Copyright (c) 2023.
 *
 *  Developed by : Bigad Aboubakr
 *  Developer website : http://bigad.me
 *  Developer github : https://github.com/Scout4all
 *  Developer Email : bigad@bigad.me
 */

package com.udacity.helper

import android.app.Application
import android.app.DownloadManager
import android.database.Cursor
import android.util.Log
import android.widget.Toast

class Helper {
    fun detectCursor(downloadManager: DownloadManager, downloadID: Long, app: Application) {
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
            Log.d(
                javaClass.name, "COLUMN_LAST_MODIFIED_TIMESTAMP: " +
                        c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP))
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
            Toast.makeText(app.applicationContext, statusMessage(c), Toast.LENGTH_SHORT).show()
        }
    }

    private fun statusMessage(c: Cursor): String? {
        var msg = "???"
        msg =
            when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                DownloadManager.STATUS_FAILED -> "Download failed!"
                DownloadManager.STATUS_PAUSED -> "Download paused!"
                DownloadManager.STATUS_PENDING -> "Download pending!"
                DownloadManager.STATUS_RUNNING -> "Download in progress!"
                DownloadManager.STATUS_SUCCESSFUL -> "Download complete!"
                else -> "Download is nowhere in sight"
            }
        return msg
    }

    companion object {
        private lateinit var instance: Helper
        fun getInstance(): Helper {
            if (instance == null) {
                instance = Helper()
            }
            return instance
        }


    }
}