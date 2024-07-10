package com.tecknobit.pandoro.ui.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinox.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.pandoro.R.string.insert_a_correct_target_version
import com.tecknobit.pandoro.R.string.you_must_insert_correct_notes
import com.tecknobit.pandoro.R.string.you_must_insert_one_note_at_least
import com.tecknobit.pandoro.ui.activities.session.ProjectActivity
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.areNotesValid
import com.tecknobit.pandorocore.helpers.InputsValidator.Companion.isValidVersion
import com.tecknobit.pandorocore.records.Note
import com.tecknobit.pandorocore.records.Project
import com.tecknobit.pandorocore.records.ProjectUpdate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **ProjectActivityViewModel** class is the support class used by the [ProjectActivity]
 * to refresh the project displayed and to manage it
 *
 * @param initialProject: the initial value of the project displayed
 * @param snackbarHostState: the host to launch the snackbar messages
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroViewModel
 * @see ViewModel
 * @see FetcherManagerWrapper
 */
class ProjectActivityViewModel (
    initialProject: Project,
    snackbarHostState: SnackbarHostState
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    /**
     * **isRefreshing** -> whether the [refreshProject] has been already invoked
     */
    private var isRefreshing: Boolean = false

    /**
     * **_project** -> the project currently displayed
     */
    private val _project = MutableStateFlow(initialProject)
    val project: StateFlow<Project> = _project

    /**
     * **targetVersion** -> the version of the update to schedule
     */
    lateinit var targetVersion: MutableState<String>

    /**
     * Function to restart the current [refreshRoutine] after other requests has been executed,
     * the [isRefreshing] instance will be set as **true** to deny the restart of the routine after executing
     * the other requests
     *
     * No-any params required
     */
    override fun restartRefresher() {
        if(isRefreshing)
            super.restartRefresher()
    }

    /**
     * Function to execute the request to refresh the [_project] item
     *
     * @param onSuccess: the action to execute whether the request has been successful
     */
    fun refreshProject(
        onSuccess: () -> Unit
    ) {
        execRefreshingRoutine(
            currentContext = ProjectActivity::class.java,
            routine = {
                isRefreshing = true
                requester.sendRequest(
                    request = {
                        requester.getProject(
                            projectId = _project.value.id
                        )
                    },
                    onSuccess = { response ->
                        _project.value = Project.getInstance(
                            response.getJSONObject(RESPONSE_MESSAGE_KEY)
                        )
                        onSuccess.invoke()
                    },
                    onFailure = { showSnack(it) }
                )
            }
        )
    }

    /**
     * Function to execute the request to add a new change note to an update
     *
     * @param update: the update where add the change note
     * @param contentNote: the content of the note
     * @param onSuccess: the action to execute whether the request has been successful
     * @param onFailure: the action to execute whether the request has been failed
     */
    fun addChangeNote(
        update: ProjectUpdate,
        contentNote: MutableState<String>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.addChangeNote(
                    projectId = _project.value.id,
                    updateId = update.id,
                    changeNote = contentNote.value
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { onFailure.invoke(it.getString(RESPONSE_MESSAGE_KEY)) }
        )
    }

    /**
     * Function to execute the request to mark as to do or mark as done a change note
     *
     * @param markedAsDone: whether the change note is currently marked as done
     * @param update: the update where add the change note
     * @param changeNote: the change note to manage
     */
    fun manageChangeNote(
        markedAsDone: MutableState<Boolean>,
        update: ProjectUpdate,
        changeNote: Note
    ) {
        requester.sendRequest(
            request = {
                if(markedAsDone.value) {
                    requester.markChangeNoteAsToDo(
                        projectId = _project.value.id,
                        updateId = update.id,
                        changeNoteId = changeNote.id
                    )
                } else {
                    requester.markChangeNoteAsDone(
                        projectId = _project.value.id,
                        updateId = update.id,
                        changeNoteId = changeNote.id
                    )
                }
            },
            onSuccess = { markedAsDone.value != markedAsDone.value },
            onFailure = { showSnack(it) }
        )
    }

    /**
     * Function to execute the request to schedule a new update
     *
     * @param project: the project where attach the update
     * @param notes: list of the change notes for that update
     * @param onSuccess: the action to execute whether the request has been successful
     */
    fun scheduleUpdate(
        project: Project,
        notes: List<String>,
        onSuccess: () -> Unit
    ) {
        if (isValidVersion(targetVersion.value)) {
            if (notes.isNotEmpty()) {
                if (areNotesValid(notes)) {
                    requester.sendRequest(
                        request = {
                            requester.scheduleUpdate(
                                projectId = project.id,
                                targetVersion = targetVersion.value,
                                updateChangeNotes = notes
                            )
                        },
                        onSuccess = {
                            targetVersion.value = ""
                            onSuccess.invoke()
                        },
                        onFailure = { showSnack(it) }
                    )
                } else
                    showSnack(you_must_insert_correct_notes)
            } else
                showSnack(you_must_insert_one_note_at_least)
        } else
            showSnack(insert_a_correct_target_version)
    }

    /**
     * Function to execute the request to start a scheduled update
     *
     * @param update: the update to start
     */
    fun startUpdate(
        update: ProjectUpdate
    ) {
        requester.sendRequest(
            request = {
                requester.startUpdate(
                    projectId = _project.value.id,
                    updateId = update.id
                )
            },
            onSuccess = { },
            onFailure = { showSnack(it) }
        )
    }

    /**
     * Function to execute the request to delete a change note of an update
     *
     * @param update: the update owner of the change note
     * @param changeNote: the change note to delete
     */
    fun deleteChangeNote(
        update: ProjectUpdate,
        changeNote: Note
    ) {
        requester.sendRequest(
            request = {
                requester.deleteChangeNote(
                    projectId = _project.value.id,
                    updateId = update.id,
                    changeNoteId = changeNote.id
                )
            },
            onSuccess = { },
            onFailure = { showSnack(it) }
        )
    }

    /**
     * Function to execute the request to publish an update
     *
     * @param update: the update to publish
     * @param onSuccess: the action to execute whether the request has been successful
     */
    fun publishUpdate(
        update: ProjectUpdate,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.publishUpdate(
                    projectId = _project.value.id,
                    updateId = update.id
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

    /**
     * Function to execute the request to delete an update
     *
     * @param update: the update to delete
     * @param onSuccess: the action to execute whether the request has been successful
     */
    fun deleteUpdate(
        update: ProjectUpdate,
        onSuccess: () -> Unit
    ) {
        requester.sendRequest(
            request = {
                requester.deleteUpdate(
                    projectId = _project.value.id,
                    updateId = update.id
                )
            },
            onSuccess = { onSuccess.invoke() },
            onFailure = { showSnack(it) }
        )
    }

}