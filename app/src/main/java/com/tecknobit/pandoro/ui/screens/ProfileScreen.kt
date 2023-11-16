package com.tecknobit.pandoro.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest.*
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.change_email
import com.tecknobit.pandoro.R.string.change_password
import com.tecknobit.pandoro.R.string.new_email
import com.tecknobit.pandoro.R.string.new_password
import com.tecknobit.pandoro.R.string.profile_details
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_email
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_password
import com.tecknobit.pandoro.helpers.Divide
import com.tecknobit.pandoro.helpers.isEmailValid
import com.tecknobit.pandoro.helpers.isPasswordValid
import com.tecknobit.pandoro.toImportFromLibrary.Changelog
import com.tecknobit.pandoro.toImportFromLibrary.Group.Role.ADMIN
import com.tecknobit.pandoro.toImportFromLibrary.Group.Role.DEVELOPER
import com.tecknobit.pandoro.toImportFromLibrary.Group.Role.MAINTAINER
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.groupDialogs
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PrimaryLight

class ProfileScreen: Screen() {

    companion object {

        const val HIDE_PASS_VALUE = "****"

        lateinit var titles: List<String>

        var showAddGroupButton = mutableStateOf(false)

        lateinit var showCreateGroup: MutableState<Boolean>

    }

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
            var profilePic by remember { mutableStateOf(user.profilePic) }
            val pickPictureLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { imageUri ->
                if (imageUri != null) {
                    val path = imageUri.path
                    if (path != null) {
                        // TODO: MAKE REQUEST THEN USE THE REAL path
                        profilePic = path
                    }
                } else
                    profilePic =
                        "https://consigliviaggiasiatravel.files.wordpress.com/2020/02/dscn1124-1.jpg"
            }
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 475.dp)
                    .clickable { pickPictureLauncher.launch("image/*") },
                painter = rememberAsyncImagePainter(
                    Builder(LocalContext.current)
                        .data(profilePic)
                        // TODO: CHANGE WITH THE APP ICON
                        .error(R.drawable.pillars)
                        .crossfade(500)
                        .build()
                ),
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

    @Composable
    private fun ShowProfileDetails() {
        val showEditEmail = remember { mutableStateOf(false) }
        val showEditPassword = remember { mutableStateOf(false) }
        var passValue by remember { mutableStateOf(HIDE_PASS_VALUE) }
        ShowSubsection {
            CreateInputModalBottom(
                containerColor = Color.White,
                show = showEditEmail,
                title = change_email,
                label = new_email,
                requestLogic = {
                    if (isEmailValid(sheetInputValue.value)) {
                        /*TODO MAKE REQUEST THEN*/
                        sheetInputValue.value = ""
                        showEditEmail.value = false
                    } else
                        pandoroModalSheet.showSnack(you_must_insert_a_correct_email)
                }
            )
            CreateInputModalBottom(
                containerColor = Color.White,
                show = showEditPassword,
                title = change_password,
                label = new_password,
                requestLogic = {
                    if (isPasswordValid(sheetInputValue.value)) {
                        /*TODO MAKE REQUEST THEN*/
                        sheetInputValue.value = ""
                        showEditPassword.value = false
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
                    ShowProfileData(profileData = user.email)
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
                onClick = {
                    // TODO: MAKE REQUEST THEN
                },
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
                    // TODO: MAKE REQUEST THEN
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ShowChangelogs() {
        ShowSubsection {
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = 55.dp,
                    top = 5.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(user.changelogs) { changelog ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp)
                            .height(100.dp)
                            .clickable {
                                val project = changelog.project
                                if (project != null)
                                    navToProject(project)
                                else {
                                    val group = changelog.group
                                    if (group != null)
                                        navToGroup(group)
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
                            onClick = { /*TODO MAKE REQUEST THEN*/ }
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
        }
    }

    @Composable
    private fun ShowMyGroups() {
        ShowSubsection {
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = 55.dp,
                    top = 5.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(user.groups) { group ->
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
        }
    }

    @Composable
    private fun ShowSubsection(content: @Composable ColumnScope.() -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }

    private fun readChangelog(changelog: Changelog) {
        // TODO: MAKE REQUEST THEN
    }

}