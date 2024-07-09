package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandorocore.records.Changelog
import com.tecknobit.pandorocore.records.Group
import java.io.File

class ProfileScreenViewModel(
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    fun changeProfilePic(
        imagePath: String,
        onSuccess: (JsonHelper) -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.changeProfilePic(
                    profilePic = File(imagePath)
                )
            },
            onSuccess = { response ->
                onSuccess.invoke(response)
            },
            onFailure = { showSnack(it) }
        )
    }

    fun changeEmail(
        newEmail: String,
        onSuccess: () -> Unit,
        onFailure: (JsonHelper) -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.changeEmail(
                    newEmail = newEmail
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { onFailure.invoke(it) }
        )
    }

    fun changePassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (JsonHelper) -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.changePassword(
                    newPassword = newPassword
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { onFailure.invoke(it) }
        )
    }

    fun changeLanguage(
        newLanguage: String,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.changeLanguage(
                    newLanguage = newLanguage
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    fun deleteAccount() {
        requester.sendRequest(
            request = {
                requester.deleteAccount()
            },
            onSuccess = { localAuthHelper.logout() },
            onFailure = { showSnack(it) }
        )
    }

    fun declineInvitation(
        group: Group,
        changelog: Changelog,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.declineInvitation(
                    groupId = group.id,
                    changelogId = changelog.id
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    fun acceptInvitation(
        group: Group,
        changelog: Changelog,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.acceptInvitation(
                    groupId = group.id,
                    changelogId = changelog.id
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    fun readChangelog(
        changelog: Changelog
    ) {
        requester.sendRequest(
            request = {
                requester.readChangelog(
                    changelogId = changelog.id,
                )
            },
            onSuccess = { },
            onFailure = { showSnack(it) }
        )
    }

    fun deleteChangelog(
        changelog: Changelog
    ) {
        requester.sendRequest(
            request = {
                requester.deleteChangelog(
                    groupId = if (changelog.group != null)
                        changelog.group.id
                    else
                        null,
                    changelogId = changelog.id,
                )
            },
            onSuccess = { },
            onFailure = { showSnack(it) }
        )
    }

}