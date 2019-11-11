package com.itaewonproject.linkshare

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.itaewonproject.R
import java.util.Calendar
import java.util.regex.Pattern

class ClipboardListener : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatyView: View
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var cm: ClipboardManager
    private var isRunning = true

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        serviceIntent = intent
        showToast(application, "Start Service")
        Log.i("bgse", "bgservice")
        cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        cm.addPrimaryClipChangedListener {
            val p = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$")
            if (p.matcher(cm.primaryClip!!.getItemAt(0).text).matches()) {
                isRunning = true
                windowManager.addView(floatyView, params)

                val animator = ObjectAnimator.ofFloat(floatyView, View.ALPHA, 1f)
                animator.duration = 500
                animator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        floatyView.alpha = 1.0f
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onAnimationStart(p0: Animator?) {
                        floatyView.alpha = 0f
                    }
                })
                animator.start()

                Handler().postDelayed(Runnable {
                    removeRunner()
                }, 5500)
            }
        }

        return Service.START_NOT_STICKY
    }

    fun removeRunner() {
        if (isRunning) {
            isRunning = false
            val animator = ObjectAnimator.ofFloat(floatyView, View.ALPHA, 0f)
            animator.duration = 500
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
                }

                override fun onAnimationEnd(p0: Animator?) {
                    floatyView.alpha = 0f
                    windowManager.removeView(floatyView)
                    Log.i("windowManager", "removed View")
                }

                override fun onAnimationCancel(p0: Animator?) {
                    TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
                }

                override fun onAnimationStart(p0: Animator?) {
                    floatyView.alpha = 1.0f
                }
            })
            animator.start()
        }
    }

    override fun onDestroy() {

        serviceIntent = null
        setAlarmTimer()
        Thread.currentThread().interrupt()

        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        serviceIntent = null
        setAlarmTimer()
        Thread.currentThread().interrupt()

        super.onTaskRemoved(rootIntent)
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        addOverlayView()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    fun showToast(application: Application, msg: String) {
        val h = Handler(application.mainLooper)
        h.post { Toast.makeText(application, msg, Toast.LENGTH_LONG).show() }
    }

    protected fun setAlarmTimer() {
        val c = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        c.add(Calendar.SECOND, 1)
        val intent = Intent(this, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)

        val mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.timeInMillis, sender)
    }

    private fun addOverlayView() {

         params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
             WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP // Gravity.CENTER or Gravity.START
        params.x = 0
        params.y = 0
        val interceptorLayout = object : FrameLayout(this) {

            override fun dispatchGenericMotionEvent(event: MotionEvent?): Boolean {
                return super.dispatchGenericMotionEvent(event)
            }

            override fun dispatchKeyEvent(event: KeyEvent): Boolean {

                // Only fire on the ACTION_DOWN event, or you'll get two events (one for _DOWN, one for _UP)
                if (event.getAction() === KeyEvent.ACTION_DOWN) {

                    // Check if the HOME button is pressed
                    if (event.getKeyCode() === KeyEvent.KEYCODE_BACK) {

                        removeRunner()
                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        return true
                    }
                }

                // Otherwise don't intercept the event
                return super.dispatchKeyEvent(event)
            }
        }

        floatyView = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(com.itaewonproject.R.layout.view_url_copy, interceptorLayout)
        var buttonOk = floatyView.findViewById(R.id.button_ok) as TextView
        buttonOk.setOnClickListener({
            var intent = Intent(this, LinkShareActivity::class.java)
            intent.putExtra(Intent.EXTRA_TEXT, cm.primaryClip!!.getItemAt(0).text)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            removeRunner()

            startActivity(intent)
        })
    }

    companion object {
        var serviceIntent: Intent? = null
    }
}
