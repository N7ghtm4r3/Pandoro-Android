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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.toImportFromLibrary.User
import com.tecknobit.pandoro.ui.components.dialogs.GroupDialogs
import com.tecknobit.pandoro.ui.components.dialogs.PandoroModalSheet
import com.tecknobit.pandoro.ui.components.dialogs.ProjectDialogs
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.defTypeface
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {

    companion object {

        val user = User()

        val groupDialogs = GroupDialogs()

        val projectDialogs = ProjectDialogs()

        val pandoroModalSheet = PandoroModalSheet()

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun openLink(url: String) {
            val intent = Intent()
            intent.data = url.toUri()
            intent.action = Intent.ACTION_VIEW
            startActivity(context, intent, null)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        setContent {
            PandoroTheme {
                context = LocalContext.current
                defTypeface = ResourcesCompat.getFont(LocalContext.current, R.font.rem)!!
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
                // TODO: CREATE THE REAL WORKFLOW
                startActivity(Intent(this@SplashScreen, ConnectActivity::class.java))
            }
        }
    }

}