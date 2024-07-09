package com.tecknobit.pandoro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material.icons.twotone.RemoveDone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.unit.dp
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.pandoro.R.string.content
import com.tecknobit.pandoro.R.string.create
import com.tecknobit.pandoro.R.string.create_a_new_note
import com.tecknobit.pandoro.R.string.creation_date
import com.tecknobit.pandoro.R.string.date_of_mark
import com.tecknobit.pandoro.R.string.insert_a_correct_content
import com.tecknobit.pandoro.R.string.no_any_personal_notes
import com.tecknobit.pandoro.R.string.note_info
import com.tecknobit.pandoro.helpers.copyNote
import com.tecknobit.pandoro.ui.activities.navigation.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.GREEN_COLOR
import com.tecknobit.pandoro.ui.theme.IceGrayColor
import com.tecknobit.pandoro.ui.theme.PrimaryLight
import com.tecknobit.pandoro.ui.viewmodels.NotesScreenViewModel
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isContentNoteValid
import com.tecknobit.pandorocore.records.Note
import kotlinx.coroutines.flow.StateFlow
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

/**
 * The **NotesScreen** class is useful to show the notes of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 */
class NotesScreen: Screen() {

    companion object {

        /**
         * **showAddNoteSheet** -> the flag to show the modal bottom sheet to add a new note
         */
        lateinit var showAddNoteSheet: MutableState<Boolean>

        lateinit var notes: StateFlow<List<Note>>

    }

    private lateinit var viewModel: NotesScreenViewModel

    /**
     * Function to show the content screen
     *
     * No any params required
     */
    @Composable
    override fun ShowScreen() {
        showAddNoteSheet = remember { mutableStateOf(false) }
        keepsnackbarHostState = remember { SnackbarHostState() }
        viewModel = NotesScreenViewModel(
            snackbarHostState = keepsnackbarHostState
        )
        val myNotes = notes.collectAsState().value
        SetScreen {
            CreateInputModalBottom(
                show = showAddNoteSheet,
                title = create_a_new_note,
                label = content,
                buttonText = create,
                requestLogic = {
                    if (isContentNoteValid(sheetInputValue.value)) {
                        viewModel.addNote(
                            content = sheetInputValue.value,
                            onSuccess = {
                                sheetInputValue.value = ""
                                showAddNoteSheet.value = false
                            },
                            onFailure = {
                                pandoroModalSheet.showSnack(it.getString(RESPONSE_MESSAGE_KEY))
                            }
                        )
                    } else
                        pandoroModalSheet.showSnack(insert_a_correct_content)
                },
                requiredTextArea = true
            )
            if(myNotes.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        bottom = 8.dp
                    )
                ) {
                    items(
                        items = myNotes,
                        key = { note ->
                            note.id
                        }
                    ) { note ->
                        val showInfoNote = remember { mutableStateOf(false) }
                        val markedAsDone = remember { mutableStateOf(note.isMarkedAsDone) }
                        pandoroModalSheet.PandoroModalSheet(
                            modifier = Modifier
                                .height(200.dp),
                            show = showInfoNote,
                            title = note_info
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 10.dp,
                                        bottom = 10.dp
                                    ),
                                text = stringResource(creation_date) + " ${note.creationDate}",
                                color = PrimaryLight
                            )
                            if(markedAsDone.value) {
                                HorizontalDivider(thickness = 1.dp)
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 10.dp,
                                            bottom = 10.dp
                                        ),
                                    text = stringResource(date_of_mark) + " ${note.markedAsDoneDate}",
                                    color = PrimaryLight
                                )
                            }
                        }
                        SwipeableActionsBox(
                            swipeThreshold = 50.dp,
                            backgroundUntilSwipeThreshold = Color.Transparent,
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
                                        viewModel.manageNote(
                                            markAsDone = markedAsDone,
                                            note = note
                                        )
                                    }
                                )
                            ),
                            endActions = listOf(
                                SwipeAction(
                                    icon = rememberVectorPainter(Icons.Default.ContentCopy),
                                    background = IceGrayColor,
                                    onSwipe = { copyNote(note) }
                                )
                            )
                        ) {
                            PandoroCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                onClick = { showInfoNote.value = true },
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                all = 20.dp
                                            ),
                                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val content = note.content
                                        if(content != null) {
                                            Text(
                                                modifier = Modifier
                                                    .weight(10f)
                                                    .fillMaxWidth(),
                                                text = content,
                                                textAlign = TextAlign.Justify,
                                                textDecoration = if (markedAsDone.value)
                                                    LineThrough
                                                else
                                                    null
                                            )
                                            IconButton(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .size(24.dp),
                                                onClick = {
                                                    viewModel.deleteNote(
                                                        note = note
                                                    )
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = null,
                                                    tint = ErrorLight
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            } else
                EmptyList(message = no_any_personal_notes)
        }
    }

}