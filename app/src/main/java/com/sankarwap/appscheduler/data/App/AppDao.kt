package com.sankarwap.appscheduler.data.App

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertApps(userRoomList: App)

    @Query("SELECT packages FROM app_table")
    fun getAllPackageName(): List<String>


    @Query("select * from app_table")
    fun getData(): List<App>

    @Query("DELETE FROM app_table WHERE packages = :package_name")
    fun deleteBypackagename(package_name: String)


}