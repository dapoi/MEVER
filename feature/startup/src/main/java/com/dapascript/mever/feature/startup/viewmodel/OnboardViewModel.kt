package com.dapascript.mever.feature.startup.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.feature.startup.screen.attr.OnboardScreenAttr.OnboardPage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class OnboardViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val meverDataStore: MeverDataStore
) : BaseViewModel() {

    val pages by lazy {
        listOf(
            OnboardPage(
                image = R.drawable.ilustration_onboard_1,
                subtitle = context.getString(R.string.onboard_1_subtitle),
                title = context.getString(R.string.onboard_1),
                highlightedText = context.getString(R.string.multiple),
                description = context.getString(R.string.onboard_1_desc)
            ),
            OnboardPage(
                image = R.drawable.ilustration_onboard_2,
                subtitle = context.getString(R.string.onboard_2_subtitle),
                title = context.getString(R.string.onboard_2),
                highlightedText = context.getString(R.string.fast),
                description = context.getString(R.string.onboard_2_desc)
            ),
            OnboardPage(
                image = R.drawable.ilustration_onboard_3,
                subtitle = context.getString(R.string.onboard_3_subtitle),
                title = context.getString(R.string.onboard_3),
                highlightedText = context.getString(R.string.organized),
                description = context.getString(R.string.onboard_3_desc)
            )
        )
    }

    fun setIsOnboarded(isOnboarded: Boolean) = viewModelScope.launch(IO) {
        meverDataStore.setIsOnboarded(isOnboarded)
    }
}