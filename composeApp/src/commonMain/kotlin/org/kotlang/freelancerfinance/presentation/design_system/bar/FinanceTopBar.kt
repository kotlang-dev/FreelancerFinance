package org.kotlang.freelancerfinance.presentation.design_system.bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * A reusable TopAppBar for the Finance app.
 *
 * @param title The optional title string to be displayed.
 * @param modifier The modifier to be applied to the TopAppBar.
 * @param titleTextStyle The style to be applied to the title text.
 * @param scrollBehavior A TopAppBarScrollBehavior for scroll effects.
 * @param onNavigateBack If not null, a back arrow navigation icon is displayed and this lambda is invoked when clicked.
 * @param navigationIconResId The drawable resource for the navigation icon. Defaults to the back arrow.
 * @param actions The composable actions to be displayed at the end of the TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateBack: (() -> Unit)? = null,
    navigationIconResId: DrawableResource = Res.drawable.ic_arrow_back,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (title != null) {
                Text(
                    text = title,
                    style = titleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(navigationIconResId),
                        contentDescription = "Navigate Back"
                    )
                }
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}