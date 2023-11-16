package com.tecknobit.pandoro.ui.components.dialogs

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_group_description
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_group_name
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_members_list
import com.tecknobit.pandoro.R.string.you_must_insert_one_member_at_least
import com.tecknobit.pandoro.helpers.checkMembersValidity
import com.tecknobit.pandoro.helpers.isEmailValid
import com.tecknobit.pandoro.helpers.isGroupDescriptionValid
import com.tecknobit.pandoro.helpers.isGroupNameValid
import com.tecknobit.pandoro.toImportFromLibrary.Group
import com.tecknobit.pandoro.ui.components.PandoroAlertDialog
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.showCreateGroup
import com.tecknobit.pandoro.ui.theme.ErrorLight
import layouts.components.PandoroTextField

class GroupDialogs : PandoroDialog() {

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun CreateGroup() {
        val members = mutableStateListOf("")
        var name by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        CreatePandoroDialog(
            show = showCreateGroup,
            title = stringResource(R.string.create_a_new_group),
            confirmText = stringResource(R.string.create),
            requestLogic = {
                if (isGroupNameValid(name)) {
                    if (isGroupDescriptionValid(description)) {
                        if (members.isNotEmpty()) {
                            if (checkMembersValidity(members)) {
                                // TODO: MAKE REQUEST THEN
                                showCreateGroup.value = false
                            } else
                                showSnack(you_must_insert_a_correct_members_list)
                        } else
                            showSnack(you_must_insert_one_member_at_least)
                    } else
                        showSnack(you_must_insert_a_correct_group_description)
                } else
                    showSnack(you_must_insert_a_correct_group_name)
            },
        ) {
            Text(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp
                ),
                text = stringResource(R.string.details_of_the_group),
                fontSize = 22.sp
            )
            PandoroTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(height = 55.dp),
                textFieldModifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.name),
                value = name,
                isError = !isGroupNameValid(name),
                onValueChange = {
                    name = it
                }
            )
            PandoroTextField(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(height = 55.dp),
                textFieldModifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.description),
                value = description,
                isError = !isGroupDescriptionValid(description),
                onValueChange = {
                    description = it
                }
            )
            Text(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp
                ),
                text = stringResource(R.string.members_of_the_group),
                fontSize = 22.sp
            )
            CreateMembersSection(
                members = members
            )
        }
    }

    /**
     * Function to show the members section
     *
     * @param members: the list of the members
     */
// TODO: PACK IN THE LIBRARY
    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CreateMembersSection(
        members: SnapshotStateList<String>
    ) {
        LazyColumn {
            stickyHeader {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            top = 5.dp,
                            bottom = 5.dp
                        )
                ) {
                    FloatingActionButton(
                        modifier = Modifier.size(40.dp),
                        onClick = { members.add("") },
                        content = { Icon(Icons.Filled.Add, null) }
                    )
                }
            }
            items(members.size) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 10.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val member = mutableStateOf(members[index])
                    PandoroTextField(
                        modifier = Modifier
                            .padding(
                                bottom = 5.dp,
                                start = 15.dp
                            )
                            .weight(8f),
                        label = stringResource(R.string.email_of_the_member),
                        isError = !isEmailValid(member.value),
                        onValueChange = {
                            if (it.isNotEmpty()) {
                                member.value = it
                                members.removeAt(index)
                                members.add(index, it)
                            }
                        },
                        value = member.value,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    IconButton(
                        modifier = Modifier.padding(start = 10.dp),
                        onClick = { members.removeAt(index) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = ErrorLight
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun RemoveUser(
        show: MutableState<Boolean>,
        group: Group,
        member: Group.Member
    ) {
        PandoroAlertDialog(
            show = show,
            title = R.string.remove_the_user_from,
            extraTitle = group.name,
            text = R.string.remove_user_text,
            requestLogic = {
                // TODO: REQUEST THEN
                show.value = false
            }
        )
    }

    @Composable
    fun LeaveGroup(
        show: MutableState<Boolean>,
        group: Group,
    ) {
        PandoroAlertDialog(
            show = show,
            title = R.string.leave_group,
            extraTitle = group.name,
            text = R.string.leave_group_text,
            requestLogic = {
                // TODO: REQUEST THEN
                show.value = false
            }
        )
    }

    @Composable
    fun DeleteGroup(
        show: MutableState<Boolean>,
        group: Group,
    ) {
        PandoroAlertDialog(
            show = show,
            title = R.string.delete_group,
            extraTitle = group.name,
            text = R.string.delete_group_text,
            requestLogic = {
                // TODO: REQUEST THEN
                show.value = false
            }
        )
    }

}