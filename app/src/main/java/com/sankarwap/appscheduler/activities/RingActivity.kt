package com.sankarwap.appscheduler.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.sankarwap.appscheduler.R
import com.sankarwap.appscheduler.data.Alarm
import com.sankarwap.appscheduler.service.AlarmService
import java.util.*

class RingActivity : AppCompatActivity() {
    private var textView: TextView? = null
    private var activityRingClock: ImageView? = null
    private var activityRingDismiss: Button? = null
    private var activityRingSnooze: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ring)
        textView = findViewById<View>(R.id.textView) as TextView
        activityRingClock = findViewById<View>(R.id.activity_ring_clock) as ImageView
        activityRingDismiss = findViewById<View>(R.id.activity_ring_dismiss) as Button
        activityRingSnooze = findViewById<View>(R.id.activity_ring_snooze) as Button
        ButterKnife.bind(this)
        activityRingDismiss!!.setOnClickListener { v: View? ->
            val intentService = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intentService)
            finish()
        }
        activityRingSnooze!!.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, 10)
            val alarm = Alarm(
                    Random().nextInt(Int.MAX_VALUE),
                    calendar[Calendar.HOUR_OF_DAY],
                    calendar[Calendar.MINUTE],
                    "Snooze",
                    System.currentTimeMillis(),
                    true,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
            )
            alarm.schedule(applicationContext)
            val intentService = Intent(applicationContext, AlarmService::class.java)
            applicationContext.stopService(intentService)
            finish()
        }
        animateClock()
    }

    private fun animateClock() {
     /*   val rotateAnimation = ObjectAnimator.ofFloat(activityRingClock, "rotation", 0f, 20f, 0f, -20f, 0f)
        rotateAnimation.repeatCount = ValueAnimator.INFINITE
        rotateAnimation.duration = 800
        rotateAnimation.start()*/
    }
}