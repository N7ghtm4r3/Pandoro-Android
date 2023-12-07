package com.tecknobit.pandoro.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.helpers.AndroidRequester
import com.tecknobit.pandoro.records.users.User
import com.tecknobit.pandoro.ui.activities.ConnectActivity.LocalAuthHelper
import com.tecknobit.pandoro.ui.components.dialogs.GroupDialogs
import com.tecknobit.pandoro.ui.components.dialogs.PandoroModalSheet
import com.tecknobit.pandoro.ui.components.dialogs.ProjectDialogs
import com.tecknobit.pandoro.ui.screens.Screen
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.defTypeface
import kotlinx.coroutines.delay

/**
 * The **SplashScreen** class is useful to create the first showable screen of the Pandoro's application
 * and init the logic
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {

    companion object {

        /**
         * **isRefreshing** -> whether is current allowed refresh the lists
         */
        lateinit var isRefreshing: MutableState<Boolean>

        /**
         * **localAuthHelper** -> the instance to manage the auth credentials in local
         */
        lateinit var localAuthHelper: LocalAuthHelper

        /**
         * **user** the current user logged in
         */
        var user = User()

        /**
         * **requester** -> the stance to manage the requests with the backend
         */
        var requester: AndroidRequester? = null

        /**
         * **activeScreen** -> the active screen to show
         */
        lateinit var activeScreen: MutableState<Screen.ScreenType>

        /**
         * **groupDialogs** the instance to manage the dialogs of the groups
         */
        val groupDialogs = GroupDialogs()

        /**
         * **projectDialogs** the instance to manage the dialogs of the projects
         */
        val projectDialogs = ProjectDialogs()

        /**
         * **pandoroModalSheet** the instance to manage the modal bottom sheets
         */
        val pandoroModalSheet = PandoroModalSheet()

        var userProfilePic: ImageBitmap? = null

        /**
         * **context** the current context of the application
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        /**
         * Method to open a link in app
         *
         * @param url: the url to open
         */
        fun openLink(url: String) {
            val intent = Intent()
            intent.data = url.toUri()
            intent.action = Intent.ACTION_VIEW
            startActivity(context, intent, null)
        }

    }

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        setContent {
            activeScreen = remember { mutableStateOf(Projects) }
            PandoroTheme {
                context = LocalContext.current
                isRefreshing = rememberSaveable { mutableStateOf(false) }
                localAuthHelper = ConnectActivity().LocalAuthHelper()
                localAuthHelper.initUserCredentials()
                defTypeface = ResourcesCompat.getFont(context, R.font.rem)!!
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getString(R.string.app_name),
                        color = Color.White,
                        fontSize = 40.sp,
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "by Tecknobit",
                            color = Color.White,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            LaunchedEffect(key1 = true) {
                delay(2000)
                if(user.id != null)
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                else
                    startActivity(Intent(this@SplashScreen, ConnectActivity::class.java))
            }
        }
    }

}