package com.dapascript.mever.core.common.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment.Companion.Center
import androidx.compose.ui.text.style.LineHeightStyle.Trim.Companion.None
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp10
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp12
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp14
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp16
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp20
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp24
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp28
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp40
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.DeviceType.TABLET

private val interphases = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold)
)

private const val HEADLINE_LINE_HEIGHT = 1.2
private const val BODY_LINE_HEIGHT = 1.4
private const val LABEL_LINE_HEIGHT = 1.2
private const val AMOUNT_LINE_HEIGHT = 1.2
private const val AVATAR_LINE_HEIGHT = 1.2
private const val UNDERLINE_LINE_HEIGHT = 1.2

data class MeverTypography(
    val deviceType: DeviceType = PHONE,
    val h1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = adjustFontSize(deviceType, Sp28),
        lineHeight = Sp28 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp24),
        lineHeight = Sp24 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val h3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp18),
        lineHeight = Sp18 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val h4: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp16),
        lineHeight = Sp16 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val h5: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp14 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val h6: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp12),
        lineHeight = Sp12 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val body1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = adjustFontSize(deviceType, Sp16),
        lineHeight = Sp16 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val bodyBold1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp16),
        lineHeight = Sp16 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp14 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val bodyBold2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp14 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val body3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = adjustFontSize(deviceType, Sp12),
        lineHeight = Sp12 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val bodyBold3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp12),
        lineHeight = Sp12 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val label1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp14 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val labelBold1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp14 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val label2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = adjustFontSize(deviceType, Sp12),
        lineHeight = Sp12 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val labelBold2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp12),
        lineHeight = Sp12 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val label3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = adjustFontSize(deviceType, Sp10),
        lineHeight = Sp10 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val labelBold3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp10),
        lineHeight = Sp10 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val amount1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = adjustFontSize(deviceType, Sp40),
        lineHeight = Sp40 * AMOUNT_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val amount2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = adjustFontSize(deviceType, Sp28),
        lineHeight = Sp28 * AMOUNT_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val amount3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = adjustFontSize(deviceType, Sp20),
        lineHeight = Sp20 * AMOUNT_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val avatar1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp24),
        lineHeight = Sp18 * AVATAR_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val numbers1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = adjustFontSize(deviceType, Sp16),
        lineHeight = Sp16 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val underline1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Medium,
        fontSize = adjustFontSize(deviceType, Sp16),
        lineHeight = Sp16 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val underlineBold1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp16),
        lineHeight = Sp16 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val underline2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Medium,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp14 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val underlineBold2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp14 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val underline3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Medium,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp12 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val underlineBold3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp12 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val underline4: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Medium,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp10 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val underline4Regular: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = adjustFontSize(deviceType, Sp14),
        lineHeight = Sp10 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    )
)

internal val LocalTypography = staticCompositionLocalOf { MeverTypography() }

private fun adjustFontSize(
    deviceType: DeviceType,
    size: TextUnit
) = when (deviceType) {
    PHONE -> size
    TABLET -> size.value.let { (it + 2).sp }
    else -> size.value.let { (it + 4).sp }
}