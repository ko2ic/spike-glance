package com.ko2ic.spike.glance.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ko2ic.spike.glance.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MyForegroundService : Service(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelId = "service"

        val builder = NotificationCompat.Builder(applicationContext, channelId).also {
            it.setContentTitle("通知のタイトルです")
            it.setContentText("通知のテキストです")
            it.setSmallIcon(R.drawable.ic_launcher_foreground)
        }.build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(channelId) == null) {
                val mChannel =
                    NotificationChannel(channelId, "チャンネルのタイトル", NotificationManager.IMPORTANCE_HIGH)
                mChannel.apply {
                    description = "概要"
                }
                manager.createNotificationChannel(mChannel)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, builder, FOREGROUND_SERVICE_TYPE_SHORT_SERVICE)
        } else {
            startForeground(1, builder)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }
}