package com.ko2ic.spike.glance.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver : BroadcastReceiver() {

    companion object {

        const val ACTION_RESTART_APP = "ACTION_RESTART_APP"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.action.equals(ACTION_RESTART_APP)) {
            restartApplication(context)
        }
    }

    private fun restartApplication(context: Context) {
        val restartIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
        restartIntent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(restartIntent)
    }
}