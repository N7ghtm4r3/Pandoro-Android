package com.tecknobit.pandoro.ui.activities.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.environment.records.EquinoxUser.EMAIL_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.PASSWORD_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.PROFILE_PIC_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.SURNAME_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.TOKEN_KEY
import com.tecknobit.equinox.inputs.InputValidator.DEFAULT_LANGUAGE
import com.tecknobit.equinox.inputs.InputValidator.isEmailValid
import com.tecknobit.equinox.inputs.InputValidator.isNameValid
import com.tecknobit.equinox.inputs.InputValidator.isPasswordValid
import com.tecknobit.equinox.inputs.InputValidator.isServerSecretValid
import com.tecknobit.equinox.inputs.InputValidator.isSurnameValid
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.are_you_new_to_pandoro
import com.tecknobit.pandoro.R.string.have_an_account
import com.tecknobit.pandoro.R.string.hello
import com.tecknobit.pandoro.R.string.server_address
import com.tecknobit.pandoro.R.string.server_secret
import com.tecknobit.pandoro.R.string.sign_in
import com.tecknobit.pandoro.R.string.sign_up
import com.tecknobit.pandoro.R.string.welcome_back
import com.tecknobit.pandoro.helpers.SnackbarLauncher
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.isRefreshing
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.openLink
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.activities.session.MainActivity
import com.tecknobit.pandoro.ui.activities.viewmodels.ConnectViewModel
import com.tecknobit.pandoro.ui.activities.viewmodels.PandoroViewModel.Companion.requester
import com.tecknobit.pandoro.ui.components.CreateSnackbarHost
import com.tecknobit.pandoro.ui.components.PandoroTextField
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects
import com.tecknobit.pandoro.ui.theme.BackgroundLight
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandorocore.helpers.InputsValidator
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isServerAddressValid
import com.tecknobit.pandorocore.helpers.InputsValidator.ScreenType.SignIn
import com.tecknobit.pandorocore.helpers.InputsValidator.ScreenType.SignUp
import com.tecknobit.pandorocore.helpers.PandoroRequester
import com.tecknobit.pandorocore.records.structures.PandoroItem.IDENTIFIER_KEY
import com.tecknobit.pandorocore.records.users.User
import com.tecknobit.pandorocore.records.users.User.LANGUAGE_KEY
import com.tecknobit.pandorocore.ui.LocalUser
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

/**
 * The **ConnectActivity** class is useful to create an activity to connect the user to the Pandoro's
 * system
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see ComponentActivity
 * @see SnackbarLauncher
 */
class ConnectActivity : ComponentActivity() {

    /**
     * **scope** the coroutine to launch the snackbars
     */
    private lateinit var scope: CoroutineScope

    /**
     * **snackbarHostState** the host to launch the snackbars
     */
    private lateinit var snackbarHostState: SnackbarHostState

