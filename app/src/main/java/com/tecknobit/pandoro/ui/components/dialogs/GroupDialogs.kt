package com.tecknobit.pandoro.ui.components.dialogs

/*
/**
 * The **GroupDialogs** class is useful to create the groups dialogs
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroDialog
 */
class GroupDialogs : PandoroDialog() {

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
                                    requester!!.execCreateGroup(
                                        name = name,
                                        groupDescription = description,
                                        members = members
                                    )
                                    if(requester!!.successResponse()) {
                                        showCreateGroup.value = false
                                        name = ""
                                        description = ""
                                    } else
                                        showSnack(requester!!.errorMessage())
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
                        .height(height = 60.dp),
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
                        .height(height = 60.dp),
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
                requester!!.execRemoveMember(
                    groupId = group.id,
                    memberId = member.id
                )
                if(requester!!.successResponse())
                    show.value = false
                else
                    showSnack(requester!!.errorMessage())
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
                        modifier = Modifier.verticalScroll(rememberScrollState())
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
                                            modifier = Modifier.size(15.dp),
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
        requester!!.execLeaveGroup(
            groupId = group.id,
            nextAdminId = nextAdmin?.id
        )
        if(requester!!.successResponse()) {
            currentGroup.value = null
            if(currentProject.value != null)
                ContextCompat.startActivity(context, Intent(context, ProjectActivity::class.java), null)
            else {
                activeScreen.value = Profile
                ContextCompat.startActivity(context, Intent(context, MainActivity::class.java), null)
            }
            show.value = false
        } else
            showSnack(requester!!.errorMessage())
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
                requester!!.execDeleteGroup(group.id)
                if(requester!!.successResponse())
                    show.value = false
                else
                    showSnack(requester!!.errorMessage())
            }
        )
    }

}*/