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

class ProjectActivityViewModel (
    initialProject: Project,
    snackbarHostState: SnackbarHostState
): PandoroViewModel(
    snackbarHostState = snackbarHostState
) {

    private val _project = MutableStateFlow(initialProject)
    val project: StateFlow<Project> = _project

    lateinit var targetVersion: MutableState<String>

    fun refreshProject(
        onSuccess: () -> Unit
    ) {
        execRefreshingRoutine(
            currentContext = ProjectActivity::class.java,
            routine = {
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