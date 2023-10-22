package com.ko2ic.spike.glance

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

class MyGlanceAppWidgetConfigure2 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (Build.VERSION_CODES.TIRAMISU <= Build.VERSION.SDK_INT) {
                FeatureThatRequiresNotificationPermission()
            } else {
                updateWidgetIdAndFinishIfNotWidgetRun()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun FeatureThatRequiresNotificationPermission() {

        val permissionState = rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        )

        if (permissionState.status.isGranted) {
            updateWidgetIdAndFinishIfNotWidgetRun()
        } else {
            SideEffect {
                permissionState.launchPermissionRequest()
            }
        }
    }

    /**
     * widgetIDを更新する。widgetからの動作じゃなければ終わらせる
     */
    private fun updateWidgetIdAndFinishIfNotWidgetRun() {
        val launchIntent = intent
        val extras = launchIntent.extras
        if (extras != null) {
            val appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            lifecycleScope.launch {
                val glanceAppWidgetManager = GlanceAppWidgetManager(this@MyGlanceAppWidgetConfigure2)
                val glanceId: GlanceId = glanceAppWidgetManager.getGlanceIdBy(appWidgetId)
                updateAppWidgetState(context = this@MyGlanceAppWidgetConfigure2, glanceId = glanceId) {
                }
                val glanceAppWidget: GlanceAppWidget = MyGlanceAppWidget2()
                glanceAppWidget.update(this@MyGlanceAppWidgetConfigure2, glanceId)
                val resultValue = Intent()
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                setResult(RESULT_OK, resultValue)
                finishAfterTransition()
                overridePendingTransition(0, 0)
            }
        } else {
            finishAfterTransition()
            overridePendingTransition(0, 0)
        }
    }
}
