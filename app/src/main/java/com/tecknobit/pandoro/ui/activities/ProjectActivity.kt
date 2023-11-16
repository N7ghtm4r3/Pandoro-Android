package com.tecknobit.pandoro.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material.icons.twotone.RemoveDone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.RichTooltipState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import com.tecknobit.pandoro.R.drawable.*
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.*
import com.tecknobit.pandoro.helpers.ColoredBorder
import com.tecknobit.pandoro.helpers.SpaceContent
import com.tecknobit.pandoro.helpers.isContentNoteValid
import com.tecknobit.pandoro.toImportFromLibrary.Note
import com.tecknobit.pandoro.toImportFromLibrary.Project
import com.tecknobit.pandoro.toImportFromLibrary.Project.RepositoryPlatform.Github
import com.tecknobit.pandoro.toImportFromLibrary.Update
import com.tecknobit.pandoro.toImportFromLibrary.Update.Status.IN_DEVELOPMENT
import com.tecknobit.pandoro.toImportFromLibrary.Update.Status.PUBLISHED
import com.tecknobit.pandoro.toImportFromLibrary.Update.Status.SCHEDULED
import com.tecknobit.pandoro.ui.activities.GroupActivity.Companion.GROUP_KEY
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.openLink
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.projectDialogs
import com.tecknobit.pandoro.ui.components.PandoroAlertDialog
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.theme.BackgroundLight
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.GREEN_COLOR
import com.tecknobit.pandoro.ui.theme.IceGrayColor
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandoro.ui.theme.YELLOW_COLOR
import com.tecknobit.pandoro.ui.theme.defTypeface
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.tecknobit.pandoro.ui.components.PandoroOutlinedTextField
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

class ProjectActivity : PandoroDataActivity() {

    companion object {

        const val PROJECT_KEY = "project"

    }

    lateinit var project: Project

