package com.ko2ic.spike.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyGlanceAppWidgetReceiver2 : GlanceAppWidgetReceiver() {

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override val glanceAppWidget: GlanceAppWidget = MyGlanceAppWidget2()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        update(context)
    }

    private fun update(context: Context) {
        scope.launch {
            val ids = GlanceAppWidgetManager(context).getGlanceIds(MyGlanceAppWidget2::class.java)
            ids.forEach { id ->
                MyGlanceAppWidget2().update(context, id)
            }
        }
    }
}