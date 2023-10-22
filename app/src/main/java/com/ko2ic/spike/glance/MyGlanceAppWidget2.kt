package com.ko2ic.spike.glance

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Row
import androidx.glance.layout.padding

class MyGlanceAppWidget2 : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val isCheck = remember {
                mutableStateOf(false)
            }

            Row(modifier = GlanceModifier.background(ColorProvider(Color.White, Color.Black))) {
                val checkText = if (isCheck.value) "Checked" else "Unchecked"
                CheckBox(
                    modifier = GlanceModifier.padding(16.dp),
                    checked = isCheck.value,
                    onCheckedChange = {
                        isCheck.value = !isCheck.value
                    },
                    text = checkText
                )
            }
        }
    }
}
