package com.tecknobit.pandoro.ui.activities.session

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material.icons.twotone.RemoveDone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
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
import com.tecknobit.pandoro.R
import com.tecknobit.pandoro.R.drawable.github
import com.tecknobit.pandoro.R.drawable.gitlab
import com.tecknobit.pandoro.R.string
import com.tecknobit.pandoro.R.string.author
import com.tecknobit.pandoro.R.string.average_development_time
import com.tecknobit.pandoro.R.string.change_notes
import com.tecknobit.pandoro.R.string.check_change_notes_message
import com.tecknobit.pandoro.R.string.content
import com.tecknobit.pandoro.R.string.copy_note
import com.tecknobit.pandoro.R.string.creation_date
import com.tecknobit.pandoro.R.string.date_of_mark
import com.tecknobit.pandoro.R.string.day
import com.tecknobit.pandoro.R.string.days
import com.tecknobit.pandoro.R.string.delete
import com.tecknobit.pandoro.R.string.delete_update
import com.tecknobit.pandoro.R.string.delete_update_text
import com.tecknobit.pandoro.R.string.development_duration
import com.tecknobit.pandoro.R.string.dismiss
import com.tecknobit.pandoro.R.string.insert_a_correct_content
import com.tecknobit.pandoro.R.string.last_update
import com.tecknobit.pandoro.R.string.marked_as_done_by
import com.tecknobit.pandoro.R.string.not_all_the_change_notes_are_done
import com.tecknobit.pandoro.R.string.note_info
import com.tecknobit.pandoro.R.string.publish
import com.tecknobit.pandoro.R.string.publish_date
import com.tecknobit.pandoro.R.string.published_by
import com.tecknobit.pandoro.R.string.start_date
import com.tecknobit.pandoro.R.string.start_update
import com.tecknobit.pandoro.R.string.started_by
import com.tecknobit.pandoro.R.string.stats
import com.tecknobit.pandoro.R.string.total_development_days
import com.tecknobit.pandoro.R.string.update_id
import com.tecknobit.pandoro.R.string.updates
import com.tecknobit.pandoro.helpers.ColoredBorder
import com.tecknobit.pandoro.helpers.SpaceContent
import com.tecknobit.pandoro.helpers.copyContent
import com.tecknobit.pandoro.helpers.copyNote
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.openLink
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.reviewManager
import com.tecknobit.pandoro.ui.components.CreateSnackbarHost
import com.tecknobit.pandoro.ui.components.PandoroAlertDialog
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.components.PandoroOutlinedTextField
import com.tecknobit.pandoro.ui.screens.ProfileScreen.Companion.groups
import com.tecknobit.pandoro.ui.screens.ProjectsScreen.Companion.projectDialogs
import com.tecknobit.pandoro.ui.screens.Screen.Companion.currentProject
import com.tecknobit.pandoro.ui.theme.BackgroundLight
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.GREEN_COLOR
import com.tecknobit.pandoro.ui.theme.IceGrayColor
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandoro.ui.theme.YELLOW_COLOR
import com.tecknobit.pandoro.ui.theme.defTypeface
import com.tecknobit.pandoro.ui.viewmodels.ProjectActivityViewModel
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.areAllChangeNotesDone
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isContentNoteValid
import com.tecknobit.pandorocore.records.Note
import com.tecknobit.pandorocore.records.Project
import com.tecknobit.pandorocore.records.Project.RepositoryPlatform.Github
import com.tecknobit.pandorocore.records.ProjectUpdate
import com.tecknobit.pandorocore.records.ProjectUpdate.Status.IN_DEVELOPMENT
import com.tecknobit.pandorocore.records.ProjectUpdate.Status.PUBLISHED
import com.tecknobit.pandorocore.records.ProjectUpdate.Status.SCHEDULED
import com.tecknobit.pandorocore.ui.formatNotesAsMarkdown
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


/**
 * The **ProjectActivity** class is useful to create the activity to show the [Project] details
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see PandoroDataActivity
 */
// TODO: TO COMMENT
class ProjectActivity : PandoroDataActivity() {

