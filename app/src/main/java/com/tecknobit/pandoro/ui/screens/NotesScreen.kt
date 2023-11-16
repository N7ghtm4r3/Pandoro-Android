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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material.icons.twotone.RemoveDone
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.tecknobit.pandoro.R.string.note_info
import com.tecknobit.pandoro.helpers.isContentNoteValid
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.pandoroModalSheet
import com.tecknobit.pandoro.ui.activities.SplashScreen.Companion.user
import com.tecknobit.pandoro.ui.components.PandoroCard
import com.tecknobit.pandoro.ui.theme.ErrorLight
import com.tecknobit.pandoro.ui.theme.GREEN_COLOR
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

class NotesScreen: Screen() {

    companion object {

        lateinit var showAddNoteSheet: MutableState<Boolean>

    }

    @Composable
    override fun ShowScreen() {
        showAddNoteSheet = remember { mutableStateOf(false) }
        SetScreen {
            CreateInputModalBottom(
                show = showAddNoteSheet,
                title = create_a_new_note,
                label = content,
                buttonText = create,
                requestLogic = {
                    if (isContentNoteValid(sheetInputValue.value)) {
                        /*TODO MAKE REQUEST THEN*/
                        sheetInputValue.value = ""
                        showAddNoteSheet.value = false
                    } else
                        pandoroModalSheet.showSnack(insert_a_correct_content)
                },
                requiredTextArea = true
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(user.notes) { note ->
                    val showInfoNote = remember { mutableStateOf(false) }
                    val markedAsDone by remember { mutableStateOf(note.isMarkedAsDone) }
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
                                    if (markedAsDone) {
                                        // TODO: MAKE REQUEST THEN
                                    } else {
                                        // TODO: MAKE REQUEST THEN
                                    }
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
                                    Text(
                                        modifier = Modifier
                                            .weight(10f)
                                            .fillMaxWidth(),
                                        text = note.content,
                                        textAlign = TextAlign.Justify,
                                        textDecoration = if (markedAsDone) LineThrough else null
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .weight(1f)
                                            .size(24.dp),
                                        onClick = { /*MAKE REQUEST THEN*/ }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = ErrorLight
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}