package com.tecknobit.pandoro.ui.components.dialogs

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.activities.session.MainActivity
import com.tecknobit.pandoro.ui.activities.session.ProjectActivity
import com.tecknobit.pandoro.ui.components.PandoroAlertDialog
import com.tecknobit.pandoro.ui.components.PandoroTextField
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.showCreateGroup
import com.tecknobit.pandoro.ui.screens.Screen.Companion.currentGroup
import com.tecknobit.pandoro.ui.screens.Screen.Companion.currentProject
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Profile
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandoro.ui.viewmodels.GroupDialogsViewModel
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isGroupDescriptionValid
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isGroupNameValid
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.users.GroupMember
import com.tecknobit.pandorocore.records.users.GroupMember.InvitationStatus.PENDING
import com.tecknobit.pandorocore.records.users.GroupMember.Role.ADMIN

/**
 * The **GroupDialogs** class is useful to create the groups dialogs
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroDialog
 */
// TODO: TO COMMENT
class GroupDialogs : PandoroDialog() {

    private val viewModel = GroupDialogsViewModel(
        snackbarHostState = snackbarHostState
    )

    /**
     * Function to create a Pandoro's custom dialog to create a new [Group]
     *
     * **No-any params required
     */
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun CreateGroup() {
        if(showCreateGroup.value) {
            val members = mutableStateListOf("")
            viewModel.name = remember { mutableStateOf("") }
            viewModel.description = remember { mutableStateOf("") }
            CreatePandoroDialog(
                show = showCreateGroup,
                title = stringResource(R.string.create_a_new_group),
                confirmText = stringResource(R.string.create),
                requestLogic = {
                    viewModel.createGroup(
                        members = members,
                        showCreateGroup = showCreateGroup
                    )
                },
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    text = stringResource(R.string.details_of_the_group),
                    fontSize = 22.sp,
                    color = PrimaryLight
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(
                            all = 10.dp
                        )
                        .fillMaxWidth()
                        .height(60.dp),
                    textFieldModifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(R.string.name),
                    value = viewModel.name,
                    isError = !isGroupNameValid(viewModel.name.value)
                )
                PandoroTextField(
                    modifier = Modifier
                        .padding(
                            all = 10.dp
                        )
                        .fillMaxWidth()
                        .height(60.dp),
                    textFieldModifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(R.string.description),
                    value = viewModel.description,
                    isError = !isGroupDescriptionValid(viewModel.description.value)
                )
                Text(
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                    text = stringResource(R.string.members_of_the_group),
                    fontSize = 22.sp,
                    color = PrimaryLight
                )
                CreateMembersSection(
                    members = members
                )
            }
        }
    }

    /**
     * Function to show the members section
     *
     * @param members: the list of the members
     */
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
                        modifier = Modifier
                            .size(40.dp),
                        onClick = { members.add("") },
                        content = { 
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null
                            )
                        }
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
                        value = member,
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

    /**
     * Function to create a Pandoro's custom dialog to remove a member from a [Group]
     *
     * @param show: whether show the dialog
     * @param group: the group where remove the user
     * @param member: the member to remove
     */
    @Composable
    fun RemoveMember(
        show: MutableState<Boolean>,
        group: Group,
        member: GroupMember
    ) {
        PandoroAlertDialog(
            show = show,
            title = R.string.remove_the_user_from,
            extraTitle = group.name,
            text = R.string.remove_user_text,
            requestLogic = {
                viewModel.removeMember(
                    show = show,
                    group = group,
                    member = member
                )
            }
        )
    }

    /**
     * Function to create a Pandoro's custom dialog to leave from a [Group]
     *
     * @param show: whether show the dialog
     * @param group: the group from leave
     */
    @Composable
    fun LeaveGroup(
        show: MutableState<Boolean>,
        group: Group,
    ) {
        val showAdminChoseDialog = remember { mutableStateOf(false) }
        val members = group.members
        PandoroAlertDialog(
            show = show,
            title = R.string.leave_group,
            extraTitle = group.name,
            text = R.string.leave_group_text,
            requestLogic = {
                if(group.isUserAdmin(user)) {
                    var pendingUsers = 0
                    members.forEach { member ->
                        if(member.invitationStatus == PENDING)
                            pendingUsers++;
                    }
                    if(((members.size - 1) - pendingUsers) != 0) {
                        var hasOtherAdmins = false
                        for(member in members) {
                            if(member.role == ADMIN && !member.isLoggedUser(user)) {
                                hasOtherAdmins = true
                                break
                            }
                        }
                        if(hasOtherAdmins)
                            leaveFromGroup(show, group)
                        else
                            showAdminChoseDialog.value = true
                    } else
                        leaveFromGroup(show, group)
                } else
                    leaveFromGroup(show, group)
            }
        )
        if(showAdminChoseDialog.value) {
            var nextAdmin by remember { mutableStateOf<GroupMember?>(null) }
            for(member in members) {
                if(!member.isLoggedUser(user) && member.invitationStatus != PENDING) {
                    nextAdmin = member
                    break
                }
            }
            AlertDialog(
                modifier = Modifier.height(330.dp),
                onDismissRequest = { showAdminChoseDialog.value = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PrimaryLight
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.choose_the_next_admin)
                    )
                },
                text = {
                    Column (
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        members.forEach { member ->
                            if(!member.isLoggedUser(user) && member.invitationStatus != PENDING) {
                                ListItem(
                                    leadingContent = {
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                ImageRequest.Builder(context)
                                                    .data("${localAuthHelper.host}/${member.profilePic}")
                                                    .error(R.drawable.logo)
                                                    .crossfade(500)
                                                    .build()
                                            ),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                        )
                                    },
                                    headlineContent = {
                                        Text(
                                            text = member.completeName,
                                            fontSize = 18.sp
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = member.role.toString(),
                                            color = if (member.isAdmin) ErrorLight else PrimaryLight
                                        )
                                    },
                                    trailingContent = {
                                        RadioButton(
                                            modifier = Modifier
                                                .size(15.dp),
                                            selected = member == nextAdmin,
                                            onClick = { nextAdmin = member }
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            show.value = false
                            showAdminChoseDialog.value = false
                        },
                        content = {
                            Text(
                                text = stringResource(R.string.dismiss),
                            )
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            leaveFromGroup(show, group, nextAdmin)
                            showAdminChoseDialog.value = false
                        },
                        content = {
                            Text(
                                text = stringResource(R.string.confirm),
                            )
                        }
                    )
                }
            )
        }
    }

    /**
     * Function to execute the request to leave from a [Group]
     *
     * @param show: whether show the dialog
     * @param group: the group from leave
     * @param nextAdmin: the next member chosen as next admin
     */
    private fun leaveFromGroup(
        show: MutableState<Boolean>,
        group: Group,
        nextAdmin: GroupMember? = null
    ) {
        viewModel.leaveFromGroup(
            group = group,
            nextAdmin = nextAdmin,
            onSuccess = {
                // TODO: TO FIX BACK NAVIGATION 
                currentGroup.value = null
                if(currentProject.value != null)
                    ContextCompat.startActivity(context, Intent(context, ProjectActivity::class.java), null)
                else {
                    activeScreen.value = Profile
                    ContextCompat.startActivity(context, Intent(context, MainActivity::class.java), null)
                }
                show.value = false
            }
        )
    }

    /**
     * Function to create a Pandoro's custom dialog to delete a [Group]
     *
     * @param show: whether show the dialog
     * @param group: the group to delete
     */
    @Composable
    fun DeleteGroup(
        show: MutableState<Boolean>,
        group: Group
    ) {
        PandoroAlertDialog(
            show = show,
            title = R.string.delete_group,
            extraTitle = group.name,
            text = R.string.delete_group_text,
            requestLogic = {
                viewModel.deleteGroup(
                    show = show,
                    group = group
                )
            }
        )
    }

}