package com.tecknobit.pandoro.ui.activities

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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.apimanager.formatters.JsonHelper
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
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_email
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_name
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_password
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_server_address
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_server_secret
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_surname
import com.tecknobit.pandoro.helpers.AndroidRequester
import com.tecknobit.pandoro.helpers.SnackbarLauncher
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.isRefreshing
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.openLink
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.requester
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.components.PandoroTextField
import com.tecknobit.pandoro.ui.screens.NotesScreen.Companion.notes
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.changelogs
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.groups
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.projectsList
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Projects
import com.tecknobit.pandoro.ui.theme.BackgroundLight
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandorocore.helpers.DEFAULT_USER_LANGUAGE
import com.tecknobit.pandorocore.helpers.InputStatus
import com.tecknobit.pandorocore.helpers.InputStatus.WRONG_EMAIL
import com.tecknobit.pandorocore.helpers.InputStatus.WRONG_PASSWORD
import com.tecknobit.pandorocore.helpers.ScreenType
import com.tecknobit.pandorocore.helpers.ScreenType.SignIn
import com.tecknobit.pandorocore.helpers.ScreenType.SignUp
import com.tecknobit.pandorocore.helpers.areCredentialsValid
import com.tecknobit.pandorocore.helpers.isEmailValid
import com.tecknobit.pandorocore.helpers.isNameValid
import com.tecknobit.pandorocore.helpers.isPasswordValid
import com.tecknobit.pandorocore.helpers.isServerAddressValid
import com.tecknobit.pandorocore.helpers.isServerSecretValid
import com.tecknobit.pandorocore.helpers.isSurnameValid
import com.tecknobit.pandorocore.records.structures.PandoroItem.IDENTIFIER_KEY
import com.tecknobit.pandorocore.records.users.PublicUser.EMAIL_KEY
import com.tecknobit.pandorocore.records.users.PublicUser.NAME_KEY
import com.tecknobit.pandorocore.records.users.PublicUser.PASSWORD_KEY
import com.tecknobit.pandorocore.records.users.PublicUser.PROFILE_PIC_KEY
import com.tecknobit.pandorocore.records.users.PublicUser.SURNAME_KEY
import com.tecknobit.pandorocore.records.users.PublicUser.TOKEN_KEY
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
class ConnectActivity : ComponentActivity(), SnackbarLauncher {

    /**
     * **scope** the coroutine to launch the snackbars
     */
    private lateinit var scope: CoroutineScope

