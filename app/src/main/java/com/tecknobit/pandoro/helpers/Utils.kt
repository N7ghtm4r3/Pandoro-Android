package com.tecknobit.pandoro.helpers

import android.graphics.BitmapFactory.decodeStream
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.pandoro.services.UsersHelper.DEFAULT_PROFILE_PIC
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.localAuthHelper
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Method to create a [Divider] on the UI
 *
 * No any params required
 */
@Composable
fun Divide() {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp
    )
}

/**
 * Method to space the content of the UI and create a [Divider]
 *
 * @param height: the height of the [Spacer]
 */
@Composable
fun SpaceContent(height: Int = 10) {
    Spacer(
        modifier = Modifier.height(height.dp)
    )
    Divide()
}

/**
 * Method to color the border of a [Box]
 *
 * @param color: the color for the border
 */
@Composable
fun ColoredBorder(color: Color) {
    Box(
        modifier = Modifier
            .background(color)
            .fillMaxHeight()
            .width(10.dp),
        content = {
            Text(
                text = ""
            )
        }
    )
}

/**
 * Function to load an image from an ul
 *
 * No-any params required
 */
@Wrapper
fun loadImageBitmap(): ImageBitmap {
    return loadImageBitmap(user.profilePic)
}

/**
 * Function to load an image from an ul
 *
 * @param url: the url from load the image
 */
fun loadImageBitmap(url: String): ImageBitmap {
    var iUrl = url
    if (!iUrl.startsWith(localAuthHelper.host!!))
        iUrl = localAuthHelper.host + "/$url"
    if (iUrl.startsWith("https")) {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _: String?, _: SSLSession? -> true }
        } catch (ignored: Exception) {
        }
    }
    var imageBitmap: ImageBitmap? = null
    runBlocking {
        async {
            imageBitmap = try {
                decodeStream(URL(iUrl).openConnection().getInputStream()).asImageBitmap()
            } catch (e: Exception) {
                decodeStream(URL(localAuthHelper.host!! + "/" + DEFAULT_PROFILE_PIC).openConnection()
                    .getInputStream()).asImageBitmap()
            }
        }.await()
    }
    return imageBitmap!!
}