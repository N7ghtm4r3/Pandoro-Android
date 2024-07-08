package com.tecknobit.pandoro.ui.activities.session

/*

/**
 * The **GroupActivity** class is useful to create the activity to show the [Group] details
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see PandoroDataActivity
 * @see AndroidSingleItemManager
 */
class GroupActivity : PandoroDataActivity(), AndroidSingleItemManager {

    /**
     * **group** the group to show its details
     */
    lateinit var group: MutableState<Group>

    /**
     * **isAdmin** whether the user is an admin
     */
    private var isAdmin: Boolean = false

    /**
     * **isMaintainer** whether the user is a maintainer
     */
    private var isMaintainer: Boolean = false

    /**
     * On create method
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * If your ComponentActivity is annotated with {@link ContentView}, this will
     * call {@link #setContentView(int)} for you.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            group = remember { mutableStateOf(currentGroup.value!!) }
            val authorId = group.value.author.id
            isAdmin = group.value.isUserAdmin(user)
            isMaintainer = group.value.isUserMaintainer(user)
            refreshItem()
            PandoroTheme {
                Scaffold(
                    topBar = {
                        LargeTopAppBar(
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = PrimaryLight,
                                navigationIconContentColor = White,
                                titleContentColor = White,
                                actionIconContentColor = White
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = { onBackPressedDispatcher.onBackPressed() }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            title = {
                                Column (
                                    verticalArrangement = spacedBy(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = spacedBy(5.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier.alignBy(LastBaseline),
                                            text = group.value.name
                                        )
                                    }
                                    val author = group.value.author
                                    if (author != null) {
                                        Text(
                                            text = getString(string.author) + " ${author.completeName}",
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            },
                            actions = {
                                val leaveGroup = remember { mutableStateOf(false) }
                                IconButton(
                                    onClick = { leaveGroup.value = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        tint = ErrorLight
                                    )
                                }
                                groupDialogs.LeaveGroup(
                                    show = leaveGroup,
                                    group = group.value
                                )
                            },
                        )
                    },
                    floatingActionButton = {
                        if (isAdmin || isMaintainer) {
                            val addMembers = remember { mutableStateOf(false) }
                            val members = mutableStateListOf("")
                            FloatingActionButton(
                                onClick = { addMembers.value = true }
                            ) {
                                Icon(
                                    imageVector = Default.PersonAdd,
                                    contentDescription = null
                                )
                            }
                            pandoroModalSheet.PandoroModalSheet(
                                show = addMembers,
                                title = string.add_new_members,
                                content = {
                                    groupDialogs.CreateMembersSection(
                                        members = members
                                    )
                                    Button(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .height(50.dp)
                                            .fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        onClick = {
                                            if(checkMembersValidity(members)) {
                                                requester!!.execAddMembers(
                                                    groupId = group.value.id,
                                                    members = members.toList()
                                                )
                                                if(requester!!.successResponse())
                                                    addMembers.value = false
                                                else
                                                    pandoroModalSheet.showSnack(requester!!.errorMessage())
                                            } else
                                                pandoroModalSheet.showSnack(you_must_insert_a_correct_members_list)
                                        },
                                        content = {
                                            Text(
                                                text = stringResource(string.add),
                                                color = White,
                                                fontSize = 20.sp
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                ) {
                    val showMembersSection = remember { mutableStateOf(true) }
                    val showProjectsSection = remember { mutableStateOf(true) }
                    ShowData {
                        item {
                            ShowDescription(
                                description = group.value.description
                            )
                        }
                        item {
                            CreateHeader(
                                headerTitle = string.members,
                                show = showMembersSection
                            )
                            SpaceContent()
                        }
                        val modifier = Modifier
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(15.dp)
                            )
                        if (showMembersSection.value) {
                            items(
                                items = group.value.members,
                                key = { member ->
                                    member.id
                                }
                            ) { member ->
                                val isNotTheAuthor = member.id != authorId
                                val isLoggedUser = member.isLoggedUser(user)
                                val changeRole = remember { mutableStateOf(false) }
                                val isMemberPending = member.invitationStatus == PENDING
                                if((isMemberPending && isMaintainer) || !isMemberPending) {
                                    ListItem(
                                        modifier = if (((isAdmin || isMaintainer) && isNotTheAuthor)) {
                                            if ((isAdmin || !member.isAdmin) && !isLoggedUser
                                                && !isMemberPending) {
                                                modifier.clickable { changeRole.value = true }
                                            } else
                                                modifier
                                        } else
                                            modifier,
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
                                                    .size(55.dp)
                                                    .clip(CircleShape)
                                            )
                                        },
                                        headlineContent = {
                                            Text(
                                                text = member.completeName,
                                                fontSize = 20.sp
                                            )
                                        },
                                        supportingContent = {
                                            Text(
                                                text = if(isMemberPending)
                                                    PENDING.toString()
                                                else
                                                    member.role.toString(),
                                                color =
                                                if (member.isAdmin) ErrorLight
                                                else {
                                                    if(isMemberPending)
                                                        YELLOW_COLOR
                                                    else
                                                        PrimaryLight
                                                }
                                            )
                                            ChangeMemberRole(
                                                expanded = changeRole,
                                                member = member
                                            )
                                        },
                                        trailingContent = {
                                            if (((isAdmin || isMaintainer) && isNotTheAuthor)) {
                                                if (!isLoggedUser) {
                                                    val removeUser = remember { mutableStateOf(false) }
                                                    IconButton(
                                                        onClick = { removeUser.value = true }
                                                    ) {
                                                        Icon(
                                                            imageVector = Default.PersonRemove,
                                                            contentDescription = null
                                                        )
                                                    }
                                                    groupDialogs.RemoveMember(
                                                        show = removeUser,
                                                        group = group.value,
                                                        member = member
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        item {
                            var extraIcon: ExtraIcon? = null
                            if (isAdmin && user.projects.isNotEmpty()) {
                                val editProjects = remember { mutableStateOf(false) }
                                extraIcon = ExtraIcon(
                                    action = { editProjects.value = true },
                                    icon = Default.Edit
                                )
                                pandoroModalSheet.PandoroModalSheet(
                                    show = editProjects,
                                    title = string.edit_the_groups_projects,
                                    content = {
                                        val projects = mutableStateListOf<String>()
                                        group.value.projects.forEach { project ->
                                            projects.add(project.id)
                                        }
                                        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                                            items(
                                                items = user.projects,
                                                key = { project ->
                                                    project.id
                                                }
                                            ) { project ->
                                                var inserted by remember {
                                                    mutableStateOf(projects.contains(project.id))
                                                }
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Checkbox(
                                                        checked = inserted,
                                                        onCheckedChange = {
                                                            inserted = it
                                                            if (it)
                                                                projects.add(project.id)
                                                            else
                                                                projects.remove(project.id)
                                                        }
                                                    )
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text(
                                                        text = project.name,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        }
                                        Button(
                                            modifier = Modifier
                                                .padding(10.dp)
                                                .height(50.dp)
                                                .fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp),
                                            onClick = {
                                                requester!!.execEditProjects(
                                                    groupId = group.value.id,
                                                    projects = projects.toList()
                                                )
                                                if(requester!!.successResponse())
                                                    editProjects.value = false
                                                else
                                                    showSnack(requester!!.errorMessage())
                                            },
                                            content = {
                                                Text(
                                                    text = stringResource(string.edit),
                                                    color = White,
                                                    fontSize = 20.sp
                                                )
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                )
                            }
                            ShowItemsList(
                                show = showProjectsSection,
                                headerTitle = string.projects,
                                extraIcon = extraIcon,
                                itemsList = group.value.projects,
                                clazz = ProjectActivity::class.java,
                                adminPrivileges = isAdmin
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to change a role of a member of the group
     *
     * @param expanded: whether the roles menu is expanded
     * @param member: the member to change the role
     */
    @Composable
    private fun ChangeMemberRole(
        expanded: MutableState<Boolean>,
        member: GroupMember
    ) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            values().forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = role.toString(),
                            color = if (role == ADMIN) ErrorLight else PrimaryLight
                        )
                    },
                    onClick = {
                        requester!!.execChangeMemberRole(
                            groupId = group.value.id,
                            memberId = member.id,
                            role = role
                        )
                        if(!requester!!.successResponse())
                            showSnack(requester!!.errorMessage())
                        expanded.value = false
                    }
                )
            }
        }
    }

    /**
     * Function to refresh an item to display in the UI
     *
     * No-any params required
     */
    override fun refreshItem() {
        CoroutineScope(Dispatchers.Default).launch {
            while (user.id != null && currentGroup.value != null) {
                try {
                    val response = requester!!.execGetSingleGroup(currentGroup.value!!.id)
                    if(requester!!.successResponse()) {
                        val tmpGroup = Group(response)
                        if(needToRefresh(group.value, tmpGroup)) {
                            group.value = tmpGroup
                            isAdmin = group.value.isUserAdmin(user)
                            isMaintainer = group.value.isUserMaintainer(user)
                        }
                    }
                } catch (_ : Exception){
                }
                delay(1000)
            }
        }
    }

}*/