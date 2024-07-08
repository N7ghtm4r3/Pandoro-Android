package com.tecknobit.pandoro.ui.activities.session

/*
/**
 * The **PandoroDataActivity** class is useful to create an activity with the behavior to show the UI
 * data
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see SnackbarLauncher
 */
@Structure
abstract class PandoroDataActivity : ComponentActivity(), SnackbarLauncher {

    /**
     * **coroutine** the coroutine to launch the snackbars
     */
    protected lateinit var coroutine: CoroutineScope

    /**
     * **snackbarHostState** the host to launch the snackbars
     */
    protected lateinit var snackbarHostState: SnackbarHostState


    /**
     * Function to show the data
     *
     * @param content: the content to show
     */
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    protected fun ShowData(
        content: LazyListScope.() -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.padding(
                top = 168.dp,
                bottom = 16.dp,
                end = 16.dp,
                start = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 10.dp),
            content = content
        )
    }

    /**
     * Function to create an header title
     *
     * @param headerTitle: the header title
     * @param extraIcon: the extra icon to show with the title
     * @param show: whether show the subsection
     * @param showArrow: whether show the arrow to display the subsection
     */
    @Composable
    protected fun CreateHeader(
        headerTitle: Int,
        extraIcon: ExtraIcon? = null,
        show: MutableState<Boolean>,
        showArrow: Boolean = true
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(headerTitle),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            if (showArrow) {
                IconButton(
                    onClick = { show.value = !show.value }
                ) {
                    Icon(
                        imageVector = if (show.value) Default.ArrowDropUp else Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            if (extraIcon != null) {
                IconButton(
                    onClick = extraIcon.action
                ) {
                    Icon(
                        imageVector = extraIcon.icon,
                        contentDescription = null
                    )
                }
            }
        }
    }

    /**
     * Function to show the description section
     *
     * @param description: the description to show
     */
    @Composable
    protected open fun ShowDescription(description: String) {
        val showDescription = remember { mutableStateOf(false) }
        val showDescriptionSection = remember { mutableStateOf(true) }
        pandoroModalSheet.PandoroModalSheet(
            show = showDescription,
            title = R.string.description,
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState()),
                text = description,
                textAlign = TextAlign.Justify,
            )
        }
        CreateHeader(
            headerTitle = R.string.description,
            show = showDescriptionSection
        )
        if (showDescriptionSection.value) {
            Text(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .clickable { showDescription.value = true },
                text = description,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
        }
        SpaceContent()
    }

    /**
     * Function to show a list of items
     *
     * @param show: whether show the list
     * @param headerTitle: the header title
     * @param extraIcon: the extra icon to show with the title
     * @param itemsList: the list of the items to show
     * @param clazz: the class value of the items list
     * @param adminPrivileges: whether the user has the admin privileges to execute the own actions
     */
    @Composable
    protected fun <T> ShowItemsList(
        show: MutableState<Boolean>,
        headerTitle: Int,
        extraIcon: ExtraIcon? = null,
        itemsList: List<PandoroItem>,
        clazz: Class<T>,
        adminPrivileges: Boolean = false
    ) {
        val itemsListFill = itemsList.isNotEmpty()
        if (itemsListFill || adminPrivileges) {
            CreateHeader(
                headerTitle = headerTitle,
                extraIcon = extraIcon,
                show = show,
                showArrow = itemsListFill
            )
            SpaceContent()
            Spacer(modifier = Modifier.height(10.dp))
            if (show.value) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(2),
                    modifier = Modifier
                        .height(130.dp)
                        .padding(
                            start = 1.dp,
                            end = 1.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = itemsList,
                        key = { item ->
                            item.id
                        }
                    ) { item ->
                        PandoroCard(
                            modifier = Modifier.size(
                                width = 130.dp,
                                height = 65.dp
                            ),
                            onClick = {
                                if(item is Group)
                                    currentGroup.value = item
                                else
                                    currentProject.value = item as Project
                                ContextCompat.startActivity(context, Intent(context, clazz), null)
                            }
                        ) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .weight(10f)
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = item.name,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    ColoredBorder(color = PrimaryLight)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * The **ExtraIcon** class is useful to pair an icon and its action
     */
    protected data class ExtraIcon(
        val action: () -> Unit,
        val icon: ImageVector
    )

    /**
     * Function to show a message with the [SnackbarHostState]
     *
     * @param message: the message to show
     */
    override fun showSnack(message: String) {
        showSnack(
            scope = coroutine,
            snackbarHostState = snackbarHostState,
            message = message
        )
    }

}
*/