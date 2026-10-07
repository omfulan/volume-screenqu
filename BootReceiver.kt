package com.fikriteknik.volumescreencontrol

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                // Accessibility Service is controlled by Android.
                // This receiver exists to keep the app boot-aware and ready.
                // Do not attempt to silently enable Accessibility: Android blocks that.
                val prefs = context.getSharedPreferences("app", Context.MODE_PRIVATE)
                prefs.edit().putLong("last_boot_event", System.currentTimeMillis()).apply()
            }
        }
    }
}
