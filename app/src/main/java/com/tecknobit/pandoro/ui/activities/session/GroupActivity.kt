package com.tecknobit.pandoro.ui.activities.session

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_members_list
import com.tecknobit.pandoro.helpers.SpaceContent
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.groupDialogs
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.screens.Screen.Companion.currentGroup
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandoro.ui.theme.YELLOW_COLOR
import com.tecknobit.pandoro.ui.viewmodels.GroupActivityViewModel
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.checkMembersValidity
import com.tecknobit.pandorocore.records.Group
import com.tecknobit.pandorocore.records.users.GroupMember
import com.tecknobit.pandorocore.records.users.GroupMember.InvitationStatus.PENDING
import com.tecknobit.pandorocore.records.users.GroupMember.Role.ADMIN

/**
 * The **GroupActivity** class is useful to create the activity to show the [Group] details
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see PandoroDataActivity
 */
class GroupActivity : PandoroDataActivity() {

    /**
     * **group** the group to show its details
     */
    lateinit var group: Group

    /**
     * **isAdmin** whether the user is an admin
     */
    private var isAdmin: Boolean = false

    /**
     * **isMaintainer** whether the user is a maintainer
     */
    private var isMaintainer: Boolean = false

    private val viewModel = GroupActivityViewModel(
        initialGroup = currentGroup!!,
        snackbarHostState = snackbarHostState
    )

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
            isAdmin = currentGroup!!.isUserAdmin(user)
            isMaintainer = currentGroup!!.isUserMaintainer(user)
            viewModel.refreshGroup {
                isAdmin = group.isUserAdmin(user)
                isMaintainer = group.isUserMaintainer(user)
            }
            group = viewModel.group.collectAsState().value
            val authorId = group.author.id
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
                                            text = group.name
                                        )
                                    }
                                    val author = group.author
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
                                    onClick = {
                                        viewModel.suspendRefresher()
                                        leaveGroup.value = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        tint = ErrorLight
                                    )
                                }
                                groupDialogs.LeaveGroup(
                                    show = leaveGroup,
                                    group = group,
                                    onDismissRequest = {
                                        leaveGroup.value = false
                                        viewModel.restartRefresher()
                                    }
                                )
                            },
                        )
                    },
                    floatingActionButton = {
                        if (isAdmin || isMaintainer) {
                            val addMembers = remember { mutableStateOf(false) }
                            val members = mutableStateListOf("")
                            FloatingActionButton(
                                onClick = {
                                    viewModel.suspendRefresher()
                                    addMembers.value = true
                                }
                            ) {
                                Icon(
                                    imageVector = Default.PersonAdd,
                                    contentDescription = null
                                )
                            }
                            val onDismissRequest = {
                                viewModel.restartRefresher()
                                addMembers.value = false
                            }
                            pandoroModalSheet.PandoroModalSheet(
                                show = addMembers,
                                title = string.add_new_members,
                                onDismissRequest = onDismissRequest,
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
                                                viewModel.addMembers(
                                                    members = members,
                                                    onSuccess = onDismissRequest,
                                                    onFailure = {
                                                        pandoroModalSheet.showSnack(it)
                                                    }
                                                )
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
                                description = group.description
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
                                items = group.members,
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
                                                modifier.clickable {
                                                    viewModel.suspendRefresher()
                                                    changeRole.value = true
                                                }
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
                                            Column {
                                                Text(
                                                    text = if(isMemberPending)
                                                        PENDING.toString()
                                                    else
                                                        member.role.toString(),
                                                    color =
                                                    if (member.isAdmin)
                                                        ErrorLight
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
                                            }
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
                                                        group = group,
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
                                    action = {
                                        editProjects.value = true
                                        viewModel.suspendRefresher()
                                    },
                                    icon = Default.Edit
                                )
                                val onDismissRequest = {
                                    editProjects.value = false
                                    viewModel.restartRefresher()
                                }
                                pandoroModalSheet.PandoroModalSheet(
                                    show = editProjects,
                                    onDismissRequest = onDismissRequest,
                                    title = string.edit_the_groups_projects,
                                    content = {
                                        val projects = mutableStateListOf<String>()
                                        group.projects.forEach { project ->
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
                                                viewModel.editProjects(
                                                    projects = projects,
                                                    onSuccess = onDismissRequest
                                                )
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
                                viewModel = viewModel,
                                show = showProjectsSection,
                                headerTitle = string.projects,
                                extraIcon = extraIcon,
                                itemsList = group.projects,
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
        val onDismissRequest = {
            expanded.value = false
            viewModel.restartRefresher()
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = onDismissRequest
        ) {
            GroupMember.Role.entries.forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = role.toString(),
                            color = if (role == ADMIN)
                                ErrorLight
                            else
                                PrimaryLight
                        )
                    },
                    onClick = {
                        viewModel.changeMemberRole(
                            member = member,
                            role = role
                        )
                        onDismissRequest.invoke()
                    }
                )
            }
        }
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or {@link #onPause}. This
     * is usually a hint for your activity to start interacting with the user, which is a good
     * indicator that the activity became active and ready to receive input. This sometimes could
     * also be a transit state toward another resting state. For instance, an activity may be
     * relaunched to {@link #onPause} due to configuration changes and the activity was visible,
     * but wasn’t the top-most activity of an activity task. {@link #onResume} is guaranteed to be
     * called before {@link #onPause} in this case which honors the activity lifecycle policy and
     * the activity eventually rests in {@link #onPause}.
     *
     * <p>On platform versions prior to {@link android.os.Build.VERSION_CODES#Q} this is also a good
     * place to try to open exclusive-access devices or to get access to singleton resources.
     * Starting  with {@link android.os.Build.VERSION_CODES#Q} there can be multiple resumed
     * activities in the system simultaneously, so {@link #onTopResumedActivityChanged(boolean)}
     * should be used for that purpose instead.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * Will be set the **[FetcherManager.activeContext]** with the current context
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     * @see #onTopResumedActivityChanged(boolean)
     */
    override fun onResume() {
        super.onResume()
        viewModel.setActiveContext(this::class.java)
        viewModel.restartRefresher()
    }

}
