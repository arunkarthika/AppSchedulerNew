package com.sankarwap.appscheduler.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room

class AlarmRepository(application: Application?) {
    private val alarmDao: AlarmDao?
    val alarmsLiveData: LiveData<List<Alarm>>
    fun insert(alarm: Alarm?) {
        alarmDao!!.insert(alarm)
    }

    fun update(alarm: Alarm?) {
      alarmDao!!.update(alarm)
    }

    fun delete(alaram_id:Int){
        alarmDao!!.deleteByalaramId(alaram_id)
    }

    init {
//        val db: AlarmDatabase = AlarmDatabase.getDatabase(application)
        val db= Room.databaseBuilder(application!!, AlarmDatabase::class.java, "alaram_database").allowMainThreadQueries().build()

        alarmDao = db.alarmDao()
        alarmsLiveData = alarmDao!!.alarms
    }
}