package com.tecknobit.pandoro.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.TableChart
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
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.screens.Screen
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Notes
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Overview
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Profile
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects

/**
 * The **NavigationHelper** class is useful to manage the navigation of the screens in Pandoro's
 * application
 * @author N7ghtm4r3 - Tecknobit
 */
class NavigationHelper {

    /**
     * The **NavigationItem** class is useful to create an item for the navigation
     * @author N7ghtm4r3 - Tecknobit
     */
    data class NavigationItem(
        val title: Screen.ScreenType,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector
    )

    /**
     * Function to create a navigation bar to navigate in the Pandoro's application
     *
     * No any params required
     *
     * @return navigation bar as [Composable] function
     */
    @Composable
    fun getNavigationBar() : @Composable () -> Unit {
        return {
            val items = listOf(
                NavigationItem(
                    title = Projects,
                    selectedIcon = Icons.AutoMirrored.Filled.List,
                    unselectedIcon = Icons.AutoMirrored.Outlined.List
                ),
                NavigationItem(
                    title = Notes,
                    selectedIcon = Icons.AutoMirrored.Filled.Notes,
                    unselectedIcon = Icons.AutoMirrored.Outlined.Notes
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