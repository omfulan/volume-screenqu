package com.fikriteknik.volumescreencontrol

import android.accessibilityservice.AccessibilityService
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.PowerManager
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

class VolumeAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN) return super.onKeyEvent(event)

        val enabled = getSharedPreferences("app", Context.MODE_PRIVATE)
            .getBoolean("enabled", false)

        if (!enabled) return super.onKeyEvent(event)

        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                wakeScreen(this)
                return true
            }

            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE)
                    as DevicePolicyManager
                val admin = ComponentName(this, ScreenLockAdminReceiver::class.java)

                if (dpm.isAdminActive(admin)) {
                    dpm.lockNow()
                    return true
                }
            }
        }

        return super.onKeyEvent(event)
    }

    companion object {
        fun wakeScreen(context: Context) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager

            @Suppress("DEPRECATION")
            val wakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "VolumeScreenControl:WakeScreen"
            )

            @Suppress("DEPRECATION")
            wakeLock.acquire(3000L)
        }
    }
}
