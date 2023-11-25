package com.tecknobit.pandoro.helpers

import com.tecknobit.apimanager.annotations.RequestPath
import com.tecknobit.apimanager.apis.APIRequest
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.controllers.UsersController.BASE_ENDPOINT
import com.tecknobit.pandoro.controllers.UsersController.CHANGE_PROFILE_PIC_ENDPOINT
import com.tecknobit.pandoro.controllers.UsersController.IDENTIFIER_KEY
import com.tecknobit.pandoro.controllers.UsersController.USERS_ENDPOINT
import com.tecknobit.pandoro.services.UsersHelper.PROFILE_PIC_KEY
import com.tecknobit.pandoro.services.UsersHelper.TOKEN_KEY
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File


/**
 * The **AndroidRequester** class is useful to communicate with the Pandoro's backend
 *
 * @param host: the host where is running the Pandoro's backend
 * @param userId: the user identifier
 * @param userToken: the user token
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class AndroidRequester(
    private val host: String,
    override var userId: String?,
    override var userToken: String?
): BaseRequester(host, userId, userToken) {

    private val okHttpClient = OkHttpClient()

    private val headers = mutableMapOf<String, String>()

    init {
        setAuthHeaders()
    }

    /**
     * Function to set the headers for the authentication of the user
     *
     * No-any params required
     */
    override fun setAuthHeaders() {
        if (userId != null && userToken != null) {
            headers[IDENTIFIER_KEY] = userId!!
            headers[TOKEN_KEY] = userToken!!
        }
    }

    /**
     * Function to execute the request to change the profile pic of the user
     *
     * @param profilePic: the profile pic of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changeProfilePic", method = APIRequest.RequestMethod.POST)
    override fun execChangeProfilePic(profilePic: File): JSONObject {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                PROFILE_PIC_KEY,
                profilePic.name,
                profilePic.readBytes().toRequestBody("image/*".toMediaType())
            )
            .build()
        val request: Request = Request.Builder()
            .headers(headers.toHeaders())
            .url("$host$BASE_ENDPOINT$USERS_ENDPOINT/$userId/$CHANGE_PROFILE_PIC_ENDPOINT")
            .post(body)
            .build()
        val client = OkHttpClient()
        var response: JSONObject? = null
        runBlocking {
            async {
                response = client.newCall(request).execute().body?.string()?.let { JSONObject(it) }
                lastResponse = JsonHelper(response)
            }.await()
        }
        return response!!
    }

    /**
     * Function to execute a request to the backend
     *
     * @param contentType: the content type of the request
     * @param endpoint: the endpoint which make the request
     * @param requestMethod: the method of the request to execute
     * @param payload: the payload of the request, default null
     * @param jsonPayload: whether the payload must be formatted as JSON, default true
     */
    override fun execRequest(
        contentType: String,
        endpoint: String,
        requestMethod: APIRequest.RequestMethod,
        payload: PandoroPayload?,
        jsonPayload: Boolean
    ): String {
        var response: String? = null
        runBlocking {
            async { response = asyncRequestExecution(contentType, endpoint, requestMethod, payload,
                jsonPayload) }.await()
        }
        return response!!
    }

    /**
     * Function to execute a request to the backend and wait for the response
     *
     * @param contentType: the content type of the request
     * @param endpoint: the endpoint which make the request
     * @param requestMethod: the method of the request to execute
     * @param payload: the payload of the request, default null
     * @param jsonPayload: whether the payload must be formatted as JSON, default true
     */
    private fun asyncRequestExecution(
        contentType: String,
        endpoint: String,
        requestMethod: APIRequest.RequestMethod,
        payload: PandoroPayload?,
        jsonPayload: Boolean
    ): String {
        headers["Content-Type"] = contentType
        return try {
            val requestUrl = host + BASE_ENDPOINT + endpoint
            var rPayload: RequestBody? = null
            if(payload != null) {
                val paramsMap = payload.getPayload()
                rPayload = if(jsonPayload)
                    JSONObject(paramsMap).toString().toRequestBody(contentType.toMediaType())
                else {
                    val contentPayload = FormBody.Builder()
                    paramsMap.keys.forEach { key ->
                        contentPayload.add(key, paramsMap[key].toString())
                    }
                    contentPayload.build()
                }
            }
            val request: Request = Request.Builder()
                .headers(headers.toHeaders())
                .url(requestUrl)
                .method(requestMethod.name, rPayload)
                .build()
            val call: Call = okHttpClient.newCall(request)
            val response: String = call.execute().body!!.string()
            lastResponse = JsonHelper(response)
            response
        } catch (e: Exception) {
            lastResponse = JsonHelper(errorResponse)
            errorResponse
        }
    }

}