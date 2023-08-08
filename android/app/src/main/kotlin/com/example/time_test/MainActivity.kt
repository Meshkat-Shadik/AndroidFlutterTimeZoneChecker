package com.example.time_test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.time_test"
    private var eventSink: EventChannel.EventSink? = null

        //for the very first time when the app is installed
    private fun checkAutoTimeSettings(): Boolean {
        val autoTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME)
        } else {
            Settings.System.getInt(contentResolver, Settings.System.AUTO_TIME)
        }
        val autoTimeZone = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME_ZONE)
        } else {
            Settings.System.getInt(contentResolver, Settings.System.AUTO_TIME_ZONE)
        }
        return autoTime == 1 && autoTimeZone == 1
    }

    private val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            Log.d("MainActivity", "onChange")
            val autoTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Log.d("MainActivity", "Ok Version 1 inside onChange")
                Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME)
            } else {
                Settings.System.getInt(contentResolver, Settings.System.AUTO_TIME)
            }
            val autoTimeZone = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Log.d("MainActivity", "Ok Version 2 inside onChange")
                Settings.Global.getInt(contentResolver, Settings.Global.AUTO_TIME_ZONE)
            } else {
                Settings.System.getInt(contentResolver, Settings.System.AUTO_TIME_ZONE)
            }
            Log.d("MainActivity", "autoTime: $autoTime, autoTimeZone: $autoTimeZone")
            val bothEnabled = autoTime == 1 && autoTimeZone == 1
            eventSink?.success(bothEnabled)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        val eventChannel = EventChannel(flutterEngine?.dartExecutor?.binaryMessenger, "$CHANNEL/event")
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {

            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                Log.d("MainActivity", "onListen")
                eventSink = events
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    Log.d("MainActivity", "Ok Version")
                    val bothEnabled = checkAutoTimeSettings()
                    eventSink?.success(bothEnabled)
                    contentResolver.registerContentObserver(Settings.Global.CONTENT_URI, true, observer)
                }
                else{
                    contentResolver.registerContentObserver(Settings.System.CONTENT_URI, true, observer)
                }
            }

            override fun onCancel(arguments: Any?) {
                Log.d("MainActivity", "onCancel")
                contentResolver.unregisterContentObserver(observer)
                eventSink = null
            }
        })
    }
}
