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
import com.udacity.MainActivity
import com.udacity.R
import com.udacity.reciever.SnoozeReceiver

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Application, type : Int) {

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
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT

    )

    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozeReceiverPendingIntent : PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        PendingIntent.FLAG_ONE_SHOT
    )

    var packagePicture = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.baseline_cloud_download_24
    )

    when(type){
        0->{
            packagePicture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.glide_logo
            )

        }
        1 ->{
            packagePicture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.udacity
            )

        }
        2 ->{
            packagePicture = BitmapFactory.decodeResource(
                applicationContext.resources,
                R.drawable.retrofit2_getting_started
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
        .setContentTitle(applicationContext.getString(
            R.string.notification_title
        ))
        .setContentText(messageBody)
            .setStyle(bigPictureStyle)
        .setLargeIcon(packagePicture)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(R.drawable.abc_vector_test,
            applicationContext.getText(R.string.notification_action),
            snoozeReceiverPendingIntent)
    notify(NOTIFICATION_ID,builder.build())
}