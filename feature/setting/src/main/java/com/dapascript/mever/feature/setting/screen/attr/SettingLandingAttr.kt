package com.dapascript.mever.feature.setting.screen.attr

import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverCreamPink
import com.dapascript.mever.core.common.ui.theme.MeverLightBlue
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverViolet
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.SettingMenus.SubMenu

object SettingLandingAttr {
    const val BTC_ADDRESS = "1NSwLsd4JvewCbXMqB5WVYKcx34NwoUTkM"

    data class SettingMenus(
        val header: Int,
        val menus: List<SubMenu>
    ) {
        data class SubMenu(
            val leadingTitle: Int,
            val icon: Int,
            val iconBackgroundColor: Color,
            val trailingTitle: String? = null
        )
    }

    fun getSettingMenus() = listOf(
        SettingMenus(
            header = R.string.application,
            menus = listOf(
                SubMenu(
                    leadingTitle = R.string.language,
                    icon = R.drawable.ic_language,
                    iconBackgroundColor = MeverLightBlue,
                    trailingTitle = ""
                ),
                SubMenu(
                    leadingTitle = R.string.notification,
                    icon = R.drawable.ic_notif,
                    iconBackgroundColor = MeverCreamPink
                ),
                SubMenu(
                    leadingTitle = R.string.theme,
                    icon = R.drawable.ic_theme,
                    iconBackgroundColor = MeverViolet,
                    trailingTitle = ""
                )
            )
        ),
        SettingMenus(
            header = R.string.donation,
            menus = listOf(
                SubMenu(
                    leadingTitle = R.string.bitcoin,
                    icon = R.drawable.ic_btc,
                    iconBackgroundColor = MeverCreamPink
                ),
                SubMenu(
                    leadingTitle = R.string.paypal,
                    icon = R.drawable.ic_paypal,
                    iconBackgroundColor = MeverLightBlue
                ),
                SubMenu(
                    leadingTitle = R.string.qris,
                    icon = R.drawable.ic_qris,
                    iconBackgroundColor = MeverPink
                )
            )
        ),
        SettingMenus(
            header = R.string.support,
            menus = listOf(
                SubMenu(
                    leadingTitle = R.string.contact,
                    icon = R.drawable.ic_cs,
                    iconBackgroundColor = MeverLightGray
                ),
                SubMenu(
                    leadingTitle = R.string.about,
                    icon = R.drawable.ic_about,
                    iconBackgroundColor = MeverViolet
                )
            )
        )
    )
}