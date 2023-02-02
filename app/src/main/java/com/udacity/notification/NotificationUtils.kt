/*
 * Copyright (c) 2023.
 *
 *  Developed by : Bigad Aboubakr
 *  Developer website : http://bigad.me
 *  Developer github : https://github.com/Scout4all
 *  Developer Email : bigad@bigad.me
 */

package com.udacity.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat

import com.udacity.R
import com.udacity.receivers.OpenDetailsViewReceiver

private val REQUEST_CODE = 0
private val NOTIFICATION_ID = 0
private val FLAGS = 0


fun NotificationManager.sendNotification(
    applicationContext: Application,
    status: String,
    fileName: String,
    fileIndex: Int
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel
        val name = applicationContext.getString(R.string.notification_channel_name)
        val descriptionText = applicationContext.getString(R.string.channel_description)
        val channelId = applicationContext.getString(R.string.notification_channel_id)
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(channelId, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    val contentIntent = Intent(applicationContext, OpenDetailsViewReceiver::class.java)
    contentIntent.putExtra("status", status)
    contentIntent.putExtra("fileName", fileName)

    val pendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        contentIntent,
        FLAGS
    )


    val packagePicture = when (fileIndex) {
        0 -> {
            BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.glide_logo
            )

        }
        1 -> {
            BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.udacity
            )

        }
        2 -> {
            BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.retrofit2_getting_started
            )

        }
        else -> {
            BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.baseline_cloud_download_24
            )
        }

    }
    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(packagePicture)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.baseline_cloud_download_24)
        .setContentTitle(
            applicationContext.getString(
                R.string.notification_title
            )
        )
        .setContentText(status)
        .setStyle(bigPictureStyle)
        .setLargeIcon(packagePicture)
        .setAutoCancel(true)
        .addAction(
            R.drawable.abc_vector_test,
            applicationContext.getText(R.string.notification_action),
            pendingIntent
        )
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}