package com.sankarwap.appscheduler.data.App

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_table")
data class App(val name: String,
               val icon: String,
               val packages: String,
               val enable: Boolean,
               @PrimaryKey(autoGenerate = false) val id: Int? = null)