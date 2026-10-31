package com.dapascript.mever.feature.setting.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp220
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverGray
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.isDarkMode
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.navigateToBrowser
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.feature.setting.viewmodel.SettingPaywallViewModel

@Composable
internal fun SettingPaywallScreen(
    navigator: Navigator,
    viewModel: SettingPaywallViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        hideDefaultTopBar = true,
        useStatusBarsPadding = false,
        onBackHandler = { navigator.navigateBack() }
    ) {
        val context = LocalContext.current
        val activity = LocalActivity.current
        val isDarkMode = isDarkMode

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.whiteDark)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp220)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.paywall_header),
                            contentDescription = stringResource(R.string.mever_pro),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = Crop
                        )
                        if (isDarkMode) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MeverBlack.copy(alpha = 0.2f))
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            colors.whiteDark.copy(alpha = 0.5f),
                                            colors.whiteDark
                                        )
                                    )
                                )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp20, vertical = Dp12),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = spacedBy(Dp12)
                    ) {
                        Text(
                            text = stringResource(R.string.paywall_title),
                            style = typography.h2,
                            color = colors.blackWhite,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(R.string.paywall_desc),
                            style = typography.body2,
                            color = colors.blackWhite.copy(alpha = 0.65f),
                            textAlign = TextAlign.Center
                        )

                        PaywallComparisonTable()
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.whiteDark)
                        .navigationBarsPadding()
                        .padding(horizontal = Dp20, vertical = Dp12),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = spacedBy(Dp12)
                ) {
                    val formattedPrice = selectedPackage?.product?.price?.formatted
                    Text(
                        text = when {
                            isLoadingOfferings -> stringResource(R.string.please_wait)
                            formattedPrice != null -> stringResource(R.string.subscribe_to_pro_for, formattedPrice)
                            else -> stringResource(R.string.subscribe_to_pro_for, "$1.99/mo")
                        },
                        style = typography.body2,
                        color = colors.blackWhite.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    if (statusMessage != null) {
                        Text(
                            text = statusMessage.orEmpty(),
                            style = typography.body2,
                            color = MeverPurple,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp48)
                            .clip(RoundedCornerShape(Dp24))
                            .background(MeverPurple)
                            .clickable(enabled = !isPurchasing) {
                                purchaseSelectedPackage(activity) {
                                    navigator.navigateBack()
                                }
                            },
                        contentAlignment = Center
                    ) {
                        if (isPurchasing || isLoadingOfferings) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(Dp24),
                                color = MeverWhite,
                                strokeWidth = Dp2
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.continue_label),
                                style = typography.bodyBold1,
                                color = MeverWhite
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = spacedBy(Dp16),
                        verticalAlignment = CenterVertically
                    ) {
                        val noActiveSubMsg = stringResource(R.string.restore_no_active_subscription)
                        Text(
                            text = stringResource(R.string.restore_purchases),
                            style = typography.label2,
                            color = MeverGray,
                            modifier = Modifier.clickable(enabled = !isPurchasing) {
                                restorePurchases(noActiveSubMsg) {
                                    navigator.navigateBack()
                                }
                            }
                        )
                        Text(
                            text = "•",
                            style = typography.label2,
                            color = MeverGray
                        )
                        Text(
                            text = stringResource(R.string.terms),
                            style = typography.label2,
                            color = MeverGray,
                            modifier = Modifier.clickable {
                                navigateToBrowser(context, "https://mever.my.id/license")
                            }
                        )
                        Text(
                            text = "•",
                            style = typography.label2,
                            color = MeverGray
                        )
                        Text(
                            text = stringResource(R.string.privacy),
                            style = typography.label2,
                            color = MeverGray,
                            modifier = Modifier.clickable {
                                navigateToBrowser(context, "https://mever.my.id/privacy-policy")
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(Dp16)
                    .size(Dp32)
                    .clip(CircleShape)
                    .background(MeverBlack.copy(alpha = 0.4f))
                    .clickable { navigator.navigateBack() }
                    .align(Alignment.TopEnd),
                contentAlignment = Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                    contentDescription = "Close",
                    tint = MeverWhite,
                    modifier = Modifier.size(Dp16)
                )
            }
        }
    }
}

@Composable
private fun PaywallComparisonTable() {
    val features = listOf(
        Pair(stringResource(R.string.feature_downloader_without_limit), Pair(true, true)),
        Pair(stringResource(R.string.feature_multiple_downloads), Pair(true, true)),
        Pair(stringResource(R.string.quick_download_notif_title), Pair(false, true)),
        Pair(stringResource(R.string.feature_customization), Pair(false, true)),
        Pair(stringResource(R.string.feature_no_ads), Pair(false, true))
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dp16)),
        color = colors.whiteDarkGray
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dp4)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dp8),
                verticalAlignment = CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(2.2f)
                        .padding(horizontal = Dp12)
                )
                Box(
                    modifier = Modifier.weight(0.9f),
                    contentAlignment = Center
                ) {
                    Text(
                        text = stringResource(R.string.free),
                        style = typography.labelBold1,
                        color = MeverGray
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .background(colors.lightPurpleDarkGray)
                        .padding(vertical = Dp4),
                    contentAlignment = Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dp8))
                            .background(MeverPurple)
                            .padding(horizontal = Dp8, vertical = Dp2)
                    ) {
                        Text(
                            text = stringResource(R.string.pro),
                            style = typography.labelBold1,
                            color = MeverWhite
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = Dp1,
                color = colors.blackWhite.copy(alpha = 0.08f)
            )

            features.forEachIndexed { index, (featureName, pair) ->
                val (isFree, _) = pair
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dp8),
                    verticalAlignment = CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(2.2f)
                            .padding(horizontal = Dp12),
                        contentAlignment = CenterStart
                    ) {
                        Text(
                            text = featureName,
                            style = typography.label1,
                            color = colors.blackWhite
                        )
                    }
                    Box(
                        modifier = Modifier.weight(0.9f),
                        contentAlignment = Center
                    ) {
                        if (isFree) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_round_checked),
                                contentDescription = "Free Included",
                                tint = MeverGray,
                                modifier = Modifier.size(Dp16)
                            )
                        } else {
                            Text(
                                text = "—",
                                style = typography.body2,
                                color = MeverGray
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.9f)
                            .background(colors.lightPurpleDarkGray)
                            .padding(vertical = Dp8),
                        contentAlignment = Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_round_checked),
                            contentDescription = "Pro Included",
                            tint = MeverPurple,
                            modifier = Modifier.size(Dp16)
                        )
                    }
                }

                if (index < features.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = Dp1,
                        color = colors.blackWhite.copy(alpha = 0.06f)
                    )
                }
            }
        }
    }
}