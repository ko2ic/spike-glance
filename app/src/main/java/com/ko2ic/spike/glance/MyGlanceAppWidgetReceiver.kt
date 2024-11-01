package com.ko2ic.spike.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyGlanceAppWidgetReceiver : GlanceAppWidgetReceiver() {

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override val glanceAppWidget: GlanceAppWidget = MyGlanceAppWidget()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyGlanceAppWidget", "MyGlanceAppWidgetReceiver:${intent.action}")
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        update(context)
    }

    private fun update(context: Context) {
        scope.launch {
            val ids = GlanceAppWidgetManager(context).getGlanceIds(MyGlanceAppWidget::class.java)
            ids.forEach { id ->
                MyGlanceAppWidget().update(context, id)
            }
        }
    }
}