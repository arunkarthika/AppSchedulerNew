package com.sankarwap.appscheduler.alarmslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sankarwap.appscheduler.data.Alarm
import com.sankarwap.appscheduler.data.AlarmRepository

class AlarmsListViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmRepository: AlarmRepository
    val alarmsLiveData: LiveData<List<Alarm>>

    fun update(alarm: Alarm?) {
        alarmRepository.update(alarm)
    }
    fun delete_alaram(alarmId: Int) {
        alarmRepository.delete(alarmId)
    }

    init {
        alarmRepository = AlarmRepository(application)
        alarmsLiveData = alarmRepository.alarmsLiveData
    }
}