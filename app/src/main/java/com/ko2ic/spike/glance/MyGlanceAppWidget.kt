package com.ko2ic.spike.glance

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class MyGlanceAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        GlanceTheme {
                LazyColumn(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .appWidgetBackground()
                        .background(GlanceTheme.colors.background)
                ) {
                    items(samples.size) { id ->
                        Text(
                            text = "${LocalContext.current.getString(R.string.widget_title)} ${samples[id]}",
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            ),
                        )
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