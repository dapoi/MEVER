package com.dapascript.mever.feature.setting.screen.attr

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverCreamPink
import com.dapascript.mever.core.common.ui.theme.MeverLightBlue
import com.dapascript.mever.core.common.ui.theme.MeverLightPurple
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverViolet
import com.dapascript.mever.core.common.util.getAppVersion
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.SettingMenus.SubMenu

object SettingLandingAttr {
    data class SettingMenus(
        val header: Int,
        val menus: List<SubMenu>
    ) {
        data class SubMenu(
            val leadingTitle: String,
            val icon: Int,
            val iconBackgroundColor: Color,
            val trailingTitle: String? = null,
            val leadingDesc: String? = null
        )
    }

    fun getSettingMenus(context: Context) = with(context){
        listOf(
            SettingMenus(
                header = R.string.application,
                menus = listOf(
                    SubMenu(
                        leadingTitle = getString(R.string.language),
                        icon = R.drawable.ic_language,
                        iconBackgroundColor = MeverLightBlue,
                        trailingTitle = ""
                    ),
                    SubMenu(
                        leadingTitle = getString(R.string.notification),
                        icon = R.drawable.ic_notif,
                        iconBackgroundColor = MeverCreamPink
                    ),
                    SubMenu(
                        leadingTitle = getString(R.string.theme),
                        icon = R.drawable.ic_theme,
                        iconBackgroundColor = MeverViolet,
                        trailingTitle = ""
                    ),
                    SubMenu(
                        leadingTitle = getString(R.string.clean_cache),
                        icon = R.drawable.ic_cache,
                        iconBackgroundColor = MeverLightBlue
                    ),
                    SubMenu(
                        leadingTitle = getString(R.string.pip),
                        leadingDesc = getString(R.string.when_video_is_playing),
                        icon = R.drawable.ic_pip,
                        iconBackgroundColor = MeverLightPurple
                    )
                )
            ),
            SettingMenus(
                header = R.string.appreciate,
                menus = listOf(
                    SubMenu(
                        leadingTitle = getString(R.string.paypal),
                        icon = R.drawable.ic_paypal,
                        iconBackgroundColor = MeverLightBlue
                    ),
                    SubMenu(
                        leadingTitle = getString(R.string.qris),
                        icon = R.drawable.ic_qris,
                        iconBackgroundColor = MeverPink
                    )
                )
            ),
            SettingMenus(
                header = R.string.support,
                menus = listOf(
                    SubMenu(
                        leadingTitle =getString(R.string.faq),
                        icon = R.drawable.ic_faq,
                        iconBackgroundColor = MeverCreamPink
                    ),
                    SubMenu(
                        leadingTitle = getString(R.string.contact),
                        icon = R.drawable.ic_cs,
                        iconBackgroundColor = MeverLightBlue
                    ),
                    SubMenu(
                        leadingTitle = getString(R.string.about),
                        leadingDesc = getAppVersion(context),
                        icon = R.drawable.ic_about,
                        iconBackgroundColor = MeverViolet
                    )
                )
            )
        )
    }
}