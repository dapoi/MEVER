package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints.Companion.fixed
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.attr.MeverTabsAttr.SubComposeID.INDICATOR
import com.dapascript.mever.core.common.ui.attr.MeverTabsAttr.SubComposeID.ITEM
import com.dapascript.mever.core.common.ui.attr.MeverTabsAttr.SubComposeID.PRE_CALCULATE_ITEM
import com.dapascript.mever.core.common.ui.attr.MeverTabsAttr.TabPosition
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite

@Composable
fun MeverTabs(
    items: List<String>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    containerColor: Color = colorScheme.onSecondaryContainer,
    indicatorColor: Color = colorScheme.background,
    onChangeTab: (Int) -> Unit
) = CustomTab(
    modifier = modifier,
    selectedTabPosition = pagerState.currentPage,
    containerColor = containerColor,
    indicatorColor = indicatorColor
) {
    items.forEachIndexed { index, title ->
        Text(
            text = title,
            style = typography.body2,
            color = if (pagerState.currentPage == index) colorScheme.primary else colorScheme.onPrimary,
            modifier = Modifier
                .wrapContentWidth(CenterHorizontally)
                .padding(horizontal = Dp16, vertical = Dp8)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onChangeTab(index) }
        )
    }
}

@Composable
private fun CustomTab(
    modifier: Modifier = Modifier,
    containerColor: Color = MeverWhite,
    indicatorColor: Color = colorScheme.background,
    containerShape: Shape = CircleShape,
    indicatorShape: Shape = CircleShape,
    paddingValues: PaddingValues = PaddingValues(Dp4),
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 250, easing = FastOutSlowInEasing),
    fixedSize: Boolean = true,
    selectedTabPosition: Int = 0,
    tabItem: @Composable () -> Unit
) {
    Surface(
        color = containerColor,
        shape = containerShape
    ) {
        SubcomposeLayout(
            modifier = modifier
                .padding(paddingValues)
                .selectableGroup()
        ) { constraints ->
            val tabMeasurable: List<Placeable> = subcompose(
                slotId = PRE_CALCULATE_ITEM,
                content = tabItem
            ).map { it.measure(constraints) }
            val itemsCount = tabMeasurable.size
            val maxItemWidth = tabMeasurable.maxOf { it.width }
            val maxItemHeight = tabMeasurable.maxOf { it.height }
            val tabPlaceable = subcompose(slotId = ITEM, content = tabItem).map {
                val c = if (fixedSize) constraints.copy(
                    minWidth = maxItemWidth,
                    maxWidth = maxItemWidth,
                    minHeight = maxItemHeight
                ) else constraints
                it.measure(c)
            }
            val tabPositions = tabPlaceable.mapIndexed { index, placeable ->
                val itemWidth = if (fixedSize) maxItemWidth else placeable.width
                val x = if (fixedSize) maxItemWidth * index
                else {
                    val leftTabWith = tabPlaceable.take(index).sumOf { it.width }
                    leftTabWith
                }
                TabPosition(left = x.toDp(), width = itemWidth.toDp())
            }
            val tabRowWidth = if (fixedSize) maxItemWidth * itemsCount
            else tabPlaceable.sumOf { it.width }

            layout(width = tabRowWidth, height = maxItemHeight) {
                subcompose(INDICATOR) {
                    Box(
                        Modifier
                            .tabIndicator(
                                tabPosition = tabPositions[selectedTabPosition],
                                animationSpec = animationSpec
                            )
                            .fillMaxWidth()
                            .height(maxItemHeight.toDp())
                            .background(color = indicatorColor, shape = indicatorShape)
                    )
                }.forEach {
                    it.measure(fixed(width = tabRowWidth, height = maxItemHeight)).placeRelative(0, 0)
                }

                tabPlaceable.forEachIndexed { index, placeable ->
                    val x = if (fixedSize) maxItemWidth * index
                    else {
                        val leftTabWith = tabPlaceable.take(index).sumOf { it.width }
                        leftTabWith
                    }
                    placeable.placeRelative(x, 0)
                }
            }
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
private fun Modifier.tabIndicator(
    tabPosition: TabPosition,
    animationSpec: AnimationSpec<Dp>
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = tabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabPosition.width,
        animationSpec = animationSpec,
        label = "currentTabWidth"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = tabPosition.left,
        animationSpec = animationSpec,
        label = "indicatorOffset"
    )
    fillMaxWidth()
        .wrapContentSize(BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
        .fillMaxHeight()
}