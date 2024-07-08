package com.tecknobit.pandoro.ui.activities.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.ui.activities.auth.ConnectActivity
import com.tecknobit.pandoro.ui.activities.auth.ConnectActivity.LocalAuthHelper
import com.tecknobit.pandoro.ui.activities.session.MainActivity
import com.tecknobit.pandoro.ui.components.dialogs.PandoroModalSheet
import com.tecknobit.pandoro.ui.screens.Screen
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.defTypeface
import com.tecknobit.pandorocore.helpers.PandoroRequester
import com.tecknobit.pandorocore.records.users.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * The **SplashScreen** class is useful to create the first showable screen of the Pandoro's application
 * and init the logic
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see ImageLoaderFactory
 */
@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity(), ImageLoaderFactory {

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
        var requester: PandoroRequester? = null

        /**
         * **activeScreen** -> the active screen to show
         */
        lateinit var activeScreen: MutableState<Screen.ScreenType>

        /**
         * **groupDialogs** the instance to manage the dialogs of the groups
         */
        //val groupDialogs = GroupDialogs()

        /**
         * **projectDialogs** the instance to manage the dialogs of the projects
         */
        //val projectDialogs = ProjectDialogs()

        /**
         * **pandoroModalSheet** the instance to manage the modal bottom sheets
         */
        val pandoroModalSheet = PandoroModalSheet()

        /**
         * **context** the current context of the application
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        /**
         * **reviewManager** the manager to review the app during its flow
         */
        lateinit var reviewManager: ReviewManager

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
     * **appUpdateManager** the manager to check if there is an update available
     */
    private lateinit var appUpdateManager: AppUpdateManager

    /**
     * **launcher** the result registered for [appUpdateManager] and the action to execute if fails
     */
    private var launcher  = registerForActivityResult(StartIntentSenderForResult()) { result: ActivityResult ->
        if (result.resultCode != RESULT_OK)
            launchApp()
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
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForUpdates()
        setContent {
            activeScreen = remember { mutableStateOf(Projects) }
            PandoroTheme {
                context = LocalContext.current
                reviewManager = ReviewManagerFactory.create(context)
                Coil.imageLoader(context)
                Coil.setImageLoader(newImageLoader())
                isRefreshing = rememberSaveable { mutableStateOf(false) }
                localAuthHelper = ConnectActivity().LocalAuthHelper()
                localAuthHelper.initUserCredentials()
                setLocale()
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
        }
    }

    /**
     * Return a new [ImageLoader].
     */
    override fun newImageLoader(): ImageLoader {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, validateSelfSignedCertificate(), SecureRandom())
        return ImageLoader.Builder(context)
            .okHttpClient {
                OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.socketFactory,
                        validateSelfSignedCertificate()[0] as X509TrustManager
                    )
                    .hostnameVerifier { _: String?, _: SSLSession? -> true }
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build()
            }
            .addLastModifiedToFileCacheKey(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    /**
     * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
     * No-any params required
     *
     * @return list of trust managers as [Array] of [TrustManager]
     * @apiNote this method disable all checks on the SLL certificate validity, so is recommended to
     * use for test only or in a private distribution on own infrastructure
     */
    private fun validateSelfSignedCertificate(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
    }

    /**
     * Method to check if there are some update available to install
     * No-any params required
     *
     */
    private fun checkForUpdates() {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { info ->
                val isUpdateAvailable = info.updateAvailability() == UPDATE_AVAILABLE
                val isUpdateSupported = info.isImmediateUpdateAllowed
                if(isUpdateAvailable && isUpdateSupported) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        launcher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                } else
                    launchApp()
            }
            .addOnFailureListener {
                launchApp()
            }
    }

    /**
     * Method to launch the app and the user session
     * No-any params required
     *
     */
    private fun launchApp() {
        runBlocking {
            delay(500)
            if(user.id != null)
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            else
                startActivity(Intent(this@SplashScreen, ConnectActivity::class.java))
        }
    }

    /**
     * Function to set locale language for the application
     *
     * No-any params required
     */
    private fun setLocale() {
        val userLanguage = user.language
        val locale = if(userLanguage != null)
            Locale.forLanguageTag(userLanguage)
        else
            Locale.getDefault()
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}