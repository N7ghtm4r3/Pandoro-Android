package com.tecknobit.pandoro.ui.activities.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableIntState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.session.MainActivity
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.changelogs
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.groups
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.projectsList
import com.tecknobit.pandoro.ui.screens.Screen.Companion.currentGroup
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Overview
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Profile
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects
import com.tecknobit.pandorocore.records.Changelog
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.Project

class MainActivityViewModel(
    snackbarHostState: SnackbarHostState
) : PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **unreadChangelogsNumber** -> the number of the changelogs yet to read
     */
    lateinit var unreadChangelogsNumber: MutableIntState

    fun refreshValues() {
        execRefreshingRoutine(
            currentContext = MainActivity::class.java,
            routine = {
                if(activeScreen.value == Projects || activeScreen.value == Overview
                    || currentGroup.value != null) {
                    requester.sendRequest(
                        request = { requester.getProjectsList() },
                        onSuccess = { response ->
                            projectsList.clear()
                            projectsList.addAll(
                                Project.getInstances(
                                    response.getJSONArray(RESPONSE_MESSAGE_KEY)
                                )
                            )
                        },
                        onFailure = { showSnack(it) }
                    )
                }
                if(activeScreen.value == Profile || activeScreen.value == Projects) {
                    requester.sendRequest(
                        request = { requester.getGroupsList() },
                        onSuccess = { response ->
                            groups.clear()
                            groups.addAll(
                                Group.getInstances(
                                    response.getJSONArray(RESPONSE_MESSAGE_KEY)
                                )
                            )
                        },
                        onFailure = { showSnack(it) }
                    )
                }
                requester.sendRequest(
                    request = { requester.getChangelogsList() },
                    onSuccess = { response ->
                        changelogs.clear()
                        changelogs.addAll(
                            Changelog.getInstances(
                                response.getJSONArray(RESPONSE_MESSAGE_KEY)
                            )
                        )
                        unreadChangelogsNumber.intValue = 0
                        changelogs.forEach { changelog ->
                            if(!changelog.isRed)
                                unreadChangelogsNumber.intValue++
                        }
                    },
                    onFailure = { showSnack(it) }
                )
            }
        )
    }

}