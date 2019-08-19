package com.itaewonproject.A

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmRecever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val `in` = Intent(context, RestartService::class.java)
            context.startForegroundService(`in`)
        } else {
            val `in` = Intent(context, RealService::class.java)
            context.startService(`in`)
        }
    }

}
