package com.sankarwap.appscheduler.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.sankarwap.appscheduler.data.AlarmDatabase
import com.sankarwap.appscheduler.service.AlarmService
import com.sankarwap.appscheduler.service.RescheduleAlarmsService
import java.util.*

class AlarmBroadcastReceiver : BroadcastReceiver() {

    private var db: AlarmDatabase? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val toastText = String.format("App Scheduler Reboot")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            startRescheduleAlarmsService(context)
        } else {
            val toastText = String.format("App Scheduler Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

            if (!intent.getBooleanExtra(RECURRING, false)) {
//                startAlarmService(context, intent)
                open_apps(context)
                cancelAlarm(context,intent)
            }
            run {
                if (alarmIsToday(intent)) {
//                    startAlarmService(context, intent)
                    open_apps(context)
                }
            }
        }
    }

    private fun cancelAlarm(context: Context, intent: Intent) {
        if (intent.getIntExtra(ALARAM_ID, 0) != 0) {
            this.db = Room.databaseBuilder(context, AlarmDatabase::class.java, "alaram_database").allowMainThreadQueries().build()
            val alaram_db = db!!.alarmDao()!!.getByalaramId(intent.getIntExtra(ALARAM_ID, 0))
                if (alaram_db != null) {
                    val alaramsss = alaram_db
                    alaramsss.cancelAlarm(context)
                    alaramsss.isStarted=false
                    db!!.alarmDao()!!.update(alaramsss)
                    Log.d("fff","addeddd")
            }
        }

    }

    private fun open_apps(context: Context) {
        this.db = Room.databaseBuilder(context, AlarmDatabase::class.java, "alaram_database").allowMainThreadQueries().build()
        val pack = db!!.appDao()!!.getAllPackageName()
        for (packs in pack) {

            val SPLASH_TIME_OUT = 5000
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    val launchIntent = context.packageManager.getLaunchIntentForPackage(packs)
                    launchIntent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(launchIntent)
                } catch (e: Exception) {
                    Log.d(ContentValues.TAG, e.message + "")
                }
            }, SPLASH_TIME_OUT.toLong())
        }
    }

    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val today = calendar[Calendar.DAY_OF_WEEK]
        when (today) {
//            Log.d("dddd",intent.getStringExtra())
            Calendar.MONDAY -> {
                return intent.getBooleanExtra(MONDAY, false)
            }
            Calendar.TUESDAY -> {
                return intent.getBooleanExtra(TUESDAY, false)
            }
            Calendar.WEDNESDAY -> {
                return intent.getBooleanExtra(WEDNESDAY, false)
            }
            Calendar.THURSDAY -> {
                return intent.getBooleanExtra(THURSDAY, false)
            }
            Calendar.FRIDAY -> {
                return intent.getBooleanExtra(FRIDAY, false)
            }
            Calendar.SATURDAY -> {
                return intent.getBooleanExtra(SATURDAY, false)
            }
            Calendar.SUNDAY -> {
                return intent.getBooleanExtra(SUNDAY, false)
            }
        }
        return false
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    companion object {
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
        const val RECURRING = "RECURRING"
        const val TITLE = "TITLE"
        const val ALARAM_ID = "ALARAM_ID"
    }
}