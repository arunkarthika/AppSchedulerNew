package com.sankarwap.appscheduler.createalarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sankarwap.appscheduler.data.Alarm
import com.sankarwap.appscheduler.data.AlarmRepository

class CreateAlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmRepository: AlarmRepository
    fun insert(alarm: Alarm?) {
        alarmRepository.insert(alarm)
    }

    init {
        alarmRepository = AlarmRepository(application)
    }
}