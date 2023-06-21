package com.ecloud.apps.watermeterreader.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ecloud.apps.watermeterreader.bottombar.BottomBarDestination
import com.ecloud.apps.watermeterreader.core.designsystem.icon.Icon
import com.ramcosta.composedestinations.spec.DestinationSpec

@Composable
fun BottomBar(
    destinations: List<BottomBarDestination>,
    onNavigateToDestination: (BottomBarDestination) -> Unit,
    currentDestination: DestinationSpec<*>?,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = { onNavigateToDestination(destination) },
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