    private lateinit var viewModel: ConnectViewModel

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            scope = rememberCoroutineScope()
            snackbarHostState = remember { SnackbarHostState() }
            viewModel = ConnectViewModel(
                snackbarHostState = snackbarHostState
            )
            PandoroTheme {
                var screenType by remember { mutableStateOf(SignIn) }
                val title = createTitle(screenType)
                val isIsSignIn = screenType == SignIn
                Scaffold(
                    snackbarHost = { CreateSnackbarHost(hostState = snackbarHostState) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .background(PrimaryLight)
                                .height(120.dp)
                                .fillMaxWidth(),
                            content = {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(
                                                top = 20.dp,
                                                start = 20.dp
                                            )
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text =
                                            if (isIsSignIn)
                                                getString(welcome_back) + ","
                                            else
                                                getString(hello) + ",",
                                            color = White,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "${title}!",
                                            color = White,
                                            fontSize = 30.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(
                                                top = 20.dp,
                                                end = 20.dp
                                            )
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    openLink("https://github.com/N7ghtm4r3/Pandoro-Android")
                                                }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.github),
                                                    contentDescription = null,
                                                    tint = White
                                                )
                                            }
                                            Text(
                                                modifier = Modifier.padding(end = 5.dp),
                                                text = "v. 1.0.4",
                                                fontSize = 12.sp,
                                                color = White,
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        Column(
                            modifier = Modifier
                                .background(BackgroundLight)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            viewModel.serverAddress = remember { mutableStateOf("") }
                            viewModel.serverSecret = remember { mutableStateOf("") }
                            viewModel.name = remember { mutableStateOf("") }
                            viewModel.surname = remember { mutableStateOf("") }
                            viewModel.email = remember { mutableStateOf("") }
                            viewModel.password = remember { mutableStateOf("") }
                            PandoroTextField(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(60.dp),
                                textFieldModifier = Modifier.fillMaxWidth(),
                                label = getString(server_address),
                                value = viewModel.serverAddress,
                                isError = !isServerAddressValid(viewModel.serverAddress.value)
                            )
                            if (!isIsSignIn) {
                                PandoroTextField(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(300.dp)
                                        .height(60.dp),
                                    textFieldModifier = Modifier.fillMaxWidth(),
                                    label = getString(server_secret),
                                    value = viewModel.serverSecret,
                                    isError = !isServerSecretValid(viewModel.serverSecret.value),
                                )
                                PandoroTextField(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(300.dp)
                                        .height(60.dp),
                                    textFieldModifier = Modifier.fillMaxWidth(),
                                    label = getString(string.name),
                                    value = viewModel.name,
                                    isError = !isNameValid(viewModel.name.value),
                                )
                                PandoroTextField(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(300.dp)
                                        .height(60.dp),
                                    textFieldModifier = Modifier.fillMaxWidth(),
                                    label = getString(string.surname),
                                    value = viewModel.surname,
                                    isError = !isSurnameValid(viewModel.surname.value)
                                )
                            }
                            PandoroTextField(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(60.dp),
                                textFieldModifier = Modifier.fillMaxWidth(),
                                label = getString(string.email),
                                value = viewModel.email,
                                isError = !isEmailValid(viewModel.email.value),
                                onValueChange = {
                                    viewModel.email.value = it.replace(" ", "")
                                }
                            )
                            var isVisible by remember { mutableStateOf(false) }
                            PandoroTextField(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(60.dp),
                                textFieldModifier = Modifier.fillMaxWidth(),
                                visualTransformation = if (isVisible) None
                                else
                                    PasswordVisualTransformation(),
                                label = getString(string.password),
                                value = viewModel.password,
                                isError = !isPasswordValid(viewModel.password.value),
                                onValueChange = {
                                    viewModel.password.value = it.replace(" ", "")
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { isVisible = !isVisible }
                                    ) {
                                        Icon(
                                            imageVector = if (isVisible)
                                                Default.VisibilityOff
                                            else
                                                Default.Visibility,
                                            contentDescription = null,
                                        )
                                    }
                                }
                            )
                            Button(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(60.dp),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    when (screenType) {
                                        SignUp -> viewModel.signUp()
                                        SignIn -> viewModel.signIn()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryLight
                                ),
                                content = {
                                    Text(
                                        text = title,
                                        color = White,
                                        fontSize = 20.sp
                                    )
                                }
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .clickable(true, onClick = {
                                        screenType =
                                            if (screenType == SignUp) {
                                                viewModel.name.value = ""
                                                viewModel.surname.value = ""
                                                SignIn
                                            } else
                                                SignUp
                                    }),
                                text = buildAnnotatedString {
                                    append(createMessage(screenType))
                                    withStyle(style = SpanStyle(color = ErrorLight)) {
                                        append(" " + createTitleLink(screenType))
                                    }
                                },
                                fontSize = 14.sp
                            )
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
     * Function to create the title to show
     *
     * @param screenType: the type from create the title
     * @return title to show as [String]
     */
    private fun createTitle(
        screenType: InputsValidator.ScreenType
    ): String {
        return when (screenType) {
            SignUp -> context.getString(sign_up)
            SignIn -> context.getString(sign_in)
        }
    }

    /**
     * Function to create the message to show
     *
     * @param screenType: the type from create the message
     * @return message to show as [String]
     */
    private fun createMessage(
        screenType: InputsValidator.ScreenType
    ): String {
        return when (screenType) {
            SignIn -> context.getString(are_you_new_to_pandoro)
            SignUp -> context.getString(have_an_account)
        }
    }

    /**
     * Function to create the title link to show
     *
     * @param screenType: the type from create the title link
     * @return title link to show as [String]
     */
    private fun createTitleLink(
        screenType: InputsValidator.ScreenType
    ): String {
        return when (screenType) {
            SignUp -> context.getString(sign_in)
            SignIn -> context.getString(sign_up)
        }
    }

    /**
     * This **LocalAuthHelper** class is useful to manage the credentials of the user in local
     *
     * @author Tecknobit - N7ghtm4r3
     * @see LocalUser
     */
    inner class LocalAuthHelper : LocalUser() {

        /**
         * **preferences** -> the instance to manage the user preferences
         */
        private val preferences = context.getSharedPreferences("pandoro", MODE_PRIVATE)

        /**
         * Function to init the user credentials
         *
         * No-any params required
         */
        override fun initUserCredentials() {
            host = preferences.getString(SERVER_ADDRESS_KEY, null)
            val userId = preferences.getString(IDENTIFIER_KEY, null)
            val userToken = preferences.getString(TOKEN_KEY, null)
            if(userId != null) {
                user = User(
                    JSONObject()
                        .put(IDENTIFIER_KEY, userId)
                        .put(TOKEN_KEY, userToken)
                        .put(PROFILE_PIC_KEY, preferences.getString(PROFILE_PIC_KEY, null))
                        .put(NAME_KEY, preferences.getString(NAME_KEY, null))
                        .put(SURNAME_KEY, preferences.getString(SURNAME_KEY, null))
                        .put(EMAIL_KEY, preferences.getString(EMAIL_KEY, null))
                        .put(PASSWORD_KEY, preferences.getString(PASSWORD_KEY, null))
                        .put(LANGUAGE_KEY, preferences.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE))
                )
                requester = PandoroRequester(
                    host = host!!,
                    userId = userId,
                    userToken = userToken
                )
            } else
                user = User()
        }

        /**
         * Function to init the user credentials
         *
         * @param response: the response of the auth request
         * @param host: the host to used in the requests
         * @param name: the name of the user
         * @param surname: the surname of the user
         * @param email: the email of the user
         * @param password: the password of the user
         * @param language: the language of the user
         */
        override fun initUserSession(
            response: JsonHelper,
            host: String?,
            name: String,
            surname: String,
            email: String?,
            password: String?,
            language: String?
        ) {
            super.initUserSession(response, host, name, surname, email, password, language)
            activeScreen.value = Projects
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        /**
         * Function to store a user value
         *
         * @param key: the key of the value to store
         * @param value: the value to store
         * @param refreshUser: whether refresh the user
         */
        override fun storeUserValue(
            key: String,
            value: String?,
            refreshUser: Boolean
        ) {
            preferences.edit().putString(key, value).apply()
            super.storeUserValue(key, value, refreshUser)
        }

        /**
         * Function to disconnect the user and clear its properties stored
         *
         * No-any params required
         */
        override fun logout() {
            preferences.edit().clear().apply()
            /*notes.clear()*/
            isRefreshing.value = false
            context.startActivity(Intent(context, ConnectActivity::class.java))
        }

    }

}
