package com.ko2ic.spike.glance

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val pm = applicationContext.packageManager
            val componentName =
                ComponentName("com.ko2ic.spike.glance", "com.ko2ic.spike.glance.MyGlanceAppWidgetReceiver")
            pm.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            val componentName2 =
                ComponentName("com.ko2ic.spike.glance", "com.ko2ic.spike.glance.MyGlanceAppWidgetReceiver2")
            pm.setComponentEnabledSetting(
                componentName2,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}