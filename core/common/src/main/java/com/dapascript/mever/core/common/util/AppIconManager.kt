package com.dapascript.mever.core.common.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.dapascript.mever.core.common.util.AppIconType.Default

object AppIconManager {

    private fun getAliasComponentName(context: Context, icon: AppIconType): ComponentName {
        val packageManager = context.packageManager
        val appPackageName = context.packageName
        val aliasSimpleName = "MainActivity${icon.aliasSuffix}"

        val candidates = listOf(
            ComponentName(appPackageName, "$appPackageName.$aliasSimpleName"),
            ComponentName(appPackageName, "com.dapascript.mever.$aliasSimpleName")
        )

        for (component in candidates) {
            try {
                packageManager.getActivityInfo(component, 0)
                return component
            } catch (_: Exception) {
                // Try next candidate
            }
        }
        return candidates.first()
    }

    fun getCurrentIcon(context: Context): AppIconType {
        val packageManager = context.packageManager

        AppIconType.entries.forEach { icon ->
            val componentName = getAliasComponentName(context, icon)
            try {
                val state = packageManager.getComponentEnabledSetting(componentName)
                if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                    return icon
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return Default
    }

    fun isProIconActive(context: Context): Boolean {
        return getCurrentIcon(context).isProOnly
    }

    fun resetToDefaultIfProOnly(context: Context) {
        if (isProIconActive(context)) {
            setAppIcon(context, Default)
        }
    }

    fun setAppIcon(context: Context, targetIcon: AppIconType) {
        val packageManager = context.packageManager

        // 1. Enable target icon FIRST so the launcher always has at least one valid launcher activity
        val targetComponent = getAliasComponentName(context, targetIcon)
        try {
            packageManager.setComponentEnabledSetting(
                targetComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. Disable all other icons
        AppIconType.entries.filter { it != targetIcon }.forEach { icon ->
            val otherComponent = getAliasComponentName(context, icon)
            try {
                if (packageManager.getComponentEnabledSetting(otherComponent) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                    packageManager.setComponentEnabledSetting(
                        otherComponent,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
