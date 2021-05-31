package com.sankarwap.appscheduler.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sankarwap.appscheduler.broadcastreceiver.AlarmBroadcastReceiver
import com.sankarwap.appscheduler.createalarm.DayUtil
import java.util.*

@Entity(tableName = "alarm_table")
class Alarm(@field:PrimaryKey var alarmId: Int, val hour: Int, val minute: Int, val title: String, var created: Long, var isStarted: Boolean, val isRecurring: Boolean, val isMonday: Boolean, val isTuesday: Boolean, val isWednesday: Boolean, val isThursday: Boolean, val isFriday: Boolean, val isSaturday: Boolean, val isSunday: Boolean) {
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.putExtra(AlarmBroadcastReceiver.ALARAM_ID,alarmId)
        intent.putExtra(AlarmBroadcastReceiver.RECURRING, isRecurring)
        intent.putExtra(AlarmBroadcastReceiver.MONDAY, isMonday)
        intent.putExtra(AlarmBroadcastReceiver.TUESDAY, isTuesday)
        intent.putExtra(AlarmBroadcastReceiver.WEDNESDAY, isWednesday)
        intent.putExtra(AlarmBroadcastReceiver.THURSDAY, isThursday)
        intent.putExtra(AlarmBroadcastReceiver.FRIDAY, isFriday)
        intent.putExtra(AlarmBroadcastReceiver.SATURDAY, isSaturday)
        intent.putExtra(AlarmBroadcastReceiver.SUNDAY, isSunday)
        intent.putExtra(AlarmBroadcastReceiver.TITLE, title)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        // if alarm time has already passed, increment day by 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar[Calendar.DAY_OF_MONTH] = calendar[Calendar.DAY_OF_MONTH] + 1
        }
        if (!isRecurring) {
            var toastText: String? = null
            try {
                toastText = String.format("One Time  %s Scheduled for %s at %02d:%02d", title, DayUtil.toDay(calendar[Calendar.DAY_OF_WEEK]), hour, minute, alarmId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        alarmPendingIntent
                )
            }
        } else {
            val toastText = String.format("Repeat  %s Scheduled for %s at %02d:%02d", title, recurringDaysText, hour, minute, alarmId)
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    RUN_DAILY,
                    alarmPendingIntent
            )
        }
        isStarted = true
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        alarmManager.cancel(alarmPendingIntent)
        isStarted = false
        val toastText = String.format("Schedule cancelled for %02d:%02d with id %d", hour, minute, alarmId)
//        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        Log.i("cancel", toastText)
    }

    val recurringDaysText: String?
        get() {
            if (!isRecurring) {
                return null
            }
            var days = ""
            if (isMonday) {
                days += "Mo "
            }
            if (isTuesday) {
                days += "Tu "
            }
            if (isWednesday) {
                days += "We "
            }
            if (isThursday) {
                days += "Th "
            }
            if (isFriday) {
                days += "Fr "
            }
            if (isSaturday) {
                days += "Sa "
            }
            if (isSunday) {
                days += "Su "
            }
            return days
        }
}