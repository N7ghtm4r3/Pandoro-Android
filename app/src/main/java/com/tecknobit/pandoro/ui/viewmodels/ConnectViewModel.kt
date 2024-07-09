package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinox.environment.records.EquinoxUser
import com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY
import com.tecknobit.equinox.environment.records.EquinoxUser.SURNAME_KEY
import com.tecknobit.equinox.inputs.InputValidator.isNameValid
import com.tecknobit.equinox.inputs.InputValidator.isServerSecretValid
import com.tecknobit.equinox.inputs.InputValidator.isSurnameValid
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_email
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_name
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_password
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_server_address
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_server_secret
import com.tecknobit.pandoro.R.string.you_must_insert_a_correct_surname
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.areCredentialsValid
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isServerAddressValid
import com.tecknobit.pandorocore.helpers.InputsValidator.InputStatus.OK
import com.tecknobit.pandorocore.helpers.InputsValidator.InputStatus.WRONG_EMAIL
import com.tecknobit.pandorocore.helpers.InputsValidator.InputStatus.WRONG_PASSWORD
import com.tecknobit.pandorocore.helpers.PandoroRequester
import com.tecknobit.pandorocore.records.users.User.LANGUAGE_KEY

class ConnectViewModel(
    snackbarHostState: SnackbarHostState
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    lateinit var serverAddress: MutableState<String>

    lateinit var serverSecret: MutableState<String>

    lateinit var name: MutableState<String>

    lateinit var surname: MutableState<String>

    lateinit var email: MutableState<String>

    lateinit var password: MutableState<String>

    fun signUp() {
        if (isServerAddressValid(serverAddress.value)) {
            if(isServerSecretValid(serverSecret.value)) {
                if (isNameValid(name.value)) {
                    if (isSurnameValid(surname.value)) {
                        checkCredentials(
                            serverAddress = serverAddress.value,
                            serverSecret = serverSecret.value,
                            name = name.value,
                            surname = surname.value,
                            email = email.value,
                            password = password.value
                        )
                    } else
                        showSnack(you_must_insert_a_correct_surname)
                } else
                    showSnack(you_must_insert_a_correct_name)
            } else
                showSnack(you_must_insert_a_correct_server_secret)
        } else
            showSnack(you_must_insert_a_correct_server_address)
    }

    fun signIn() {
        if (isServerAddressValid(serverAddress.value)) {
            checkCredentials(
                serverAddress = serverAddress.value,
                email = email.value,
                password = password.value
            )
        } else
            showSnack(you_must_insert_a_correct_server_address)
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
        var language = ""
        when (areCredentialsValid(email, password)) {
            OK -> {
                requester = PandoroRequester(
                    host = serverAddress,
                    userId = null,
                    userToken = null
                )
                requester.sendRequest(
                    request = {
                        if(serverSecret.isNullOrBlank()) {
                            requester.signIn(
                                email = email,
                                password = password
                            )
                        } else {
                            language = EquinoxUser.getValidUserLanguage()
                            requester.signUp(
                                serverSecret = serverSecret,
                                name = name,
                                surname = surname,
                                email = email,
                                password = password,
                                language = language
                            )
                        }
                    },
                    onSuccess = { response ->
                        localAuthHelper.initUserSession(
                            response,
                            serverAddress,
                            name.ifEmpty { response.getString(NAME_KEY) },
                            surname.ifEmpty { response.getString(SURNAME_KEY) },
                            email,
                            password,
                            language.ifEmpty { response.getString(LANGUAGE_KEY) }
                        )
                    },
                    onFailure = { showSnack(it) }
                )
            }
            WRONG_PASSWORD -> showSnack(you_must_insert_a_correct_password)
            WRONG_EMAIL -> showSnack(you_must_insert_a_correct_email)
        }
    }

}