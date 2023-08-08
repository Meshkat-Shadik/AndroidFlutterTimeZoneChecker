package com.example.time_test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import io.flutter.plugin.common.EventChannel

class TimeChangedReceiver(private val eventSink: EventChannel.EventSink?) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_TIMEZONE_CHANGED) {
            // Send event to Flutter
            Log.d("TimezoneChangeReceiver", "Timezone changed")
            Toast.makeText(context, "time change " + intent.getAction(), Toast.LENGTH_SHORT).show();
            eventSink?.success("Timezone changed")
        }
    }
}