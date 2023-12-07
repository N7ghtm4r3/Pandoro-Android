package com.tecknobit.pandoro.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.change_email
import com.tecknobit.pandoro.R.string.change_password
import com.tecknobit.pandoro.R.string.new_email
import com.tecknobit.pandoro.R.string.new_password
import com.tecknobit.pandoro.R.string.no_any_changelogs
import com.tecknobit.pandoro.R.string.no_any_groups
import com.tecknobit.pandoro.R.string.profile_details
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_email
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_password
import com.tecknobit.pandoro.helpers.Divide
import com.tecknobit.pandoro.helpers.isEmailValid
import com.tecknobit.pandoro.helpers.isPasswordValid
import com.tecknobit.pandoro.records.Changelog
import com.tecknobit.pandoro.records.Changelog.ChangelogEvent.INVITED_GROUP
import com.tecknobit.pandoro.records.Group
import com.tecknobit.pandoro.records.users.GroupMember.Role.ADMIN
import com.tecknobit.pandoro.records.users.GroupMember.Role.DEVELOPER
import com.tecknobit.pandoro.records.users.GroupMember.Role.MAINTAINER
import com.tecknobit.pandoro.services.UsersHelper.PROFILE_PIC_KEY
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.groupDialogs
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.requester
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.userProfilePic
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.GREEN_COLOR
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.min


/**
 * The **ProfileScreen** class is useful to show the profile of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 */
class ProfileScreen: Screen() {

    companion object {

        /**
         * **HIDE_PASS_VALUE** -> the value to show an hidden password
         */
        const val HIDE_PASS_VALUE = "****"

        /**
         * **titles** -> the titles list
         */
        lateinit var titles: List<String>

        /**
         * **showAddGroupButton** -> whether show the add group button
         */
        var showAddGroupButton = mutableStateOf(false)

        /**
         * **showCreateGroup** -> whether show the add group dialog
         */
        lateinit var showCreateGroup: MutableState<Boolean>

        /**
         * **changelogs** -> the list of the changelogs
         */
        val changelogs = mutableStateListOf<Changelog>()

        /**
         * **groups** -> the list of the groups
         */
        val groups = mutableStateListOf<Group>()

    }

