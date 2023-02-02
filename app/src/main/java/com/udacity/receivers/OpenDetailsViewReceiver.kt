/*
 * Copyright (c) 2023.
 *
 *  Developed by : Bigad Aboubakr
 *  Developer website : http://bigad.me
 *  Developer github : https://github.com/Scout4all
 *  Developer Email : bigad@bigad.me
 */

package com.udacity.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.udacity.DetailActivity
import com.udacity.notification.cancelNotifications

class OpenDetailsViewReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //start new activity
        val openDetailActivityIntent = Intent(context, DetailActivity::class.java)
        openDetailActivityIntent.putExtra("status", intent.getStringExtra("status"))
        openDetailActivityIntent.putExtra("fileName", intent.getStringExtra("fileName"))
        openDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openDetailActivityIntent)
//clear notification
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()
        //cloase notification panel
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)


    }
}