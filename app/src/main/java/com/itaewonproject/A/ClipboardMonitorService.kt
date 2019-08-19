package com.itaewonproject.A

import android.app.Service
import android.content.ClipboardManager
import android.util.Log
import android.text.TextUtils
import android.content.ClipData
import android.os.Environment.MEDIA_MOUNTED
import android.content.Intent
import android.os.IBinder
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors
import android.os.SystemClock
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import android.app.PendingIntent
import android.content.Context


class ClipboardMonitorService : Service() {

    private var mHistoryFile: File? = null
    private val mThreadPool = Executors.newSingleThreadExecutor()
    private var mClipboardManager: ClipboardManager? = null

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED.equals(state)) {
                true
            } else false
        }

    private val mOnPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
        Log.i("!!!clip", "onPrimaryClipChanged")
        Toast.makeText(applicationContext,"!!!!!!",Toast.LENGTH_SHORT)
        val clip = mClipboardManager!!.primaryClip
        /*mThreadPool.execute(
            WriteHistoryRunnable(
                clip!!.getItemAt(0).text
            )
        )*/
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("!!!!","activated Monitor")
        // TODO: Show an ongoing notification when this service is running.
        mHistoryFile = File(getExternalFilesDir(null), FILENAME)
        mClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        mClipboardManager!!.addPrimaryClipChangedListener(
            mOnPrimaryClipChangedListener
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mClipboardManager != null) {
            mClipboardManager!!.removePrimaryClipChangedListener(
                mOnPrimaryClipChangedListener
            )
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d(TAG, "BackgroundService.onTaskRemoved")
        //create an intent that you want to start again.
        val intent = Intent(applicationContext, ClipboardMonitorService::class.java)
        val pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent)
        super.onTaskRemoved(rootIntent)

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private inner class WriteHistoryRunnable(private val mTextToWrite: CharSequence) : Runnable {
        private val mNow: Date

        init {
            mNow = Date(System.currentTimeMillis())
        }

        override fun run() {
            if (TextUtils.isEmpty(mTextToWrite)) {
                // Don't write empty text to the file
                return
            }

            if (isExternalStorageWritable) {
                try {
                    Log.i(TAG, "Writing new clip to history:")
                    Log.i(TAG, mTextToWrite.toString())
                    val writer = BufferedWriter(FileWriter(mHistoryFile, true))
                    writer.write(String.format("[%s]: ", mNow.toString()))
                    writer.write(mTextToWrite.toString())
                    writer.newLine()
                    writer.close()
                } catch (e: IOException) {
                    Log.w(
                        TAG, String.format(
                            "Failed to open file %s for writing!",
                            mHistoryFile!!.getAbsoluteFile()
                        )
                    )
                }

            } else {
                Log.w(TAG, "External storage is not writable!")
            }
        }
    }

    companion object {
        private val TAG = "ClipboardManager"
        private val FILENAME = "clipboard-history.txt"
    }
}
