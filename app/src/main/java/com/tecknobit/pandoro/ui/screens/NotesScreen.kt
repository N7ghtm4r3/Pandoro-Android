package com.tecknobit.pandoro.ui.screens

/*
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
                            swipeThreshold = 50.dp,
                            backgroundUntilSwipeThreshold = Color.Transparent,
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
                            ),
                            endActions = listOf(
                                SwipeAction(
                                    icon = rememberVectorPainter(Default.ContentCopy),
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
                                            IconButton(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .size(24.dp),
                                                onClick = {
                                                    requester!!.execDeleteNote(note.id)
                                                    if (!requester!!.successResponse())
                                                        showSnack(requester!!.errorMessage())
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

}*/