    private var publishUpdates = arrayListOf<Update>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(PROJECT_KEY, Project::class.java) as Project
        else
            intent.getSerializableExtra(PROJECT_KEY) as Project
        publishUpdates.addAll(project.publishedUpdates)
        setContent {
            val showScheduleUpdate = remember { mutableStateOf(false) }
            projectDialogs.ScheduleUpdate(
                project = project,
                show = showScheduleUpdate
            )
            PandoroTheme {
                Scaffold(
                    topBar = {
                        LargeTopAppBar(
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = PrimaryLight,
                                navigationIconContentColor = White,
                                titleContentColor = White,
                                actionIconContentColor = White
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = { onBackPressedDispatcher.onBackPressed() }
                                ) {
                                    Icon(
                                        imageVector = Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            title = {
                                Column (
                                    verticalArrangement = spacedBy(2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = spacedBy(5.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier.alignBy(LastBaseline),
                                            text = project.name
                                        )
                                        Text(
                                            modifier = Modifier.alignBy(LastBaseline),
                                            text = "v. ${project.version}",
                                            fontSize = 14.sp
                                        )
                                    }
                                    val author = project.author
                                    if(author != null) {
                                        Text(
                                            text = getString(string.author) + " ${author.completeName}",
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            },
                            actions = {
                                val platform = project.repositoryPlatform
                                if (platform != null) {
                                    val isGitHub = platform == Github
                                    var modifier = Modifier.size(
                                        if (isGitHub)
                                            45.dp
                                        else
                                            60.dp
                                    )
                                    if (isGitHub) {
                                        modifier = modifier.padding(
                                            top = 10.dp,
                                            end = 10.dp
                                        )
                                    }
                                    IconButton(
                                        modifier = modifier,
                                        onClick = { openLink(project.projectRepo) }
                                    ) {
                                        Image(
                                            painter = painterResource(
                                                if (isGitHub)
                                                    github
                                                else
                                                    gitlab
                                            ),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showScheduleUpdate.value = true }
                        ) {
                            Icon(
                                imageVector = Default.Add,
                                contentDescription = null
                            )
                        }
                    }
                ) {
                    val showUpdatesSection = remember { mutableStateOf(true) }
                    val showGroupsSection = remember { mutableStateOf(true) }
                    val showChartSection = remember { mutableStateOf(true) }
                    ShowData {
                        item {
                            ShowDescription(
                                description = project.description
                            )
                        }
                        item {
                            CreateHeader(
                                headerTitle = updates,
                                show = showUpdatesSection
                            )
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = getString(last_update) + " ${project.lastUpdateDate}"
                            )
                            SpaceContent()
                        }
                        if (showUpdatesSection.value) {
                            items(project.updates) { update ->
                                val status = update.status
                                val isScheduled = status == SCHEDULED
                                val isInDevelopment = status == IN_DEVELOPMENT
                                val isPublished = status == PUBLISHED
                                val showUpdateInfo = remember { mutableStateOf(false) }
                                pandoroModalSheet.PandoroModalSheet(
                                    show = showUpdateInfo,
                                    title = getString(change_notes) + " ${update.targetVersion}",
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(
                                                top = 10.dp,
                                                start = 5.dp,
                                                end = 5.dp
                                            ),
                                        verticalArrangement = spacedBy(10.dp)
                                    ) {
                                        LazyColumn(
                                            verticalArrangement = spacedBy(10.dp),
                                            contentPadding = PaddingValues(bottom = 10.dp)
                                        ) {
                                            if (isScheduled || isInDevelopment) {
                                                stickyHeader {
                                                    var addNote by remember {
                                                        mutableStateOf(false)
                                                    }
                                                    val contentNote = remember {
                                                        mutableStateOf("")
                                                    }
                                                    Row(
                                                        verticalAlignment = CenterVertically,
                                                        horizontalArrangement = spacedBy(10.dp)
                                                    ) {
                                                        if (!addNote) {
                                                            FloatingActionButton(
                                                                modifier = Modifier.size(40.dp),
                                                                onClick = { addNote = true },
                                                                content = {
                                                                    Icon(Filled.Add, null)
                                                                }
                                                            )
                                                        }
                                                        if (addNote) {
                                                            Row(
                                                                verticalAlignment = CenterVertically,
                                                                horizontalArrangement = spacedBy(10.dp)
                                                            ) {
                                                                PandoroOutlinedTextField(
                                                                    modifier = Modifier.weight(6f),
                                                                    label = content,
                                                                    isError = !isContentNoteValid(
                                                                        contentNote.value
                                                                    ),
                                                                    value = contentNote,
                                                                    requiredTextArea = true
                                                                )
                                                                Column(
                                                                    modifier = Modifier.weight(1f),
                                                                    verticalArrangement = spacedBy(
                                                                        10.dp
                                                                    )
                                                                ) {
                                                                    FloatingActionButton(
                                                                        modifier = Modifier.size(40.dp),
                                                                        onClick = {
                                                                            if (isContentNoteValid(
                                                                                    contentNote.value
                                                                                )
                                                                            ) {
                                                                                // TODO: MAKE REQUEST THEN
                                                                                addNote = false
                                                                                contentNote.value =
                                                                                    ""
                                                                            } else {
                                                                                pandoroModalSheet
                                                                                    .showSnack(
                                                                                        insert_a_correct_content
                                                                                    )
                                                                            }
                                                                        },
                                                                        containerColor = GREEN_COLOR,
                                                                        contentColor = White,
                                                                        content = {
                                                                            Icon(
                                                                                imageVector = Filled.Done,
                                                                                contentDescription = null
                                                                            )
                                                                        }
                                                                    )
                                                                    FloatingActionButton(
                                                                        modifier = Modifier.size(40.dp),
                                                                        onClick = {
                                                                            addNote = false
                                                                            contentNote.value = ""
                                                                        },
                                                                        containerColor = ErrorLight,
                                                                        contentColor = White,
                                                                        content = {
                                                                            Icon(
                                                                                imageVector = Filled.Cancel,
                                                                                contentDescription = null
                                                                            )
                                                                        }
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            items(update.notes) { note ->
                                                val markedAsDone = remember {
                                                    mutableStateOf(note.isMarkedAsDone)
                                                }
                                                if (isInDevelopment) {
                                                    SwipeableActionsBox(
                                                        swipeThreshold = 200.dp,
                                                        startActions = listOf(
                                                            SwipeAction(
                                                                icon = rememberVectorPainter(
                                                                    if (markedAsDone.value)
                                                                        Icons.TwoTone.RemoveDone
                                                                    else
                                                                        Icons.TwoTone.Done
                                                                ),
                                                                background =
                                                                if (markedAsDone.value)
                                                                    ErrorLight
                                                                else
                                                                    GREEN_COLOR,
                                                                onSwipe = {
                                                                    if (markedAsDone.value) {
                                                                        // TODO: MAKE REQUEST THEN
                                                                    } else {
                                                                        // TODO: MAKE REQUEST THEN
                                                                    }
                                                                }
                                                            )
                                                        )
                                                    ) {
                                                        ChangeNoteCard(
                                                            note = note,
                                                            markedAsDone = markedAsDone,
                                                            isScheduled = false,
                                                            isInDevelopment = true
                                                        )
                                                    }
                                                } else {
                                                    ChangeNoteCard(
                                                        note = note,
                                                        markedAsDone = markedAsDone,
                                                        isScheduled = isScheduled,
                                                        isInDevelopment = false
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                PandoroCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(15.dp),
                                    onClick = { showUpdateInfo.value = true }
                                ) {
                                    var showOptions by remember { mutableStateOf(false) }
                                    val showDeleteDialog = remember { mutableStateOf(false) }
                                    PandoroAlertDialog(
                                        show = showDeleteDialog,
                                        title = delete_update,
                                        extraTitle = update.targetVersion,
                                        text = delete_update_text,
                                        requestLogic = {
                                            runBlocking {
                                                launch {
                                                    deleteUpdate(update)
                                                    showDeleteDialog.value = false
                                                }
                                            }
                                        }
                                    )
                                    Row(
                                        modifier = Modifier.height(IntrinsicSize.Min)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .weight(14f)
                                                .padding(
                                                    top = 10.dp,
                                                    start = 16.dp,
                                                    bottom = 10.dp
                                                )
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = CenterVertically
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .weight(3f)
                                                        .fillMaxWidth(),
                                                    text = "v. ${update.targetVersion}",
                                                    fontSize = 18.sp
                                                )
                                                Column(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxWidth(),
                                                    horizontalAlignment = Alignment.End
                                                ) {
                                                    if (showOptions) {
                                                        DropdownMenu(
                                                            modifier = Modifier.background(White),
                                                            expanded = showOptions,
                                                            onDismissRequest = {
                                                                showOptions = false
                                                            }
                                                        ) {
                                                            DropdownMenuItem(
                                                                text = {
                                                                    Text(
                                                                        text =
                                                                        if (isScheduled)
                                                                            getString(start_update)
                                                                        else
                                                                            getString(publish)
                                                                    )
                                                                },
                                                                trailingIcon = {
                                                                    Icon(
                                                                        imageVector =
                                                                        if (isScheduled)
                                                                            Default.PlayArrow
                                                                        else
                                                                            Default.Publish,
                                                                        contentDescription = null,
                                                                        tint =
                                                                        if (isScheduled)
                                                                            YELLOW_COLOR
                                                                        else
                                                                            GREEN_COLOR
                                                                    )
                                                                },
                                                                onClick = {
                                                                    if (isScheduled) {
                                                                        // TODO: MAKE REQUEST THEN
                                                                    } else {
                                                                        // TODO: BEFORE THE REQUEST CHECK
                                                                        //  IF ALL THE NOTES ARE MARKED AS DONE
                                                                        // AND WARN THE USER ON THAT IF NOT
                                                                        // TODO: MAKE REQUEST THEN
                                                                    }
                                                                    showOptions = false
                                                                }
                                                            )
                                                            DropdownMenuItem(
                                                                text = {
                                                                    Text(
                                                                        text = getString(
                                                                            delete
                                                                        )
                                                                    )
                                                                },
                                                                trailingIcon = {
                                                                    Icon(
                                                                        imageVector = Default.Delete,
                                                                        contentDescription = null,
                                                                        tint = ErrorLight
                                                                    )
                                                                },
                                                                onClick = {
                                                                    showOptions = false
                                                                    showDeleteDialog.value = true
                                                                }
                                                            )
                                                        }
                                                    }
                                                    IconButton(
                                                        modifier = Modifier.size(22.dp),
                                                        onClick = {
                                                            if (isPublished)
                                                                showDeleteDialog.value = true
                                                            else
                                                                showOptions = true
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = if (isPublished)
                                                                Default.Delete
                                                            else
                                                                Default.MoreVert,
                                                            contentDescription = null,
                                                            tint = if (isPublished)
                                                                ErrorLight
                                                            else
                                                                PrimaryLight,
                                                        )
                                                    }
                                                }
                                            }
                                            Column(
                                                modifier = Modifier.padding(end = 10.dp)
                                            ) {
                                                Text(
                                                    text = getString(update_id) + " ${update.id}",
                                                )
                                                SpaceContent()
                                                Column(
                                                    modifier = Modifier.padding(top = 5.dp),
                                                ) {
                                                    val updateAuthor = update.author
                                                    if (updateAuthor != null) {
                                                        Text(
                                                            text = getString(author)
                                                                    + " ${updateAuthor.completeName}",
                                                        )
                                                    }
                                                    Text(
                                                        text = getString(creation_date)
                                                                + " ${update.createDate}",
                                                    )
                                                    SpaceContent()
                                                }
                                                if (isInDevelopment || isPublished) {
                                                    Column(
                                                        modifier = Modifier.padding(top = 5.dp),
                                                    ) {
                                                        val scheduledBy = update.startedBy
                                                        if (scheduledBy != null) {
                                                            Text(
                                                                text = getString(started_by)
                                                                        + " ${scheduledBy.completeName}",
                                                            )
                                                        }
                                                        Text(
                                                            text = getString(start_date)
                                                                    + " ${update.startDate}",
                                                        )
                                                        SpaceContent()
                                                    }
                                                }
                                                if (isPublished) {
                                                    Column(
                                                        modifier = Modifier.padding(top = 5.dp),
                                                    ) {
                                                        val publishedBy = update.publishedBy
                                                        if (publishedBy != null) {
                                                            Text(
                                                                text = getString(published_by)
                                                                        + " ${publishedBy.completeName}",
                                                            )
                                                        }
                                                        Text(
                                                            text = getString(publish_date)
                                                                    + " ${update.publishDate}",
                                                        )
                                                        SpaceContent()
                                                        Row(
                                                            modifier = Modifier.padding(top = 5.dp),
                                                            horizontalArrangement = spacedBy(5.dp),
                                                            verticalAlignment = CenterVertically
                                                        ) {
                                                            val duration =
                                                                update.developmentDuration
                                                            Text(
                                                                text = getString(
                                                                    development_duration
                                                                )
                                                                        + " $duration",
                                                            )
                                                            val time =
                                                                if (duration == 1)
                                                                    day
                                                                else
                                                                    days
                                                            Text(
                                                                text = getString(time),
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            ColoredBorder(
                                                if (isScheduled)
                                                    ErrorLight
                                                else if (isInDevelopment)
                                                    YELLOW_COLOR
                                                else
                                                    GREEN_COLOR
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            ShowItemsList(
                                show = showGroupsSection,
                                headerTitle = groups,
                                itemsList = project.groups,
                                key = GROUP_KEY,
                                clazz = GroupActivity::class.java
                            )
                        }
                        if (publishUpdates.isNotEmpty()) {
                            item {
                                CreateHeader(
                                    headerTitle = stats,
                                    show = showChartSection
                                )
                                Text(
                                    modifier = Modifier.padding(top = 5.dp),
                                    text = getString(total_development_days)
                                            + " ${project.totalDevelopmentDays}"
                                )
                                val avgDevelopmentTime = project.averageDevelopmentTime
                                val temporal =
                                    if (avgDevelopmentTime > 0)
                                        days
                                    else
                                        day
                                Text(
                                    modifier = Modifier.padding(top = 5.dp),
                                    text = getString(average_development_time) + ": $avgDevelopmentTime "
                                            + getString(temporal)

                                )
                                SpaceContent()
                            }
                            if (showChartSection.value) {
                                item {
                                    ShowStats()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun deleteUpdate(update: Update) {
        // TODO: MAKE REQUEST THEN
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeNoteCard(
        note: Note,
        markedAsDone: MutableState<Boolean>,
        isScheduled: Boolean,
        isInDevelopment: Boolean
    ) {
        val tooltipState = remember { RichTooltipState() }
        RichTooltipBox(
            tooltipState = tooltipState,
            colors = TooltipDefaults.richTooltipColors(
                containerColor = IceGrayColor
            ),
            title = { Text(text = getString(note_info)) },
            text = {
                val author = note.author
                val authorIsNotNull = author != null
                Column(
                    verticalArrangement = spacedBy(5.dp)
                ) {
                    if (authorIsNotNull) {
                        Text(
                            text = getString(string.author) + " ${author.completeName}"
                        )
                    }
                    Text(
                        text = stringResource(creation_date) + " ${note.creationDate}",
                    )
                    if (markedAsDone.value) {
                        if (authorIsNotNull) {
                            Text(
                                text = stringResource(marked_as_done_by) + " ${author.completeName}"
                            )
                        }
                        Text(
                            text = stringResource(date_of_mark) + " ${note.markedAsDoneDate}",
                        )
                    }
                }
            },
            action = {
                TextButton(
                    onClick = { coroutine.launch { tooltipState.dismiss() } }
                ) {
                    Text(
                        text = getString(dismiss)
                    )
                }
            },
            content = {}
        )
        PandoroCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { coroutine.launch { tooltipState.show() } }
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .weight(0.5f),
                    text = "-"
                )
                Text(
                    modifier = Modifier.weight(8f),
                    text = note.content,
                    textAlign = TextAlign.Justify,
                    textDecoration =
                    if (markedAsDone.value)
                        LineThrough
                    else
                        null
                )
                if (isScheduled || isInDevelopment) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(
                            onClick = {
                                // TODO: MAKE REQUEST THEN
                            }
                        ) {
                            Icon(
                                imageVector = Default.Delete,
                                contentDescription = null,
                                tint = ErrorLight
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowStats() {
        val barData = arrayListOf<BarData>()
        publishUpdates.forEachIndexed { index, update ->
            val y = update.developmentDuration.toFloat()
            barData.add(
                BarData(
                    point = Point(
                        x = index.toFloat(),
                        y = y
                    ),
                    color = ErrorLight,
                    label = "v. ${update.targetVersion}",
                )
            )
        }
        barData.reverse()
        val xAxisData = AxisData.Builder()
            .axisStepSize(25.dp)
            .axisLabelFontSize(12.sp)
            .axisLabelAngle(20f)
            .startDrawPadding(20.dp)
            .labelData { index -> barData[index].label }
            .backgroundColor(BackgroundLight)
            .typeFace(defTypeface)
            .build()
        val barChartData = BarChartData(
            chartData = barData,
            paddingEnd = 0.dp,
            paddingTop = 10.dp,
            xAxisData = xAxisData,
            barStyle = BarStyle(
                paddingBetweenBars = 20.dp,
                barWidth = 25.dp,
                selectionHighlightData = SelectionHighlightData(
                    highlightTextBackgroundColor = Color.Transparent,
                    highlightTextTypeface = defTypeface,
                    popUpLabel = { _, y ->
                        val temporal = if(y > 0)
                            days
                        else
                            day
                        "${y.toInt()} " + getString(temporal)
                    }
                )
            ),
            showXAxis = true,
            backgroundColor = BackgroundLight
        )
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BarChart(
                modifier = Modifier.height(350.dp),
                barChartData = barChartData
            )
        }
    }

}
