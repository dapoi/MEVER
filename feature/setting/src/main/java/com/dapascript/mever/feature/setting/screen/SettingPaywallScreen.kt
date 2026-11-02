package com.dapascript.mever.feature.setting.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp18
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverGray
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.isDarkMode
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.navigateToBrowser
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
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
        val scrollState = rememberScrollState()
        val paywallHeader = remember(isDarkMode) {
            if (isDarkMode) R.drawable.paywall_header_dark else R.drawable.paywall_header
        }
        val showDivider by remember {
            derivedStateOf { scrollState.canScrollForward }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.whiteDark)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Scrollable Content: Hero Artwork + Title + Benefits Showcase
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    horizontalAlignment = CenterHorizontally
                ) {
                    // Hero Artwork
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp200)
                            .clip(RoundedCornerShape(bottomStart = Dp32, bottomEnd = Dp32))
                    ) {
                        MeverImage(
                            modifier = Modifier.fillMaxSize(),
                            source = paywallHeader,
                            contentScale = FillBounds
                        )
                        if (isDarkMode) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MeverBlack.copy(alpha = 0.25f))
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MeverTransparent,
                                            MeverTransparent,
                                            colors.whiteDark.copy(alpha = 0.3f),
                                            colors.whiteDark.copy(alpha = 0.85f),
                                            colors.whiteDark
                                        )
                                    )
                                )
                        )
                    }

                    // Content Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp20, vertical = Dp8),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = spacedBy(Dp16)
                    ) {
                        // Premium Crown Badge Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dp20))
                                .background(colors.alwaysPurple.copy(alpha = 0.12f))
                                .border(
                                    width = Dp1,
                                    color = colors.alwaysPurple.copy(alpha = 0.35f),
                                    shape = RoundedCornerShape(Dp20)
                                )
                                .padding(horizontal = Dp14, vertical = Dp6),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = spacedBy(Dp6)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
                                contentDescription = null,
                                tint = colors.alwaysPurple,
                                modifier = Modifier.size(Dp16)
                            )
                            Text(
                                text = stringResource(R.string.mever_pro),
                                style = typography.labelBold1,
                                color = colors.alwaysPurple
                            )
                        }

                        // Title & Subtitle
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = spacedBy(Dp6)
                        ) {
                            Text(
                                text = stringResource(R.string.paywall_title),
                                style = typography.h3.copy(fontSize = Sp22),
                                color = colors.blackWhite,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.paywall_desc),
                                style = typography.body2,
                                color = colors.blackWhite.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = Dp10)
                            )
                        }

                        // Benefits Card with Dedicated Assets
                        PaywallBenefitsSection()

                        Spacer(modifier = Modifier.height(Dp12))
                    }
                }

                // Divider above Sticky Bottom Section (Only shown when scrollable forward)
                if (showDivider) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = Dp1,
                        color = colors.blackWhite.copy(alpha = 0.1f)
                    )
                }

                // Sticky Bottom Bar: Clean Price + Reassurance + CTA + Legal
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.whiteDark)
                        .navigationBarsPadding()
                        .padding(horizontal = Dp20, vertical = Dp12),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = spacedBy(Dp10)
                ) {
                    // Minimalist Price & Cancel anytime (No bulky box)
                    val formattedPrice = selectedPackage?.product?.price?.formatted
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = spacedBy(Dp2)
                    ) {
                        Text(
                            text = when {
                                isLoadingOfferings -> stringResource(R.string.please_wait)
                                formattedPrice != null -> "$formattedPrice ${stringResource(R.string.per_month)}"
                                else -> "$1.99 ${stringResource(R.string.per_month)}"
                            },
                            style = typography.bodyBold1,
                            color = colors.blackWhite
                        )
                        Text(
                            text = stringResource(R.string.cancel_anytime_google_play),
                            style = typography.label3,
                            color = colors.blackWhite.copy(alpha = 0.6f)
                        )
                    }

                    if (statusMessage != null) {
                        Text(
                            text = statusMessage.orEmpty(),
                            style = typography.body2,
                            color = colors.alwaysPurple,
                            textAlign = TextAlign.Center
                        )
                    }

                    // CTA Button
                    MeverButton(
                        title = stringResource(R.string.continue_label),
                        buttonType = MeverButtonType.Filled(
                            backgroundColor = colors.alwaysPurple,
                            contentColor = MeverWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dp48),
                        shape = RoundedCornerShape(Dp24),
                        textStyle = typography.bodyBold1,
                        isEnabled = isPurchasing.not(),
                        isLoading = isPurchasing || isLoadingOfferings,
                        trailingIcon = painterResource(R.drawable.ic_arrow_started),
                        trailingIconSize = Dp18,
                        iconSpacing = Dp8,
                        onClick = {
                            purchaseSelectedPackage(activity) {
                                navigator.navigateBack(popUpTo = SettingLandingRoute::class)
                            }
                        }
                    )

                    // Footer links
                    Row(
                        horizontalArrangement = spacedBy(Dp16),
                        verticalAlignment = CenterVertically
                    ) {
                        val noActiveSubMsg = stringResource(R.string.restore_no_active_subscription)
                        Text(
                            text = stringResource(R.string.restore_purchases),
                            style = typography.label2,
                            color = MeverGray,
                            modifier = Modifier.clickable(enabled = isPurchasing.not()) {
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

            // Floating Top-Right Glass Close Button
            Box(
                modifier = Modifier
                    .align(TopEnd)
                    .statusBarsPadding()
                    .padding(end = Dp16, top = Dp8)
                    .size(Dp32)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MeverBlack.copy(alpha = 0.55f),
                                MeverBlack.copy(alpha = 0.35f)
                            )
                        )
                    )
                    .border(
                        width = Dp1,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MeverWhite.copy(alpha = 0.35f),
                                MeverWhite.copy(alpha = 0.08f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .clickable { navigator.navigateBack() },
                contentAlignment = Center
            ) {
                Icon(
                    modifier = Modifier.size(Dp24),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                    contentDescription = "Close",
                    tint = MeverWhite.copy(alpha = 0.9f)
                )
            }
        }
    }
}

private data class PaywallBenefit(
    val iconRes: Int,
    val titleRes: Int,
    val descRes: Int
)

@Composable
private fun PaywallBenefitsSection() {
    val benefits = listOf(
        PaywallBenefit(
            iconRes = R.drawable.ic_no_ads,
            titleRes = R.string.feature_no_ads,
            descRes = R.string.feature_no_ads_desc
        ),
        PaywallBenefit(
            iconRes = R.drawable.ic_quick_download_notif,
            titleRes = R.string.quick_download_notif_title,
            descRes = R.string.feature_quick_download_desc
        ),
        PaywallBenefit(
            iconRes = R.drawable.ic_art_gallery,
            titleRes = R.string.feature_doodle_theme,
            descRes = R.string.feature_doodle_theme_desc
        ),
        PaywallBenefit(
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
                color = colors.alwaysPurple.copy(alpha = 0.25f),
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
                                color = colors.alwaysPurple.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(Dp10)
                            ),
                        contentAlignment = Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(benefit.iconRes),
                            contentDescription = stringResource(benefit.titleRes),
                            tint = colors.alwaysPurple,
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
                        tint = colors.alwaysPurple,
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
}