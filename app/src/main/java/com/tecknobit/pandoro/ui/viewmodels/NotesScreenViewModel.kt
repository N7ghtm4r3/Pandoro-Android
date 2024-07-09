package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandorocore.records.Note

class NotesScreenViewModel(
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    fun addNote(
        content: String,
        onSuccess: () -> Unit,
        onFailure: (JsonHelper) -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.addNote(
                    contentNote = content
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { onFailure.invoke(it) }
        )
    }

    fun manageNote(
        markAsDone: MutableState<Boolean>,
        note: Note
    ) {
        requester.sendRequest(
            request = {
                if(markAsDone.value) {
                    requester.markNoteAsToDo(
                        noteId = note.id
                    )
                } else {
                    requester.markNoteAsDone(
                        noteId = note.id
                    )
                }
            },
            onSuccess = { markAsDone.value = !markAsDone.value},
            onFailure = { showSnack(it) }
        )
    }

    fun deleteNote(
        note: Note
    ) {
        requester.sendRequest(
            request = {
                requester.deleteNote(
                    noteId = note.id
                )
            },
            onSuccess = {},
            onFailure = { showSnack(it) }
        )
    }

}