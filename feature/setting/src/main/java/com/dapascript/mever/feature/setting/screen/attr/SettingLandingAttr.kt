package com.dapascript.mever.feature.setting.screen.attr

import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverCreamSemiPink
import com.dapascript.mever.core.common.ui.theme.MeverLightBlue
import com.dapascript.mever.core.common.ui.theme.MeverLightGray2
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverViolet
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.SettingMenus.SubMenu

object SettingLandingAttr {
    data class SettingMenus(
        val header: String,
        val menus: List<SubMenu>
    ) {
        data class SubMenu(
            val leadingTitle: String,
            val icon: Int,
            val iconBackgroundColor: Color,
            val trailingTitle: String? = null
        )
    }

    fun getSettingMenus() = listOf(
        SettingMenus(
            header = "Application",
            menus = listOf(
                SubMenu(
                    leadingTitle = "Language",
                    icon = R.drawable.ic_language,
                    iconBackgroundColor = MeverLightBlue,
                    trailingTitle = ""
                ),
                SubMenu(
                    leadingTitle = "Notification",
                    icon = R.drawable.ic_notif,
                    iconBackgroundColor = MeverCreamSemiPink
                ),
                SubMenu(
                    leadingTitle = "Theme",
                    icon = R.drawable.ic_theme,
                    iconBackgroundColor = MeverViolet,
                    trailingTitle = ""
                )
            )
        ),
        SettingMenus(
            header = "Donation",
            menus = listOf(
                SubMenu(
                    leadingTitle = "Bitcoin",
                    icon = R.drawable.ic_btc,
                    iconBackgroundColor = MeverCreamSemiPink
                ),
                SubMenu(
                    leadingTitle = "Paypal",
                    icon = R.drawable.ic_paypal,
                    iconBackgroundColor = MeverLightBlue
                ),
                SubMenu(
                    leadingTitle = "QRIS",
                    icon = R.drawable.ic_qris,
                    iconBackgroundColor = MeverPink
                )
            )
        ),
        SettingMenus(
            header = "Support",
            menus = listOf(
                SubMenu(
                    leadingTitle = "Contact Us",
                    icon = R.drawable.ic_cs,
                    iconBackgroundColor = MeverLightGray2
                ),
                SubMenu(
                    leadingTitle = "About Application",
                    icon = R.drawable.ic_about,
                    iconBackgroundColor = MeverViolet
                )
            )
        )
    )
}