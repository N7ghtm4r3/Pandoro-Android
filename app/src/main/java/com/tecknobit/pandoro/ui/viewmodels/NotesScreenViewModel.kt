package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.pandorocore.records.Note

/**
 * The **NotesScreenViewModel** class is the support class used by the [NotesScreenViewModel]
 * to manage the notes of the user
 *
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class NotesScreenViewModel(
    override var snackbarHostState: SnackbarHostState?
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * Function to execute the request to add a new note
     *
     * @param content: the content of the note
     * @param onSuccess: the action to execute whether the request has been successful
     * @param onFailure: the action to execute whether the request has been failed
     */
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

    /**
     * Function to execute the request to mark as to do or mark as done a note
     *
     * @param markAsDone: whether the change note is currently marked as done
     * @param note: the note to manage
     */
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

    /**
     * Function to execute the request to delete a note
     *
     * @param note: the note to delete
     */
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