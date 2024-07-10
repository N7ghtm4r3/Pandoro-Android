package com.tecknobit.pandoro.ui.activities.session

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.helpers.NavigationHelper
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.screens.NotesScreen
import com.tecknobit.pandoro.ui.screens.NotesScreen.Companion.showAddNoteSheet
import com.tecknobit.pandoro.ui.screens.OverviewScreen
import com.tecknobit.pandoro.ui.screens.ProfileScreen
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.showAddGroupButton
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.showCreateGroup
import com.tecknobit.pandoro.ui.screens.ProjectsScreen
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.showAddProjectDialog
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Notes
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Overview
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Profile
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandoro.ui.viewmodels.MainActivityViewModel


/**
 * The **MainActivity** class is useful to create the main activity where place the main logic of the
 * application like navigation, show the current screen, etc
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
class MainActivity : ComponentActivity() {

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
     * *viewModel* -> the support view model to manage the requests to the backend
     */
    private val viewModel = MainActivityViewModel()

    /**
     * **projectsScreen** -> the screen to show the projects
     */
    private val projectsScreen = ProjectsScreen(
        viewModel = viewModel
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
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navigationHelper = NavigationHelper()
        setContent {
            viewModel.unreadChangelogsNumber = remember { mutableIntStateOf(user.unreadChangelogsNumber) }
            PandoroTheme {
                val isServerOffline = viewModel.isServerOffline.collectAsState().value
                AnimatedVisibility(
                    visible = isServerOffline,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ServerOfflineUI()
                }
                AnimatedVisibility(
                    visible = !isServerOffline,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
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
                                                    ImageRequest.Builder(context)
                                                        .data(user.profilePic)
                                                        .error(R.drawable.logo)
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
                                            if (viewModel.unreadChangelogsNumber.intValue > 0) {
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
                                                        text = "${viewModel.unreadChangelogsNumber.intValue}",
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
                                if (activeScreen.value != Profile ||
                                    (activeScreen.value == Profile && showAddGroupButton.value)
                                ) {
                                    FloatingActionButton(
                                        onClick = {
                                            when (activeScreen.value) {
                                                Projects -> {
                                                    viewModel.suspendRefresher()
                                                    showAddProjectDialog.value = true
                                                }
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
                            Projects -> projectsScreen.ShowScreen()
                            Notes -> notesScreen.ShowScreen()
                            Overview -> overviewScreen.ShowScreen()
                            Profile -> profileScreen.ShowScreen()
                        }
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

    /**
     * Function to display the layout if the server is currently offline
     *
     * No-any params required
     */
    @Composable
    private fun ServerOfflineUI() {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(150.dp),
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = stringResource(R.string.server_currently_offline),
            )
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
        viewModel.refreshValues()
    }

    override fun onPause() {
        super.onPause()
        viewModel.suspendRefresher()
    }

}