package com.tecknobit.pandoro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.R.string.content
import com.tecknobit.pandoro.R.string.create
import com.tecknobit.pandoro.R.string.create_a_new_note
import com.tecknobit.pandoro.R.string.creation_date
import com.tecknobit.pandoro.R.string.date_of_mark
import com.tecknobit.pandoro.R.string.insert_a_correct_content
import com.tecknobit.pandoro.R.string.no_any_personal_notes
import com.tecknobit.pandoro.R.string.note_info
import com.tecknobit.pandoro.helpers.copyNote
import com.tecknobit.pandoro.helpers.isContentNoteValid
import com.tecknobit.pandoro.helpers.refreshers.AndroidListManager
import com.tecknobit.pandoro.records.Note
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.activeScreen
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.requester
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.screens.Screen.ScreenType.Notes
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.GREEN_COLOR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * The **NotesScreen** class is useful to show the notes of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 * @see AndroidListManager
 */
class NotesScreen: Screen(), AndroidListManager {

    companion object {

        /**
         * **showAddNoteSheet** -> the flag to show the modal bottom sheet to add a new note
         */
        lateinit var showAddNoteSheet: MutableState<Boolean>

        /**
         * **notes** -> the list of the notes
         */
        val notes: SnapshotStateList<Note> = mutableStateListOf()

    }

    /**
     * Function to show the content screen
     *
     * No any params required
     */
    @Composable
    override fun ShowScreen() {
        showAddNoteSheet = remember { mutableStateOf(false) }
        refreshValues()
        SetScreen {
            CreateInputModalBottom(
                show = showAddNoteSheet,
                title = create_a_new_note,
                label = content,
                buttonText = create,
                requestLogic = {
                    if (isContentNoteValid(sheetInputValue.value)) {
                        requester!!.execAddNote(sheetInputValue.value)
                        if(requester!!.successResponse()) {
                            sheetInputValue.value = ""
                            showAddNoteSheet.value = false
                        } else
                            pandoroModalSheet.showSnack(requester!!.errorMessage())
                    } else
                        pandoroModalSheet.showSnack(insert_a_correct_content)
                },
                requiredTextArea = true
            )
            if(notes.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(
                        items = notes,
                        key = { note ->
                            note.id
                        }
                    ) { note ->
                        val showInfoNote = remember { mutableStateOf(false) }
                        var markedAsDone = note.isMarkedAsDone
                        pandoroModalSheet.PandoroModalSheet(
                            modifier = Modifier.height(200.dp),
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
                            )
                            if(markedAsDone) {
                                Divider(thickness = 1.dp)
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 10.dp,
                                            bottom = 10.dp
                                        ),
                                    text = stringResource(date_of_mark) + " ${note.markedAsDoneDate}",
                                )
                            }
                        }
                        SwipeableActionsBox(
                            swipeThreshold = 200.dp,
                            startActions = listOf(
                                SwipeAction(
                                    icon = rememberVectorPainter(
                                        if (markedAsDone)
                                            Icons.TwoTone.RemoveDone
                                        else
                                            Icons.TwoTone.Done
                                    ),
                                    background =
                                    if (markedAsDone)
                                        ErrorLight
                                    else
                                        GREEN_COLOR,
                                    onSwipe = {
                                        if (markedAsDone)
                                            requester!!.execMarkNoteAsToDo(note.id)
                                        else
                                            requester!!.execMarkNoteAsDone(note.id)
                                        if(requester!!.successResponse())
                                            markedAsDone = !markedAsDone
                                        else
                                            showSnack(requester!!.errorMessage())
                                    }
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
                                            .padding(20.dp),
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
                                                textDecoration = if (markedAsDone) LineThrough else null
                                            )
                                            Column (
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight(),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .background(GREEN_COLOR)
                                                        .weight(1f),
                                                        //.width(10.dp),
                                                    content = {
                                                        IconButton(
                                                            modifier = Modifier
                                                                .size(20.dp),
                                                            onClick = { copyNote(note) }
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.ContentCopy,
                                                                contentDescription = null
                                                            )
                                                        }
                                                    }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .background(ErrorLight)
                                                        .weight(1f),
                                                    //.width(10.dp),
                                                    content = {
                                                        IconButton(
                                                            modifier = Modifier
                                                                .size(20.dp),
                                                            onClick = {
                                                                requester!!.execDeleteNote(note.id)
                                                                if (!requester!!.successResponse())
                                                                    showSnack(requester!!.errorMessage())
                                                            }
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Delete,
                                                                contentDescription = null,
                                                                tint = ErrorLight
                                                            )
                                                        }
                                                    }
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

    /**
     * Function to refresh a list of items to display in the UI
     *
     * No-any params required
     */
    override fun refreshValues() {
        CoroutineScope(Dispatchers.Default).launch {
            var response: String
            while (user.id != null && activeScreen.value == Notes) {
                try {
                    val tmpNotes = mutableStateListOf<Note>()
                    response = requester!!.execNotesList()
                    if(requester!!.successResponse()) {
                        val jNotes = JSONArray(response)
                        for (j in 0 until jNotes.length())
                            tmpNotes.add(Note(jNotes[j] as JSONObject))
                        if(needToRefresh(notes, tmpNotes)) {
                            notes.clear()
                            notes.addAll(tmpNotes)
                        }
                    } else
                        showSnack(requester!!.errorMessage())
                } catch (_: JSONException){
                }
            }
        }
    }

}