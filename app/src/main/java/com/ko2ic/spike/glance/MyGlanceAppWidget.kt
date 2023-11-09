package com.ko2ic.spike.glance

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.ko2ic.spike.glance.service.MyForegroundService

class MyGlanceAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
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
                        text = "開始",
                        onClick = actionStartService<MyForegroundService>(
                            isForegroundService = true,
                        ),
                    )
                    Spacer(
                        modifier = GlanceModifier.width(16.dp)
                    )
                    Button(
                        text = "終了",
                        onClick = {
                            val intent = Intent(context, MyForegroundService::class.java)
                            context.stopService(intent)
                        })
                }
                LazyColumn(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(samples.size) { id ->
                        Text(
                            text = "${LocalContext.current.getString(R.string.widget_title)} ${samples[id]}",
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable(
                                    actionStartActivity<MainActivity>(
                                        actionParametersOf(
                                            ActionParameters.Key<Int>(MainActivity.widgetItemKey) to id
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

    private val samples = (0..5).map { it }
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

