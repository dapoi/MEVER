package com.dapascript.mever.feature.setting.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp18
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp60
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.isDarkMode
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.navigateToBrowser
import com.dapascript.mever.core.navigation.helper.Navigator

@Composable
internal fun SettingSubscriptionDetailScreen(navigator: Navigator) {
    val context = LocalContext.current
    val isDarkMode = isDarkMode
    val scrollState = rememberScrollState()
    val showDivider by remember {
        derivedStateOf { scrollState.canScrollForward }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(title = stringResource(R.string.subscription_details)),
        onBackHandler = { navigator.navigateBack() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp60)
        ) {
            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = Dp20, vertical = Dp16),
                verticalArrangement = spacedBy(Dp20)
            ) {
                // Active Membership Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dp20))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    colors.lightPurpleDarkGray,
                                    colors.whiteDarkGray
                                )
                            )
                        )
                        .border(
                            width = Dp1,
                            color = MeverPurple.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(Dp20)
                        )
                        .padding(Dp16)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = spacedBy(Dp14)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceBetween,
                            verticalAlignment = CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = spacedBy(Dp12),
                                verticalAlignment = CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(Dp40)
                                        .clip(CircleShape)
                                        .background(MeverPurple),
                                    contentAlignment = Center
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
                                        contentDescription = stringResource(R.string.mever_pro),
                                        tint = MeverWhite,
                                        modifier = Modifier.size(Dp20)
                                    )
                                }
                                Column(verticalArrangement = spacedBy(Dp2)) {
                                    Text(
                                        text = stringResource(R.string.mever_pro),
                                        style = typography.bodyBold1,
                                        color = colors.blackWhite
                                    )
                                    Text(
                                        text = stringResource(R.string.pro_membership_active),
                                        style = typography.label2,
                                        color = colors.blackWhite.copy(alpha = 0.65f)
                                    )
                                }
                            }

                            // Active Status Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(Dp20))
                                    .background(MeverPurple.copy(alpha = 0.15f))
                                    .border(
                                        width = Dp1,
                                        color = MeverPurple.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(Dp20)
                                    )
                                    .padding(horizontal = Dp12, vertical = Dp6),
                                contentAlignment = Center
                            ) {
                                Text(
                                    text = stringResource(R.string.active),
                                    style = typography.labelBold1,
                                    color = MeverPurple
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = Dp1,
                            color = colors.blackWhite.copy(alpha = 0.08f)
                        )

                        Text(
                            text = stringResource(R.string.managed_via_google_play),
                            style = typography.body2,
                            color = colors.blackWhite.copy(alpha = 0.7f)
                        )
                    }
                }

                // Section Title: Included Benefits
                Text(
                    text = stringResource(R.string.included_benefits),
                    style = typography.h3,
                    color = colors.blackWhite
                )

                // Benefits Card with Dedicated Assets
                val benefits = listOf(
                    SubscriptionBenefitItem(
                        iconRes = R.drawable.ic_no_ads,
                        titleRes = R.string.feature_no_ads,
                        descRes = R.string.feature_no_ads_desc
                    ),
                    SubscriptionBenefitItem(
                        iconRes = R.drawable.ic_quick_download_notif,
                        titleRes = R.string.quick_download_notif_title,
                        descRes = R.string.feature_quick_download_desc
                    ),
                    SubscriptionBenefitItem(
                        iconRes = R.drawable.ic_art_gallery,
                        titleRes = R.string.feature_customization,
                        descRes = R.string.feature_customization_desc
                    ),
                    SubscriptionBenefitItem(
                        iconRes = R.drawable.ic_heart,
                        titleRes = R.string.feature_support_dev,
                        descRes = R.string.feature_support_dev_desc
                    )
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dp20))
                        .border(
                            width = Dp1,
                            color = MeverPurple.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(Dp20)
                        ),
                    color = colors.lightPurpleDarkGray
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dp6)
                    ) {
                        benefits.forEachIndexed { index, benefit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Dp16, vertical = Dp10),
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = spacedBy(Dp14)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(Dp40)
                                        .clip(RoundedCornerShape(Dp10))
                                        .background(
                                            if (isDarkMode) colors.whiteDarkGray
                                            else MeverWhite
                                        )
                                        .border(
                                            width = Dp1,
                                            color = MeverPurple.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(Dp10)
                                        ),
                                    contentAlignment = Center
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(benefit.iconRes),
                                        contentDescription = stringResource(benefit.titleRes),
                                        tint = MeverPurple,
                                        modifier = Modifier.size(Dp20)
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = spacedBy(Dp2)
                                ) {
                                    Text(
                                        text = stringResource(benefit.titleRes),
                                        style = typography.bodyBold2,
                                        color = colors.blackWhite
                                    )
                                    Text(
                                        text = stringResource(benefit.descRes),
                                        style = typography.label2,
                                        color = colors.blackWhite.copy(alpha = 0.65f)
                                    )
                                }

                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_round_checked),
                                    contentDescription = "Included in Pro",
                                    tint = MeverPurple,
                                    modifier = Modifier.size(Dp18)
                                )
                            }

                            if (index < benefits.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Dp16),
                                    thickness = Dp1,
                                    color = colors.blackWhite.copy(alpha = 0.06f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dp8))
            }

            // Divider above Sticky Bottom Section (Only when scrollable)
            if (showDivider) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = Dp1,
                    color = colors.blackWhite.copy(alpha = 0.08f)
                )
            }

            // Sticky Bottom Bar: Manage Subscription Action
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.whiteDark)
                    .navigationBarsPadding()
                    .padding(horizontal = Dp20, vertical = Dp12)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp48)
                        .clip(RoundedCornerShape(Dp24))
                        .background(colors.lightPurpleDarkGray)
                        .border(
                            width = Dp1,
                            color = MeverPurple.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(Dp24)
                        )
                        .clickable {
                            navigateToBrowser(
                                context,
                                "https://play.google.com/store/account/subscriptions?package=com.dapascript.mever"
                            )
                        },
                    contentAlignment = Center
                ) {
                    Row(
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = spacedBy(Dp8)
                    ) {
                        Text(
                            text = stringResource(R.string.manage_subscription),
                            style = typography.bodyBold1,
                            color = MeverPurple
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_link),
                            contentDescription = null,
                            tint = MeverPurple,
                            modifier = Modifier.size(Dp16)
                        )
                    }
                }
            }
        }
    }
}

private data class SubscriptionBenefitItem(
    val iconRes: Int,
    val titleRes: Int,
    val descRes: Int
)