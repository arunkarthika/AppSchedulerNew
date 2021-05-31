package com.sankarwap.appscheduler.util

import com.sankarwap.appscheduler.data.App.AppList

interface OnToggleAppListener {
    fun onToggle(app: AppList?, checked: Boolean?)
}