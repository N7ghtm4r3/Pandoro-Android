package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_group_description
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_group_name
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_members_list
import com.tecknobit.pandoro.R.string.you_must_insert_one_member_at_least
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.checkMembersValidity
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isGroupDescriptionValid
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isGroupNameValid
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.users.GroupMember

/**
 * The **GroupDialogsViewModel** class is the support class used by the [GroupDialogsViewModel]
 * to manage a group
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class GroupDialogsViewModel(
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **name** -> the name of the group
     */
    lateinit var name: MutableState<String>

    /**
     * **description** -> the description of the group
     */
    lateinit var description: MutableState<String>

    /**
     * Function to execute the request to create a new group
     *
     * @param members: the list of the members to add to the group
     * @param showCreateGroup: whether show the dialog for the creation of the group
     */
    fun createGroup(
        members: List<String>,
        showCreateGroup: MutableState<Boolean>
    ) {
        if (isGroupNameValid(name.value)) {
            if (isGroupDescriptionValid(description.value)) {
                if (members.isNotEmpty()) {
                    if (checkMembersValidity(members)) {
                        requester.sendRequest(
                            request = {
                                requester.createGroup(
                                    name = name.value,
                                    groupDescription = description.value,
                                    members = members
                                )
                            },
                            onSuccess = {
                                showCreateGroup.value = false
                                name.value = ""
                                description.value = ""
                            },
                            onFailure = { showSnack(it) }
                        )
                    } else
                        showSnack(you_must_insert_a_correct_members_list)
                } else
                    showSnack(you_must_insert_one_member_at_least)
            } else
                showSnack(you_must_insert_a_correct_group_description)
        } else
            showSnack(you_must_insert_a_correct_group_name)
    }

    /**
     * Function to execute the request to remove a member from a group
     *
     * @param show: whether show the dialog for the member removal
     * @param group: the group from remove that member
     * @param member: the member to remove
     */
    fun removeMember(
        show: MutableState<Boolean>,
        group: Group,
        member: GroupMember
    ) {
        requester.sendRequest(
            request = {
                requester.removeMember(
                    groupId = group.id,
                    memberId = member.id
                )
            },
            onSuccess = { show.value = false },
            onFailure = { showSnack(it) }
        )
    }

    /**
     * Function to execute the request to leave from a group
     *
     * @param group: the group from leave
     * @param nextAdmin: the next admin choose by the admin is leaving
     * @param onSuccess: the action to execute whether the request has been successful
     */
    fun leaveFromGroup(
        group: Group,
        nextAdmin: GroupMember?,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.leaveGroup(
                    groupId = group.id,
                    nextAdminId = nextAdmin?.id
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    /**
     * Function to execute the request to delete a group
     *
     * @param show: whether show the dialog for the group deletion
     * @param group: the group to delete
     */
    fun deleteGroup(
        show: MutableState<Boolean>,
        group: Group
    ) {
        requester.sendRequest(
            request = {
                requester.deleteGroup(
                    groupId = group.id,
                )
            },
            onSuccess = { show.value = false },
            onFailure = { showSnack(it) }
        )
    }

}