    /**
     * **snackbarHostState** the host to launch the snackbars
     */
    private lateinit var snackbarHostState: SnackbarHostState

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
                                                text = "v. 1.0.3",
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
                            var serverAddress by remember { mutableStateOf("") }
                            var serverSecret by remember { mutableStateOf("") }
                            var name by remember { mutableStateOf("") }
                            var surname by remember { mutableStateOf("") }
                            var email by remember { mutableStateOf("") }
                            var password by remember { mutableStateOf("") }
                            PandoroTextField(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(60.dp),
                                textFieldModifier = Modifier.fillMaxWidth(),
                                label = getString(server_address),
                                value = serverAddress,
                                isError = !isServerAddressValid(serverAddress),
                                onValueChange = {
                                    serverAddress = it
                                }
                            )
                            if (!isIsSignIn) {
                                PandoroTextField(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(300.dp)
                                        .height(60.dp),
                                    textFieldModifier = Modifier.fillMaxWidth(),
                                    label = getString(server_secret),
                                    value = serverSecret,
                                    isError = !isServerSecretValid(serverSecret),
                                    onValueChange = {
                                        serverSecret = it
                                    }
                                )
                                PandoroTextField(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(300.dp)
                                        .height(60.dp),
                                    textFieldModifier = Modifier.fillMaxWidth(),
                                    label = getString(string.name),
                                    value = name,
                                    isError = !isNameValid(name),
                                    onValueChange = {
                                        name = it
                                    }
                                )
                                PandoroTextField(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(300.dp)
                                        .height(60.dp),
                                    textFieldModifier = Modifier.fillMaxWidth(),
                                    label = getString(string.surname),
                                    value = surname,
                                    isError = !isSurnameValid(surname),
                                    onValueChange = {
                                        surname = it
                                    }
                                )
                            }
                            PandoroTextField(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(60.dp),
                                textFieldModifier = Modifier.fillMaxWidth(),
                                label = getString(string.email),
                                value = email,
                                isError = !isEmailValid(email),
                                onValueChange = {
                                    email = it.replace(" ", "")
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
                                value = password,
                                isError = !isPasswordValid(password),
                                onValueChange = {
                                    password = it.replace(" ", "")
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
                                        SignUp -> {
                                            if (isServerAddressValid(serverAddress)) {
                                                if(isServerSecretValid(serverSecret)) {
                                                    if (isNameValid(name)) {
                                                        if (isSurnameValid(surname)) {
                                                            checkCredentials(
                                                                serverAddress = serverAddress,
                                                                serverSecret = serverSecret,
                                                                name = name,
                                                                surname = surname,
                                                                email = email,
                                                                password = password
                                                            )
                                                        } else
                                                            showSnack(you_must_insert_a_correct_surname)
                                                    } else
                                                        showSnack(you_must_insert_a_correct_name)
                                                } else {
                                                    showSnack(you_must_insert_a_correct_server_secret)
                                                }
                                            } else
                                                showSnack(you_must_insert_a_correct_server_address)
                                        }

                                        SignIn -> {
                                            if (isServerAddressValid(serverAddress)) {
                                                checkCredentials(
                                                    serverAddress = serverAddress,
                                                    email = email,
                                                    password = password
                                                )
                                            } else
                                                showSnack(you_must_insert_a_correct_server_address)
                                        }
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
                                                name = ""
                                                surname = ""
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
    private fun createTitle(screenType: ScreenType): String {
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
    private fun createMessage(screenType: ScreenType): String {
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
    private fun createTitleLink(screenType: ScreenType): String {
        return when (screenType) {
            SignUp -> context.getString(sign_in)
            SignIn -> context.getString(sign_up)
        }
    }

    /**
     * Function to check the validity of the credentials
     *
     * @param serverAddress: the address of the Pandoro's backend
     * @param serverSecret: the secret of the Pandoro's backend
     * @param name: the name of the user
     * @param surname: the surname of the user
     * @param email: email to check
     * @param password: password to check
     */
    private fun checkCredentials(
        serverAddress: String,
        serverSecret: String? = null,
        name: String = "",
        surname: String = "",
        email: String,
        password: String
    ) {
        val language: String?
        when (areCredentialsValid(email, password)) {
            InputStatus.OK -> {
                requester = AndroidRequester(serverAddress, null, null)
                val response: JsonHelper = if(serverSecret.isNullOrBlank()) {
                    language = ""
                    JsonHelper(requester!!.execSignIn(email, password))
                } else {
                    language = Locale.current.toLanguageTag().substringBefore("-")
                    JsonHelper(requester!!.execSignUp(serverSecret, name, surname, email, password,
                        language))
                }
                if(requester!!.successResponse()) {
                    localAuthHelper.initUserSession(
                        response,
                        serverAddress,
                        name.ifEmpty { response.getString(NAME_KEY) },
                        surname.ifEmpty { response.getString(SURNAME_KEY) },
                        email,
                        password,
                        language.ifEmpty { response.getString(LANGUAGE_KEY) }
                    )
                } else
                    showSnack(requester!!.errorMessage())
            }
            WRONG_PASSWORD -> showSnack(you_must_insert_a_correct_password)
            WRONG_EMAIL -> showSnack(you_must_insert_a_correct_email)
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
                        .put(LANGUAGE_KEY, preferences.getString(LANGUAGE_KEY, DEFAULT_USER_LANGUAGE))
                )
                requester = AndroidRequester(host!!, userId, userToken)
            } else {
                requester = null
                user = User()
            }
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
            projectsList.clear()
            notes.clear()
            changelogs.clear()
            groups.clear()
            isRefreshing.value = false
            context.startActivity(Intent(context, ConnectActivity::class.java))
        }

    }

}
