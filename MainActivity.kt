package com.fikriteknik.volumescreencontrol

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var status: TextView
    private lateinit var toggle: Button

    private fun adminComponent() =
        ComponentName(this, ScreenLockAdminReceiver::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.txtStatus)
        toggle = findViewById(R.id.btnToggle)

        toggle.setOnClickListener {
            if (isReady()) {
                setEnabled(!isEnabled())
            } else {
                openRequiredSettings()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun isEnabled(): Boolean =
        getSharedPreferences("app", MODE_PRIVATE).getBoolean("enabled", false)

    private fun setEnabled(value: Boolean) {
        getSharedPreferences("app", MODE_PRIVATE)
            .edit()
            .putBoolean("enabled", value)
            .apply()
        updateUi()
    }

    private fun isAccessibilityEnabled(): Boolean {
        return try {
            val enabled = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: ""
            enabled.contains(packageName)
        } catch (_: Exception) {
            false
        }
    }

    private fun isAdminEnabled(): Boolean {
        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return dpm.isAdminActive(adminComponent())
    }

    private fun isBatteryExempt(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(packageName)
    }

    private fun isReady(): Boolean =
        isAccessibilityEnabled() && isAdminEnabled() && isBatteryExempt()

    private fun updateUi() {
        if (isEnabled()) {
            status.text = "AKTIF"
            toggle.text = "MATIKAN"
        } else {
            status.text = "NONAKTIF"
            toggle.text = "AKTIFKAN"
        }
    }

    private fun openRequiredSettings() {
        when {
            !isAccessibilityEnabled() ->
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

            !isAdminEnabled() ->
                startActivity(Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent())
                    putExtra(
                        DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Izin ini diperlukan agar Volume Down dapat mengunci layar."
                    )
                })

            !isBatteryExempt() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                try {
                    startActivity(
                        Intent(
                            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                            Uri.parse("package:$packageName")
                        )
                    )
                } catch (_: Exception) {
                    startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                }
            }
        }
    }
}
