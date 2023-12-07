package com.tecknobit.pandoro.helpers

import com.tecknobit.apimanager.annotations.RequestPath
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandoro.controllers.UsersController.BASE_ENDPOINT
import com.tecknobit.pandoro.controllers.UsersController.CHANGE_PROFILE_PIC_ENDPOINT
import com.tecknobit.pandoro.controllers.UsersController.USERS_ENDPOINT
import com.tecknobit.pandoro.services.UsersHelper.PROFILE_PIC_KEY
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


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
): Requester(host, userId, userToken) {

    init {
        setAuthHeaders()
    }

    /**
     * Function to execute the request to change the profile pic of the user
     *
     * @param profilePic: the profile pic of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{id}/changeProfilePic", method = RequestMethod.POST)
    override fun execChangeProfilePic(profilePic: File): JSONObject {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                PROFILE_PIC_KEY,
                profilePic.name,
                profilePic.readBytes().toRequestBody("image/*".toMediaType())
            )
            .build()
        val mHeaders = mutableMapOf<String, String>()
        headers.headersKeys.forEach { headerKey ->
            mHeaders[headerKey] = headers.getHeader(headerKey)
        }
        val request: Request = Request.Builder()
            .headers(mHeaders.toHeaders())
            .url("$host$BASE_ENDPOINT$USERS_ENDPOINT/$userId$CHANGE_PROFILE_PIC_ENDPOINT")
            .post(body)
            .build()
        val client = validateSelfSignedCertificate(OkHttpClient())
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
     * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
     * No-any params required
     *
     * @apiNote this method disable all checks on the SLL certificate validity, so is recommended to use for test only or
     * in a private distribution on own infrastructure
     */
    private fun validateSelfSignedCertificate(okHttpClient: OkHttpClient): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
        val builder = okHttpClient.newBuilder()
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            builder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier({ hostname: String?, session: SSLSession? -> true })
        } catch (ignored: java.lang.Exception) {
        }finally {
            return builder.build()
        }
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
        requestMethod: RequestMethod,
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
        requestMethod: RequestMethod,
        payload: PandoroPayload?,
        jsonPayload: Boolean
    ): String {
        headers.addHeader("Content-Type", contentType)
        if(host.startsWith("https"))
            apiRequest.validateSelfSignedCertificate()
        return try {
            val requestUrl = host + BASE_ENDPOINT + endpoint
            if (payload != null) {
                payload.paramsKeys.forEach { key ->
                    val param = payload.getParam<Any>(key)
                    if(param is List<*>)
                        payload.addParam(key, JSONArray(param))
                }
                if (jsonPayload)
                    apiRequest.sendJSONPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
                else
                    apiRequest.sendPayloadedAPIRequest(requestUrl, requestMethod, headers, payload)
            } else
                apiRequest.sendAPIRequest(requestUrl, requestMethod, headers)
            val response = apiRequest.response
            lastResponse = JsonHelper(response)
            response
        } catch (e: Exception) {
            lastResponse = JsonHelper(errorResponse)
            errorResponse
        }
    }

}