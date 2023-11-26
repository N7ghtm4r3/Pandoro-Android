package com.tecknobit.pandoro.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager.*
import android.view.WindowManager.LayoutParams.*
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.helpers.NavigationHelper
import com.tecknobit.pandoro.helpers.SnackbarLauncher
import com.tecknobit.pandoro.helpers.ui.ListManager
import com.tecknobit.pandoro.records.Project
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.isRefreshing
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.requester
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.screens.NotesScreen
import com.tecknobit.pandoro.ui.screens.NotesScreen.Companion.showAddNoteSheet
import com.tecknobit.pandoro.ui.screens.OverviewScreen
import com.tecknobit.pandoro.ui.screens.ProfileScreen
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.showAddGroupButton
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.showCreateGroup
import com.tecknobit.pandoro.ui.screens.ProjectsScreen
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.projectsList
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.showAddProjectDialog
import com.tecknobit.pandoro.ui.screens.Screen.Companion.scope
import com.tecknobit.pandoro.ui.screens.Screen.Companion.snackbarHostState
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.*
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * The **MainActivity** class is useful to create the main activity where place the main logic of the
 * application like navigation, show the current screen, etc
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see SnackbarLauncher
 * @see ListManager
 */
class MainActivity : ComponentActivity(), SnackbarLauncher, ListManager {

    /**
     * **projectsScreen** -> the screen to show the projects
     */
    private val projectsScreen = ProjectsScreen()

    /**
     * **notesScreen** -> the screen to show the notes
     */
    private val notesScreen = NotesScreen()

    /**
     * **overviewScreen** -> the screen to show the overview
     */
    private val overviewScreen = OverviewScreen()

    /**
     * **profileScreen** -> the screen to show the profile
     */
    private val profileScreen = ProfileScreen()

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
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navigationHelper = NavigationHelper()
        setContent {
            val unreadChangelogsNumber by rememberSaveable {
                mutableIntStateOf(user.unreadChangelogsNumber)
            }
            PandoroTheme {
                Scaffold(
                    topBar = {
                        if (activeScreen.value != Profile) {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = PrimaryLight,
                                    titleContentColor = Color.White,
                                    actionIconContentColor = Color.White
                                ),
                                title = {
                                    Text(
                                        text = stringResource(
                                            activeScreen.value
                                                .getStringResourceTitle(activeScreen.value)
                                        )
                                    )
                                },
                                actions = {
                                    Box {
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(user.profilePic)
                                                    // TODO: CHANGE WITH THE APP ICON
                                                    .error(R.drawable.error)
                                                    .crossfade(500)
                                                    .build()
                                            ),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clickable { activeScreen.value = Profile }
                                                .clip(CircleShape)
                                        )
                                        if (unreadChangelogsNumber > 0) {
                                            Badge(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .border(
                                                        border = BorderStroke(
                                                            width = 0.5.dp,
                                                            color = PrimaryLight
                                                        ),
                                                        shape = CircleShape
                                                    )
                                            ) {
                                                Text(
                                                    text = "$unreadChangelogsNumber",
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            )
                        }
                    },
                    bottomBar = navigationHelper.getNavigationBar(),
                    floatingActionButton = {
                        if (activeScreen.value != Overview) {
                            if (activeScreen.value != Profile
                                || (activeScreen.value == Profile && showAddGroupButton.value)
                            ) {
                                FloatingActionButton(
                                    onClick = {
                                        when (activeScreen.value) {
                                            Projects -> showAddProjectDialog.value = true
                                            Notes -> showAddNoteSheet.value = true
                                            Profile -> showCreateGroup.value = true
                                            else -> {}
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                ) {
                    when(activeScreen.value) {
                        Projects -> {
                            if(!isRefreshing.value) {
                                refreshValues()
                                isRefreshing.value = true
                            }
                            projectsScreen.ShowScreen()
                        }
                        Notes -> notesScreen.ShowScreen()
                        Overview -> overviewScreen.ShowScreen()
                        Profile -> profileScreen.ShowScreen()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        refreshValues()
    }

    /**
     * Function to refresh a list of items to display in the UI
     *
     * No-any params required
     */
    override fun refreshValues() {
        CoroutineScope(Dispatchers.Default).launch {
            var response: String
            while (user.id != null) {
                if(activeScreen.value == Projects || activeScreen.value == Overview) {
                    try {
                        val tmpProjects = mutableStateListOf<Project>()
                        response = requester!!.execProjectsList()
                        if(requester!!.successResponse()) {
                            val jProjects = JSONArray(response)
                            for (j in 0 until jProjects.length())
                                tmpProjects.add(Project(jProjects[j] as JSONObject))
                            if(needToRefresh(projectsList, tmpProjects)) {
                                projectsList.clear()
                                projectsList.addAll(tmpProjects)
                                user.setProjects(projectsList)
                            }
                        } else
                            showSnack(requester!!.errorMessage())
                    } catch (_: JSONException){
                    }
                }
                delay(1000)
            }
        }
    }

    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param message: the message to show
     */
    override fun showSnack(message: String) {
        showSnack(
            scope = scope,
            snackbarHostState = snackbarHostState,
            message = message
        )
    }

}