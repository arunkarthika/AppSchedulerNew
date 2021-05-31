package com.sankarwap.appscheduler.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.sankarwap.appscheduler.R
import me.ibrahimsn.lib.SmoothBottomBar


class MainActivity : AppCompatActivity() {
    private var ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1
    lateinit var navController: NavController
    lateinit var bottomBar: SmoothBottomBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomBar = findViewById<SmoothBottomBar>(R.id.bottomBar)
        navController = findNavController(R.id.activity_main_nav_host_fragment)
        setupSmoothBottomMenu()
        bottomBar.setOnItemReselectedListener {

        }
/*
        bottomBar.setOnItemSelectedListener {
            when (it) {
                0 -> {
                    navController.navigate(R.id.alarmsListFragment)
                }
                1 -> {
                    navController.navigate(R.id.appListFragment)
                }
                2 -> {
                    navController.navigate(R.id.scheduleFragment)
                }
            }
        }
*/
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = android.widget.PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom)
        val menu = popupMenu.menu
        bottomBar.setupWithNavController(menu, navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun RequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.packageName))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                //Permission Granted-System will work
            }
        }
    }

    override fun onResume() {
        super.onResume()
        RequestPermission()
    }
}