package com.udacity.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.udacity.MainActivity

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notifyIntent = Intent(context, MainActivity::class.java)
        context.startActivity(notifyIntent)

    }


}