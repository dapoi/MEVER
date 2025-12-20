package com.dapascript.mever.core.common.ui.attr

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverActionButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

object MeverTopBarAttr {

    data class ActionMenu(
        val icon: Int,
        val nameIcon: String,
        val showBadge: Boolean = false,
        val onClickActionMenu: (String) -> Unit
    )

    data class TopBarArgs(
        val actionMenus: List<ActionMenu> = emptyList(),
        val iconBack: Int? = null,
        val title: String? = null,
        val topBarColor: Color? = null,
        val titleColor: Color? = null,
        val iconBackColor: Color? = null,
        val actionMenusColor: Color? = null,
        val onClickBack: (() -> Unit)? = null
    )

    @Composable
    fun navigationIcon(
        icon: Int? = null,
        onClickBack: (() -> Unit)?
    ): @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .then(icon?.let { Modifier } ?: Modifier.clip(CircleShape))
                .onCustomClick(enabled = onClickBack != null) { onClickBack?.invoke() },
            contentAlignment = Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(icon ?: R.drawable.ic_back),
                contentDescription = "Navigation Icon"
            )
        }
    }

    @Composable
    fun actions(actionMenus: List<ActionMenu>): @Composable (RowScope.() -> Unit) = {
        Row(horizontalArrangement = spacedBy(Dp16)) {
            actionMenus.forEach { (resource, name, showBadge, onClickAction) ->
                MeverActionButton(
                    resource = resource,
                    showBadge = showBadge
                ) { onClickAction(name) }
            }
        }
    }

    @Composable
    fun title(screenName: String?): @Composable () -> Unit = {
        AnimatedVisibility(
            visible = screenName.isNullOrEmpty().not(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = Dp8),
                text = screenName.orEmpty(),
                maxLines = 2,
                overflow = Ellipsis,
                style = typography.h3.copy(fontWeight = Medium)
            )
        }
    }
}