    /**
     * **project** the project to show its details
     */
    private lateinit var project: Project

    /**
     * **hasGroup** -> whether the projects has group
     */
    private var hasGroup: Boolean = false

    /**
     * **showDeleteDialog** -> whether show the delete dialog to delete an update
     */
    private lateinit var showDeleteDialog: MutableState<Boolean>

    private val viewModel = ProjectActivityViewModel(
        initialProject = currentProject.value!!,
        snackbarHostState = snackbarHostState
    )

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
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.refreshProject {
                hasGroup = project.hasGroups()
            }
            project = viewModel.project.collectAsState().value
            showDeleteDialog = remember { mutableStateOf(false) }
            val showScheduleUpdate = remember { mutableStateOf(false) }
            coroutine = rememberCoroutineScope()
            projectDialogs.ScheduleUpdate(
                project = project,
                show = showScheduleUpdate,
                viewModel = viewModel,
                dismissDialog = {
                    showScheduleUpdate.value = false
                    viewModel.snackbarHostState = snackbarHostState
                    viewModel.restartRefresher()
                }
            )
            PandoroTheme {
                Scaffold(
                    snackbarHost = { CreateSnackbarHost(hostState = snackbarHostState) },
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
                                    onClick = {
                                        currentProject.value = null
                                        onBackPressedDispatcher.onBackPressed()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                                    if(author != null && project.hasGroups()) {
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
                            onClick = {
                                viewModel.suspendRefresher()
                                showScheduleUpdate.value = true
                            }
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
                    var currentUpdate: ProjectUpdate? by remember { mutableStateOf(null) }
                    if(showDeleteDialog.value) {
                        PandoroAlertDialog(
                            show = showDeleteDialog,
                            title = delete_update,
                            extraTitle = currentUpdate!!.targetVersion,
                            text = delete_update_text,
                            requestLogic = {
                                viewModel.deleteUpdate(
                                    update = currentUpdate!!,
                                    onSuccess = {
                                        showDeleteDialog.value = false
                                    }
                                )
                            }
                        )
                    }
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
                            items(
                                items = project.updates,
                                key = { update ->
                                    update.id
                                }
                            ) { update ->
                                val status = update.status
                                val isScheduled = status == SCHEDULED
                                val isInDevelopment = status == IN_DEVELOPMENT
                                val isPublished = status == PUBLISHED
                                val showUpdateInfo = remember { mutableStateOf(false) }
                                val changeNotes = update.notes
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
                                            stickyHeader {
                                                if(isScheduled || isInDevelopment) {
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
                                                            Text(
                                                                text = stringResource(string.total_change_notes)
                                                                        + " ${update.notes.size}"
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
                                                                                viewModel.addChangeNote(
                                                                                    update = update,
                                                                                    contentNote = contentNote,
                                                                                    onSuccess = {
                                                                                        addNote = false
                                                                                        contentNote.value = ""
                                                                                    },
                                                                                    onFailure = {
                                                                                        pandoroModalSheet.showSnack(
                                                                                            it
                                                                                        )
                                                                                    }
                                                                                )
                                                                            } else {
                                                                                pandoroModalSheet.showSnack(
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
                                                } else {
                                                    Text(
                                                        text = stringResource(string.total_change_notes)
                                                                + " ${update.notes.size}"
                                                    )
                                                }
                                            }
                                            items(
                                                items = changeNotes,
                                                key = { note ->
                                                    note.id
                                                }
                                            ) { note ->
                                                val markedAsDone = mutableStateOf(note.isMarkedAsDone)
                                                if (isInDevelopment) {
                                                    SwipeableActionsBox(
                                                        swipeThreshold = 50.dp,
                                                        backgroundUntilSwipeThreshold = Transparent,
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
                                                                    viewModel.manageChangeNote(
                                                                        markedAsDone = markedAsDone,
                                                                        update = update,
                                                                        changeNote = note
                                                                    )
                                                                }
                                                            )
                                                        )
                                                    ) {
                                                        ChangeNoteCard(
                                                            note = note,
                                                            update = update,
                                                            markedAsDone = markedAsDone,
                                                            isScheduled = false,
                                                            isInDevelopment = true
                                                        )
                                                    }
                                                } else {
                                                    ChangeNoteCard(
                                                        note = note,
                                                        update = update,
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
                                    val showOptions = remember { mutableStateOf(false) }
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
                                                    if (showOptions.value) {
                                                        DropdownMenu(
                                                            modifier = Modifier.background(White),
                                                            expanded = showOptions.value,
                                                            onDismissRequest = {
                                                                showOptions.value = false
                                                            }
                                                        ) {
                                                            val publishUpdate = remember {
                                                                mutableStateOf(false)
                                                            }
                                                            if(update.notes.isNotEmpty()) {
                                                                DropdownMenuItem(
                                                                    text = {
                                                                        Text(
                                                                            text = stringResource(string.export_notes)
                                                                        )
                                                                    },
                                                                    trailingIcon = {
                                                                        Icon(
                                                                            imageVector = Icons.Default.ContentPaste,
                                                                            contentDescription = null
                                                                        )
                                                                    },
                                                                    onClick = {
                                                                        showOptions.value = false
                                                                        copyContent(
                                                                            formatNotesAsMarkdown(
                                                                                update = update
                                                                            )
                                                                        )
                                                                        showSnack(
                                                                            string.notes_formatted_in_markdown_copied
                                                                        )
                                                                    }
                                                                )
                                                            }
                                                            if(!isPublished) {
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
                                                                            viewModel.startUpdate(
                                                                                update = update
                                                                            )
                                                                            showOptions.value = false
                                                                        } else
                                                                            publishUpdate.value = true
                                                                    }
                                                                )
                                                            }
                                                            if(publishUpdate.value) {
                                                                PublishUpdate(
                                                                    showOptions,
                                                                    update,
                                                                    changeNotes,
                                                                    publishUpdate
                                                                )
                                                            }
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
                                                                    currentUpdate = update
                                                                    showOptions.value = false
                                                                    showDeleteDialog.value = true
                                                                }
                                                            )
                                                        }
                                                    }
                                                    IconButton(
                                                        modifier = Modifier.size(22.dp),
                                                        onClick = { showOptions.value = true }
                                                    ) {
                                                        Icon(
                                                            imageVector = Default.MoreVert,
                                                            contentDescription = null,
                                                            tint = PrimaryLight,
                                                        )
                                                    }
                                                }
                                            }
                                            Column(
                                                modifier = Modifier.padding(end = 10.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = CenterVertically,
                                                    horizontalArrangement = spacedBy(5.dp)
                                                ) {
                                                    Text(
                                                        text = getString(update_id),
                                                    )
                                                    Text(
                                                        text = update.id,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                SpaceContent()
                                                Column(
                                                    modifier = Modifier.padding(top = 5.dp),
                                                ) {
                                                    val updateAuthor = update.author
                                                    if (updateAuthor != null && hasGroup) {
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
                                                        if (scheduledBy != null && hasGroup) {
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
                                                        if (publishedBy != null && hasGroup) {
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
                                headerTitle = R.string.groups,
                                itemsList = project.groups,
                                clazz = GroupActivity::class.java
                            )
                        }
                        if (project.publishedUpdates.isNotEmpty()) {
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

    /**
     * Function to create a card for a change note
     *
     * @param note: the note to show
     * @param update: the update where is placed the change note
     * @param markedAsDone: whether the note is marked as done
     * @param isScheduled: whether the update is scheduled
     * @param isInDevelopment: whether the update is in development
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChangeNoteCard(
        note: Note,
        update: ProjectUpdate,
        markedAsDone: MutableState<Boolean>,
        isScheduled: Boolean,
        isInDevelopment: Boolean
    ) {
        val tooltipState = rememberTooltipState(
            isPersistent = true
        )
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                RichTooltip(
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
                            if (authorIsNotNull && hasGroup) {
                                Text(
                                    text = getString(string.author) + " ${author.completeName}"
                                )
                            }
                            Text(
                                text = stringResource(creation_date) + " ${note.creationDate}",
                            )
                            if (markedAsDone.value) {
                                if (authorIsNotNull && hasGroup) {
                                    Text(
                                        text = stringResource(marked_as_done_by) + " ${note.markedAsDoneBy.completeName}"
                                    )
                                }
                                Text(
                                    text = stringResource(date_of_mark) + " ${note.markedAsDoneDate}",
                                )
                            }
                        }
                    },
                    action = {
                        Row (
                            verticalAlignment = CenterVertically
                        ) {
                            TextButton(
                                onClick = { coroutine.launch { tooltipState.dismiss() } }
                            ) {
                                Text(
                                    text = getString(dismiss)
                                )
                            }
                            TextButton(
                                onClick = {
                                    coroutine.launch {
                                        copyNote(note)
                                        tooltipState.dismiss()
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(copy_note)
                                )
                            }
                        }
                    },
                )
            },
            state = tooltipState,
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
                                viewModel.deleteChangeNote(
                                    update = update,
                                    changeNote = note
                                )
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

    /**
     * Method to check if all the change notes of the update are marked as done before publish that
     * update
     *
     * @param showOptions: whether show the options menu
     * @param update: the update to publish
     * @param changeNotes: the list of the change notes to check
     * @param check: whether check the change notes list
     */
    @Composable
    private fun PublishUpdate(
        showOptions: MutableState<Boolean>,
        update: ProjectUpdate,
        changeNotes: List<Note>,
        check: MutableState<Boolean>
    ) {
        if(!areAllChangeNotesDone(changeNotes)) {
            PandoroAlertDialog(
                show = showOptions,
                title = not_all_the_change_notes_are_done,
                text = check_change_notes_message,
                requestLogic = { publishUpdate(showOptions, update, check) }
            )
        } else
            publishUpdate(showOptions, update, check)
    }

    /**
     * Function to check execute the request to publish the [ProjectUpdate]
     *
     * @param showOptions: whether show the options menu
     * @param update: the update to publish
     * @param check: whether check the change notes list
     */
    private fun publishUpdate(
        showOptions: MutableState<Boolean>,
        update: ProjectUpdate,
        check: MutableState<Boolean>
    ) {
        viewModel.publishUpdate(
            update = update,
            onSuccess = {
                val request = reviewManager.requestReviewFlow()
                showOptions.value = false
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val flow = reviewManager.launchReviewFlow(this, task.result)
                        flow.addOnCompleteListener {
                            check.value = false
                        }
                    } else
                        check.value = false
                }
            }
        )
    }

    /**
     * Function to show the user stats
     *
     * No any params required
     */
    @Composable
    private fun ShowStats() {
        val barData = arrayListOf<BarData>()
        project.publishedUpdates.forEachIndexed { index, update ->
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
                    highlightTextBackgroundColor = Transparent,
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

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or {@link #onPause}. This
     * is usually a hint for your activity to start interacting with the user, which is a good
     * indicator that the activity became active and ready to receive input. This sometimes could
     * also be a transit state toward another resting state. For instance, an activity may be
     * relaunched to {@link #onPause} due to configuration changes and the activity was visible,
     * but wasn’t the top-most activity of an activity task. {@link #onResume} is guaranteed to be
     * called before {@link #onPause} in this case which honors the activity lifecycle policy and
     * the activity eventually rests in {@link #onPause}.
     *
     * <p>On platform versions prior to {@link android.os.Build.VERSION_CODES#Q} this is also a good
     * place to try to open exclusive-access devices or to get access to singleton resources.
     * Starting  with {@link android.os.Build.VERSION_CODES#Q} there can be multiple resumed
     * activities in the system simultaneously, so {@link #onTopResumedActivityChanged(boolean)}
     * should be used for that purpose instead.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * Will be set the **[FetcherManager.activeContext]** with the current context
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     * @see #onTopResumedActivityChanged(boolean)
     */
    override fun onResume() {
        super.onResume()
        viewModel.setActiveContext(this::class.java)
    }

}