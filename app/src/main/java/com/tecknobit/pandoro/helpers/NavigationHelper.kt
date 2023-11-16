package com.tecknobit.pandoro.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.tecknobit.pandoro.ui.activities.MainActivity.Companion.activeScreen
import com.tecknobit.pandoro.ui.screens.Screen
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Notes
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Overview
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Profile
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects

class NavigationHelper {

    data class NavigationItem(
        val title: Screen.ScreenType,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector
    )

    @Composable
    fun getNavigationBar() : @Composable () -> Unit {
        return {
            val items = listOf(
                NavigationItem(
                    title = Projects,
                    selectedIcon = Icons.Filled.List,
                    unselectedIcon = Icons.Outlined.List
                ),
                NavigationItem(
                    title = Notes,
                    selectedIcon = Icons.Filled.Notes,
                    unselectedIcon = Icons.Outlined.Notes
                ),
                NavigationItem(
                    title = Overview,
                    selectedIcon = Icons.Filled.TableChart,
                    unselectedIcon = Icons.Outlined.TableChart
                )
            )
            var selected by rememberSaveable { mutableIntStateOf(0) }
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selected == index && activeScreen.value != Profile,
                        onClick = {
                            selected = index
                            activeScreen.value = item.title
                        },
                        label = {
                            Text(
                                text = stringResource(item.title.getStringResourceTitle(item.title))
                            )
                        },
                        alwaysShowLabel = false,
                        icon = {
                            Icon(
                                imageVector =
                                if (selected == index)
                                    item.selectedIcon
                                else
                                    item.unselectedIcon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }

}