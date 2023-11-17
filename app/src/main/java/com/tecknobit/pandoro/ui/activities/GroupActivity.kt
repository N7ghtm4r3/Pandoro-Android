package com.tecknobit.pandoro.ui.activities

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.helpers.SpaceContent
import com.tecknobit.pandoro.toImportFromLibrary.Group
import com.tecknobit.pandoro.toImportFromLibrary.Group.*
import com.tecknobit.pandoro.toImportFromLibrary.Group.GroupMember.InvitationStatus.PENDING
import com.tecknobit.pandoro.toImportFromLibrary.Group.Role.ADMIN
import com.tecknobit.pandoro.toImportFromLibrary.Project
import com.tecknobit.pandoro.ui.activities.ProjectActivity.Companion.PROJECT_KEY
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.groupDialogs
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandoro.ui.theme.YELLOW_COLOR

/**
 * The **GroupActivity** class is useful to create the activity to show the [Group] details
 *
 * @see ComponentActivity
 * @see PandoroDataActivity
 */
class GroupActivity : PandoroDataActivity() {

    companion object {

        /**
         * **GROUP_KEY** the group key
         */
        const val GROUP_KEY = "group"

    }

    /**
     * **group** the group to show its details
     */
    lateinit var group: Group

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
        group = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(GROUP_KEY, Group::class.java) as Group
        else
            intent.getSerializableExtra(GROUP_KEY) as Group
        val isAdmin = group.isUserAdmin(user)
        val isMaintainer = group.isUserMaintainer(user)
        setContent {
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
                                        imageVector = Default.ArrowBack,
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
                                    onClick = { leaveGroup.value = true }
                                ) {
                                    Icon(
                                        imageVector = Default.ExitToApp,
                                        contentDescription = null,
                                        tint = ErrorLight
                                    )
                                }
                                groupDialogs.LeaveGroup(
                                    show = leaveGroup,
                                    group = group
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
                                            // TODO: VALIDATE INPUTS FIRST
                                            // TODO: MAKE REQUEST THENù
                                            addMembers.value = false
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
                            items(group.members) { member ->
                                val isLoggedUser = member.isLoggedUser(user)
                                val changeRole = remember { mutableStateOf(false) }
                                val isMemberPending = member.invitationStatus == PENDING
                                if((isMemberPending && isMaintainer) || !isMemberPending) {
                                    ListItem(
                                        modifier = if (isAdmin || isMaintainer) {
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
                                                    ImageRequest.Builder(LocalContext.current)
                                                        .data(member.profilePic)
                                                        // TODO: CHANGE WITH THE APP ICON
                                                        //.error(R.drawable.pillars)
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
                                            if (isAdmin || isMaintainer) {
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
                                                    groupDialogs.RemoveUser(
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
                            if (isAdmin) {
                                val editProjects = remember { mutableStateOf(false) }
                                extraIcon = ExtraIcon(
                                    action = { editProjects.value = true },
                                    icon = Default.Edit
                                )
                                pandoroModalSheet.PandoroModalSheet(
                                    show = editProjects,
                                    title = string.edit_the_groups_projects,
                                    content = {
                                        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                                            val projects = mutableStateListOf<Project>()
                                            projects.addAll(group.projects)
                                            items(user.projects) { project ->
                                                var inserted by remember {
                                                    mutableStateOf(projects.contains(project))
                                                }
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Checkbox(
                                                        checked = inserted,
                                                        onCheckedChange = {
                                                            inserted = it
                                                            if (it)
                                                                projects.add(project)
                                                            else
                                                                projects.remove(project)
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
                                                // TODO: MAKE REQUEST THEN
                                                editProjects.value = false
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
                                itemsList = group.projects,
                                key = PROJECT_KEY,
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
            Role.values().forEach { role ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = role.toString(),
                            color = if (role == ADMIN) ErrorLight else PrimaryLight
                        )
                    },
                    onClick = {
                        // TODO: MAKE REQUEST THEN
                        expanded.value = false
                    }
                )
            }
        }
    }

}
