package com.ecloud.apps.watermeterreader.ui

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ecloud.apps.watermeterreader.bottombar.BottomBarDestination
import com.ecloud.apps.watermeterreader.core.designsystem.icon.Icon
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@Composable
fun BottomBar(
    selectedNavigation: DestinationSpec<*>?,
    onNavigationSelected: (DirectionDestinationSpec) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        BottomBarDestination.values().forEach { destination ->
            NavigationBarItem(
                selected = selectedNavigation == destination.direction,
                onClick = { onNavigationSelected(destination.direction) },
                icon = {
                    when (destination.icon) {
                        is Icon.DrawableResourceIcon -> {
                            Icon(
                                painter = painterResource(destination.icon.id),
                                contentDescription = stringResource(destination.title)
                            )
                        }

                        is Icon.ImageVectorIcon -> {
                            Icon(
                                imageVector = destination.icon.imageVectorIcon,
                                contentDescription = stringResource(destination.title)
                            )
                        }
                    }
                },
                label = {
                    Text(text = stringResource(destination.title))
                }
            )
        }
    }
}