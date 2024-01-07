package com.ko2ic.spike.glance.worker

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ko2ic.spike.glance.MyGlanceAppWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyWorker constructor(
    private val context: Context,
    private val params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {

        const val KEY_APP_WIDGET_IDS = "key_app_widget_ids"
        fun start(context: Context, appWidgetIds: IntArray) {
            val request = OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(
                    workDataOf(
                        KEY_APP_WIDGET_IDS to appWidgetIds,
                    )
                )
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    override suspend fun doWork() = withContext(Dispatchers.IO) {
        val glanceAppWidgetManager = GlanceAppWidgetManager(context)
        val appWidgetIds = params.inputData.getIntArray(KEY_APP_WIDGET_IDS) ?: throw IllegalArgumentException()
        val widget = MyGlanceAppWidget()
        appWidgetIds
            .map { appWidgetId -> glanceAppWidgetManager.getGlanceIdBy(appWidgetId) }
            .map { glanceId ->
                launch {
                    updateAppWidgetState(context, glanceId) { preferences ->
                        val current = preferences[MyGlanceAppWidget.PREF_KEY_LIST] ?: MyGlanceAppWidget.INITIAL_STATE
                        preferences[MyGlanceAppWidget.PREF_KEY_LIST] =
                            current.map { (it.toInt() * it.toInt()).toString() }.toSet()
                    }
                    widget.update(context, glanceId)
                }
            }
            .joinAll()

        Result.success()
    }
}