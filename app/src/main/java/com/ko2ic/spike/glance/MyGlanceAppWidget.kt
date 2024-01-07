package com.ko2ic.spike.glance

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.ko2ic.spike.glance.receiver.MyBroadcastReceiver
import com.ko2ic.spike.glance.service.MyForegroundService
import com.ko2ic.spike.glance.worker.MyWorker

class MyGlanceAppWidget : GlanceAppWidget() {

    companion object {

        val PREF_KEY_LIST = stringSetPreferencesKey("pref_list")
        val INITIAL_STATE = setOf("0", "1", "2", "3", "4", "5")
    }

    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val pref: Preferences = currentState()
            val state = pref[PREF_KEY_LIST] ?: INITIAL_STATE
            Content(id, state)
        }
    }

    @Composable
    fun Content(id: GlanceId, state: Set<String>) {
        val context = LocalContext.current
        GlanceTheme {
            Column(
                modifier =
                GlanceModifier
                    .fillMaxWidth()
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.primaryContainer)
            ) {
                Row(
                    modifier =
                    GlanceModifier.fillMaxWidth().padding(top = 4.dp, start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Button(
                        text = "サービス開始(通知表示)",
                        onClick = actionStartService<MyForegroundService>(
                            isForegroundService = true,
                        ),
                    )
                    Spacer(
                        modifier = GlanceModifier.width(4.dp)
                    )
                    Button(
                        text = "終了(通知削除)",
                        onClick = {
                            val intent = Intent(context, MyForegroundService::class.java)
                            context.stopService(intent)
                        })
                }
                Row(
                    modifier =
                    GlanceModifier.fillMaxWidth().padding(top = 4.dp, start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Button(
                        text = "Broadcast送信(アプリ再起動)",
                        onClick = actionSendBroadcast(
                            MyBroadcastReceiver.ACTION_RESTART_APP,
                            ComponentName(context, MyBroadcastReceiver::class.java)
                        ),
                    )
                    Spacer(
                        modifier = GlanceModifier.width(4.dp)
                    )
                    Button(
                        text = "Worker起動",
                        onClick = {
                            val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
                            MyWorker.start(context, intArrayOf(appWidgetId))
                        })
                }
                LazyColumn(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(state.toList()) { id ->
                        Text(
                            text = "${LocalContext.current.getString(R.string.widget_title)} $id",
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable(
                                    actionStartActivity<MainActivity>(
                                        actionParametersOf(
                                            ActionParameters.Key<Int>(MainActivity.widgetItemKey) to id.toInt()
                                        )
                                    )
                                ),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            ),
                        )
                    }
                }
            }
        }
    }

    suspend fun updateList(context: Context, glanceId: GlanceId) {
        updateAppWidgetState(context, glanceId) { preferences ->
            val current = preferences[PREF_KEY_LIST] ?: INITIAL_STATE
            preferences[PREF_KEY_LIST] = current.map { (it.toInt() * it.toInt()).toString() }.toSet()
        }
        update(context, glanceId)
    }
}

//fun GlanceModifier.appWidgetBackgroundCornerRadius(): GlanceModifier {
//    if (Build.VERSION.SDK_INT >= 31) {
//        cornerRadius(android.R.dimen.system_app_widget_background_radius)
//    } else {
//        cornerRadius(16.dp)
//    }
//    return this
//}
//
//fun GlanceModifier.appWidgetInnerCornerRadius(): GlanceModifier {
//    if (Build.VERSION.SDK_INT >= 31) {
//        cornerRadius(android.R.dimen.system_app_widget_inner_radius)
//    } else {
//        cornerRadius(8.dp)
//    }
//    return this
//}

