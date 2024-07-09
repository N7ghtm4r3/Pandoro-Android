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

class GroupDialogsViewModel(
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    lateinit var name: MutableState<String>

    lateinit var description: MutableState<String>

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