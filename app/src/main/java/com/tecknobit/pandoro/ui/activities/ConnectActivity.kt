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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.*
import com.tecknobit.pandoro.helpers.InputStatus.*
import com.tecknobit.pandoro.helpers.ScreenType
import com.tecknobit.pandoro.helpers.ScreenType.SignIn
import com.tecknobit.pandoro.helpers.ScreenType.SignUp
import com.tecknobit.pandoro.helpers.SnackbarLauncher
import com.tecknobit.pandoro.helpers.areCredentialsValid
import com.tecknobit.pandoro.helpers.isEmailValid
import com.tecknobit.pandoro.helpers.isNameValid
import com.tecknobit.pandoro.helpers.isPasswordValid
import com.tecknobit.pandoro.helpers.isServerAddressValid
import com.tecknobit.pandoro.helpers.isSurnameValid
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.openLink
import com.tecknobit.pandoro.ui.theme.BackgroundLight
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import kotlinx.coroutines.CoroutineScope
import com.tecknobit.pandoro.ui.components.PandoroTextField

class ConnectActivity : ComponentActivity(), SnackbarLauncher {

    private lateinit var scope: CoroutineScope

    private lateinit var snackbarHostState: SnackbarHostState

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
                                                text = "v. 1.0.0",
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
                            var name by remember { mutableStateOf("") }
                            var surname by remember { mutableStateOf("") }
                            var email by remember { mutableStateOf("") }
                            var password by remember { mutableStateOf("") }
                            PandoroTextField(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(55.dp),
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
                                        .height(55.dp),
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
                                        .height(55.dp),
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
                                    .height(55.dp),
                                textFieldModifier = Modifier.fillMaxWidth(),
                                label = getString(string.email),
                                value = email,
                                isError = !isEmailValid(email),
                                onValueChange = {
                                    email = it
                                }
                            )
                            PandoroTextField(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(300.dp)
                                    .height(55.dp),
                                textFieldModifier = Modifier.fillMaxWidth(),
                                label = getString(string.password),
                                value = password,
                                isError = !isPasswordValid(password),
                                onValueChange = {
                                    password = it
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
                                                if (isNameValid(name)) {
                                                    if (isSurnameValid(surname))
                                                        checkCredentials(email, password)
                                                    else
                                                        showSnack(you_must_insert_a_correct_surname)
                                                } else
                                                    showSnack(you_must_insert_a_correct_name)
                                            } else
                                                showSnack(you_must_insert_a_correct_server_address)
                                        }

                                        SignIn -> {
                                            if (isServerAddressValid(serverAddress))
                                                checkCredentials(email, password)
                                            else
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
                                            if (screenType == SignUp)
                                                SignIn
                                            else
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

    private fun checkCredentials(email: String, password: String) {
        when (areCredentialsValid(email, password)) {
            OK -> {
                // TODO: MAKE REQUEST THEN
                startActivity(Intent(context, MainActivity::class.java))
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
    override fun showSnack(message: Int) {
        showSnack(
            scope = scope,
            snackbarHostState = snackbarHostState,
            message = message
        )
    }

}
