package com.udacity.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.udacity.DetailActivity
import com.udacity.notification.cancelNotifications

class OpenDetailsViewReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.w("Received","Receiver")
        val openDetailActivityIntent = Intent(context, DetailActivity::class.java)
        openDetailActivityIntent.putExtra("status",intent.getStringExtra("status"))
        openDetailActivityIntent.putExtra("fileName",intent.getStringExtra("fileName"))
        context.startActivity(intent)
//
//        val notificationManager = ContextCompat.getSystemService(
//            context,
//            NotificationManager::class.java) as NotificationManager
//        notificationManager.cancelNotifications()


    }
}