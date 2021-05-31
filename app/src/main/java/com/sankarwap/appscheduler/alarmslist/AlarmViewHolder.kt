package com.sankarwap.appscheduler.alarmslist

import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.sankarwap.appscheduler.R
import com.sankarwap.appscheduler.data.Alarm
import java.text.ParseException
import java.text.SimpleDateFormat

class AlarmViewHolder(itemView: View, private val listener: OnToggleAlarmListener) : RecyclerView.ViewHolder(itemView) {
    private val alarmTime: TextView = itemView.findViewById(R.id.item_alarm_time)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val day: TextView = itemView.findViewById(R.id.day)
    var alarmStarted: SwitchMaterial = itemView.findViewById(R.id.item_alarm_started)
    fun bind(alarm: Alarm) {
        var alarmText: String? = String.format("%02d:%02d", alarm.hour, alarm.minute)
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(alarmText)
            alarmText = SimpleDateFormat("hh:mm a").format(dateObj)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        alarmTime.text = alarmText
        alarmStarted.isChecked = alarm.isStarted
        if (alarm.isRecurring) {
//            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            day.text = alarm.recurringDaysText
        } else {
//            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            day.text = "Once"
        }
        if (alarm.title.isNotEmpty()) {
            title.text = String.format("%s", alarm.title)
        } else {
            title.text = String.format("%s", "Schedule")
        }
        alarmStarted.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> listener.onToggle(alarm) }
    }

}