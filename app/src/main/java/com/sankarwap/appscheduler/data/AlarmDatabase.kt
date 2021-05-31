package com.sankarwap.appscheduler.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sankarwap.appscheduler.data.App.App
import com.sankarwap.appscheduler.data.App.AppDao

@Database(entities = [Alarm::class,App::class], version = 2, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao?
    abstract fun appDao(): AppDao?
}

