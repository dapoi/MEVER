package com.dapascript.mever.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.dapascript.mever.core.common.component.MeverTextField
import com.dapascript.mever.core.common.ui.BaseScreen
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32

@Composable
fun HomeRoute() {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    val webDomain = remember { mutableStateOf(TextFieldValue()) }

    BaseScreen(
        isHome = true,
        listMenuAction = mapOf(
            R.drawable.ic_notification to "Notification",
            R.drawable.ic_setting to "Setting"
        )
    ) {
        Column(
            modifier = Modifier.padding(top = Dp32),
            horizontalAlignment = CenterHorizontally
        ) {
            MeverTextField(webDomainValue = webDomain.value) {
                webDomain.value = it
            }
        }
    }
}