    /**
     * Function to show the content screen
     *
     * No any params required
     */
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun ShowScreen() {
        showCreateGroup = rememberSaveable { mutableStateOf(false) }
        titles = listOf(
            stringResource(profile_details),
            stringResource(string.changelogs),
            stringResource(string.my_groups)
        )
        groupDialogs.CreateGroup()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var showImagePicker by remember { mutableStateOf(false) }
            var profilePic by remember { mutableStateOf(user.profilePic) }
            FilePicker(
                show = showImagePicker,
                fileExtensions = listOf("jpeg", "jpg", "png")
            ) { file ->
                if(file != null) {
                    var imagePath: String? = null
                    runBlocking {
                        async { imagePath = getImagePath(file.path.toUri()) }.await()
                    }
                    if(imagePath != null) {
                        val response = requester!!.execChangeProfilePic(File(imagePath!!))
                        if(requester!!.successResponse()) {
                            localAuthHelper.storeProfilePic(JsonHelper(response)
                                .getString(PROFILE_PIC_KEY), true)
                        } else
                            showSnack(requester!!.errorMessage())
                    }
                    showImagePicker = false
                }
            }
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 475.dp)
                    .clickable { showImagePicker = true },
                bitmap = userProfilePic!!,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            PandoroCard(
                modifier = Modifier
                    .align(BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(.75f),
                elevation = CardDefaults.cardElevation(20.dp),
                shape = RoundedCornerShape(topEnd = 80.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val pageCount = 3
                    val pagerState = rememberPagerState(pageCount = { pageCount })
                    HorizontalPager(state = pagerState) { page ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(2f),
                                    text = titles[page],
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .height(24.dp)
                                        .width(100.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    repeat(pageCount) { iteration ->
                                        Box(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(
                                                    if (pagerState.currentPage == iteration)
                                                        ErrorLight
                                                    else
                                                        ErrorLight.copy(.5f)
                                                )
                                                .weight(1f)
                                                .height(4.dp)
                                        )
                                    }
                                }
                            }
                            when (page) {
                                0 -> ShowProfileDetails()
                                1 -> ShowChangelogs()
                                2 -> ShowMyGroups()
                            }
                            showAddGroupButton.value = pagerState.currentPage == 2
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to get the complete image path of an image
     *
     * @param uri: the uri of the image
     * @return the path of the image
     */
    private fun getImagePath(uri: Uri): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available() ?: 0
            val bufferSize = min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream?.read(buffers).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream?.close()
            outputStream.close()
        } catch (_: Exception) {
        } finally {
            returnCursor.close()
        }
        return file.path
    }

    /**
     * Function to show the details of the user
     *
     * No any params required
     */
    @Composable
    private fun ShowProfileDetails() {
        val showEditEmail = remember { mutableStateOf(false) }
        val showEditPassword = remember { mutableStateOf(false) }
        var passValue by remember { mutableStateOf(HIDE_PASS_VALUE) }
        var email by remember { mutableStateOf(user.email) }
        ShowSubsection {
            CreateInputModalBottom(
                show = showEditEmail,
                title = change_email,
                label = new_email,
                requestLogic = {
                    if (isEmailValid(sheetInputValue.value)) {
                        requester!!.execChangeEmail(sheetInputValue.value)
                        if(requester!!.successResponse()) {
                            localAuthHelper.storeEmail(sheetInputValue.value, true)
                            email = user.email
                            sheetInputValue.value = ""
                            showEditEmail.value = false
                        } else
                            pandoroModalSheet.showSnack(requester!!.errorMessage())
                    } else
                        pandoroModalSheet.showSnack(you_must_insert_a_correct_email)
                }
            )
            CreateInputModalBottom(
                show = showEditPassword,
                title = change_password,
                label = new_password,
                requestLogic = {
                    if (isPasswordValid(sheetInputValue.value)) {
                        requester!!.execChangePassword(sheetInputValue.value)
                        if(requester!!.successResponse()) {
                            localAuthHelper.storePassword(sheetInputValue.value, true)
                            if(passValue != HIDE_PASS_VALUE)
                                passValue = user.password
                            sheetInputValue.value = ""
                            showEditPassword.value = false
                        } else
                            pandoroModalSheet.showSnack(requester!!.errorMessage())
                    } else
                        pandoroModalSheet.showSnack(you_must_insert_a_correct_password)
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(id = string.name)
                    )
                    ShowProfileData(profileData = user.name)
                }
            }
            Divide()
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = stringResource(string.surname)
                    )
                    ShowProfileData(profileData = user.surname)
                }
            }
            Divide()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(string.email)
                    )
                    ShowProfileData(profileData = email)
                }
                IconButton(
                    modifier = Modifier.padding(start = 10.dp),
                    onClick = { showEditEmail.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = ErrorLight
                    )
                }
            }
            Divide()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(string.password),
                    )
                    ShowProfileData(
                        profileData = passValue,
                        modifier = Modifier.clickable {
                            passValue =
                                if (passValue.contains(HIDE_PASS_VALUE))
                                    user.password
                                else
                                    HIDE_PASS_VALUE
                        }
                    )
                }
                IconButton(
                    modifier = Modifier.padding(start = 10.dp),
                    onClick = { showEditPassword.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = ErrorLight
                    )
                }
            }
            Divide()
            Button(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { localAuthHelper.logout() },
                content = {
                    Text(
                        text = stringResource(string.logout),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            )
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErrorLight
                ),
                onClick = {
                    requester!!.execDeleteAccount()
                    if(requester!!.successResponse())
                        localAuthHelper.logout()
                    else
                        showSnack(requester!!.errorMessage())
                },
                content = {
                    Text(
                        text = stringResource(string.delete),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            )
        }
    }

    /**
     * Function to show the profile of the user
     *
     * @param profileData: profile data to show
     * @param modifier: modifier for the text
     */
    @Composable
    private fun ShowProfileData(
        profileData: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            modifier = modifier,
            text = profileData,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

    /**
     * Function to show the changelogs of the user
     *
     * No any params required
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ShowChangelogs() {
        ShowSubsection {
            if(changelogs.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = 55.dp,
                        top = 5.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(changelogs) { changelog ->
                        val isInviteToAGroup = changelog.changelogEvent == INVITED_GROUP
                        val showInvite = remember { mutableStateOf(false) }
                        val group = changelog.group
                        if(showInvite.value) {
                            AlertDialog(
                                modifier = Modifier.height(330.dp),
                                onDismissRequest = { showInvite.value = false },
                                icon = {
                                    IconButton(
                                        onClick = { navToGroup(group) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Group,
                                            contentDescription = null,
                                            tint = PrimaryLight
                                        )
                                    }
                                },
                                title = {
                                    Text(
                                        text = stringResource(id = string.accept_invite)
                                    )
                                },
                                text = {
                                    Column (
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            text = changelog.content,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Justify
                                        )
                                        Row (
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(top = 5.dp),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Button(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .width(200.dp)
                                                    .height(40.dp),
                                                shape = RoundedCornerShape(10.dp),
                                                onClick = {
                                                    requester!!.execDeclineInvitation(
                                                        groupId = group.id,
                                                        changelogId = changelog.id
                                                    )
                                                    if(requester!!.successResponse())
                                                        showInvite.value = false
                                                    else
                                                        showSnack(requester!!.errorMessage())
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = ErrorLight
                                                ),
                                                content = {
                                                    Text(
                                                        text = stringResource(string.decline),
                                                        color = Color.White
                                                    )
                                                }
                                            )
                                            Button(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .width(200.dp)
                                                    .height(40.dp),
                                                shape = RoundedCornerShape(10.dp),
                                                onClick = {
                                                    requester!!.execAcceptInvitation(
                                                        groupId = group.id,
                                                        changelogId = changelog.id
                                                    )
                                                    if(requester!!.successResponse())
                                                        showInvite.value = false
                                                    else
                                                        showSnack(requester!!.errorMessage())
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = GREEN_COLOR
                                                ),
                                                content = {
                                                    Text(
                                                        text = stringResource(string.join),
                                                        color = Color.White
                                                    )
                                                }
                                            )
                                        }
                                    }
                                },
                                dismissButton = {},
                                confirmButton = {
                                    TextButton(
                                        onClick = { showInvite.value = false },
                                        content = {
                                            Text(
                                                text = stringResource(R.string.dismiss),
                                            )
                                        }
                                    )
                                }
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp)
                                .height(110.dp)
                                .clickable {
                                    if (isInviteToAGroup)
                                        showInvite.value = true
                                    else {
                                        if (group != null)
                                            navToGroup(group)
                                        else {
                                            val project = changelog.project
                                            if (project != null)
                                                navToProject(project)
                                        }
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(10f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val isRed = changelog.isRed
                                    if (!isRed) {
                                        Badge(
                                            modifier = Modifier
                                                .padding(end = 5.dp)
                                                .size(10.dp)
                                                .clickable { readChangelog(changelog) }
                                        ) {
                                            Text(text = "")
                                        }
                                    }
                                    var modifier = Modifier.weight(10f)
                                    if (!isRed) {
                                        modifier = modifier.clickable {
                                            readChangelog(changelog)
                                        }
                                    }
                                    Text(
                                        modifier = modifier,
                                        text = changelog.title,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = changelog.content,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Justify
                                )
                            }
                            IconButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .size(24.dp),
                                onClick = {
                                    val groupId = if (changelog.group != null)
                                        changelog.group.id
                                    else
                                        null
                                    requester!!.execDeleteChangelog(
                                        changelogId = changelog.id,
                                        groupId = groupId
                                    )
                                    if(!requester!!.successResponse())
                                        showSnack(requester!!.errorMessage())
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = ErrorLight
                                )
                            }
                        }
                        Divide()
                    }
                }
            } else
                EmptyList(message = no_any_changelogs)
        }
    }

    /**
     * Function to show the groups of the user
     *
     * No any params required
     */
    @Composable
    private fun ShowMyGroups() {
        ShowSubsection {
            if(groups.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = 55.dp,
                        top = 5.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(groups) { group ->
                        val role =
                            if (group.isUserAdmin(user))
                                ADMIN
                            else if (group.isUserMaintainer(user))
                                MAINTAINER
                            else
                                DEVELOPER
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp)
                                .height(100.dp)
                                .clickable { navToGroup(group) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(10f),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = group.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                                Text(
                                    text = group.description
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(
                                        text = stringResource(string.role)
                                    )
                                    Text(
                                        text = role.toString(),
                                        color = if (role == ADMIN) ErrorLight else PrimaryLight,
                                    )
                                }
                            }
                            if (role == ADMIN) {
                                val deleteGroup = remember { mutableStateOf(false) }
                                IconButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(24.dp),
                                    onClick = { deleteGroup.value = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = ErrorLight
                                    )
                                }
                                groupDialogs.DeleteGroup(
                                    show = deleteGroup,
                                    group = group
                                )
                            }
                        }
                        Divide()
                    }
                }
            } else
                EmptyList(message = no_any_groups)
        }
    }

    /**
     * Function to show the subsection of the screen
     *
     * @param content: content of the subsection to show
     */
    @Composable
    private fun ShowSubsection(content: @Composable ColumnScope.() -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }


    /**
     * Function to make request to read a changelog
     *
     * @param changelog: the changelog to read
     */
    private fun readChangelog(changelog: Changelog) {
        requester!!.execReadChangelog(changelog.id)
        if(!requester!!.successResponse())
            showSnack(requester!!.errorMessage())
    }

}