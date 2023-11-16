package com.tecknobit.pandoro.ui.activities

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.helpers.ColoredBorder
import com.tecknobit.pandoro.helpers.SpaceContent
import com.tecknobit.pandoro.toImportFromLibrary.PandoroItem
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.context
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.components.dialogs.PandoroDialog
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import kotlinx.coroutines.CoroutineScope

/**
 * The **PandoroDataActivity** class is useful to create an activity with the behavior to show the UI
 * data
 *
 * @see ComponentActivity
 */
@Structure
abstract class PandoroDataActivity : ComponentActivity() {

    /**
     * **coroutine** the coroutine to launch the snackbars
     */
    protected lateinit var coroutine: CoroutineScope

    /**
     * Function to show the data
     *
     * @param content: the content to show
     */
    @Composable
    protected fun ShowData(
        content: LazyListScope.() -> Unit
    ) {
        coroutine = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier.padding(
                top = 168.dp,
                bottom = 16.dp,
                end = 16.dp,
                start = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 10.dp),
            content = content
        )
    }

    /**
     * Function to create an header title
     *
     * @param headerTitle: the header title
     * @param extraIcon: the extra icon to show with the title
     * @param show: whether show the subsection
     * @param showArrow: whether show the arrow to display the subsection
     */
    @Composable
    protected fun CreateHeader(
        headerTitle: Int,
        extraIcon: ExtraIcon? = null,
        show: MutableState<Boolean>,
        showArrow: Boolean = true
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(headerTitle),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if (showArrow) {
                IconButton(
                    onClick = { show.value = !show.value }
                ) {
                    Icon(
                        imageVector = if (show.value) Default.ArrowDropUp else Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            if (extraIcon != null) {
                IconButton(
                    onClick = extraIcon.action
                ) {
                    Icon(
                        imageVector = extraIcon.icon,
                        contentDescription = null
                    )
                }
            }
        }
    }

    /**
     * Function to show the description section
     *
     * @param description: the description to show
     */
    @Composable
    protected open fun ShowDescription(description: String) {
        val showDescription = remember { mutableStateOf(false) }
        val showDescriptionSection = remember { mutableStateOf(true) }
        pandoroModalSheet.PandoroModalSheet(
            show = showDescription,
            title = R.string.description,
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState()),
                text = description,
                textAlign = TextAlign.Justify,
            )
        }
        CreateHeader(
            headerTitle = R.string.description,
            show = showDescriptionSection
        )
        if (showDescriptionSection.value) {
            Text(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clickable { showDescription.value = true },
                text = description,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
        }
        SpaceContent()
    }

    /**
     * Function to show a list of items
     *
     * @param show: whether show the list
     * @param headerTitle: the header title
     * @param extraIcon: the extra icon to show with the title
     * @param itemsList: the list of the items to show
     * @param key: the key value
     * @param clazz: the class value of the items list
     * @param adminPrivileges: whether the user has the admin privileges to execute the own actions
     */
    @Composable
    protected fun <T> ShowItemsList(
        show: MutableState<Boolean>,
        headerTitle: Int,
        extraIcon: ExtraIcon? = null,
        itemsList: List<PandoroItem>,
        key: String,
        clazz: Class<T>,
        adminPrivileges: Boolean = false
    ) {
        val itemsListFill = itemsList.isNotEmpty()
        if (itemsListFill || adminPrivileges) {
            CreateHeader(
                headerTitle = headerTitle,
                extraIcon = extraIcon,
                show = show,
                showArrow = itemsListFill
            )
            SpaceContent()
            Spacer(modifier = Modifier.height(10.dp))
            if (show.value) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    modifier = Modifier
                        .height(130.dp)
                        .padding(
                            start = 1.dp,
                            end = 1.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(itemsList) { item ->
                        PandoroCard(
                            modifier = Modifier.size(
                                width = 130.dp,
                                height = 65.dp
                            ),
                            onClick = {
                                val intent = Intent(context, clazz)
                                intent.putExtra(key, item)
                                ContextCompat.startActivity(context, intent, null)
                            }
                        ) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(10f)
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = item.name,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    ColoredBorder(color = PrimaryLight)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * The **ExtraIcon** class is useful to pair an icon and its action
     */
    protected data class ExtraIcon(
        val action: () -> Unit,
        val icon: ImageVector
    )

}