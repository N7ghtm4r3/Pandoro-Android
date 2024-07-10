package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandorocore.records.Changelog
import com.tecknobit.pandorocore.records.Group
import java.io.File

/**
 * The **ProfileScreenViewModel** class is the support class used by the [ProfileScreenViewModel]
 * to manage the account operations of the user
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class ProfileScreenViewModel(
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * Function to execute the profile pic change
     *
     * @param imagePath: the path of the image to set
     * @param onSuccess: the action to execute whether the request has been successful
     */
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

    /**
     * Function to execute the email change
     *
     * @param newEmail: the new email of the user
     * @param onSuccess: the action to execute whether the request has been successful
     * @param onFailure: the action to execute whether the request has been failed
     */
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

    /**
     * Function to execute the password change
     *
     * @param newPassword: the new password of the user
     * @param onSuccess: the action to execute whether the request has been successful
     * @param onFailure: the action to execute whether the request has been failed
     */
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

    /**
     * Function to execute the language change
     *
     * @param newLanguage: the new language of the user
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Function to execute the account deletion
     *
     * No-any params required
     */
    fun deleteAccount() {
        requester.sendRequest(
            request = {
                requester.deleteAccount()
            },
            onSuccess = { localAuthHelper.logout() },
            onFailure = { showSnack(it) }
        )
    }

    /**
     * Function to decline a group invitation
     *
     * @param group: the group where the user has been invited
     * @param changelog: the related changelog of the invitation
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Function to accept a group invitation
     *
     * @param group: the group where the user has been invited
     * @param changelog: the related changelog of the invitation
     * @param onSuccess: the action to execute if the request has been successful
     */
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

    /**
     * Function to read a changelog
     *
     * @param changelog: the changelog to read
     */
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

    /**
     * Function to delete a changelog
     *
     * @param changelog: the changelog to delete
     */
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