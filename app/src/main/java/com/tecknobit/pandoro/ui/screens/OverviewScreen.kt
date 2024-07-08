package com.tecknobit.pandoro.ui.screens

/*
/**
 * The **OverviewScreen** class is useful to show the overview performance of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Screen
 */
class OverviewScreen: Screen() {

    /**
     * **overviewUIHelper** -> the manager of the overview UI
     */
    private lateinit var overviewUIHelper: OverviewUIHelper

    /**
     * **donutChartConfig** -> the config for the donut chart
     */
    private lateinit var donutChartConfig: PieChartConfig

    /**
     * Function to show the content screen
     *
     * No any params required
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun ShowScreen() {
        overviewUIHelper = OverviewUIHelper(user.projects)
        donutChartConfig = PieChartConfig(
            strokeWidth = 60f,
            isAnimationEnable = true,
            labelVisible = false,
            chartPadding = 25,
            isClickOnSliceEnabled = false
        )
        SetScrollableScreen {
            val showUpdatesStatus = remember { mutableStateOf(false) }
            val showProjectsPerformance = remember { mutableStateOf(false) }
            val projects = user.projects
            var personalValue = 0
            projects.forEach { project ->
                if (!project.hasGroups())
                    personalValue++
            }
            CreateCompareCard(
                title = stringResource(id = string.projects),
                total = projects.size,
                personalValue = personalValue,
                onClick = { showProjectsPerformance.value = true }
            )
            pandoroModalSheet.PandoroModalSheet(
                columnModifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 20.dp
                ),
                show = showProjectsPerformance,
                title = projects_performance
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            start = 5.dp,
                            end = 5.dp
                        )
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    overviewUIHelper.bestPersonalVProject = overviewUIHelper.getBestPersonalProject()
                    overviewUIHelper.bestGroupVProject = overviewUIHelper.getBestGroupProject()
                    if (overviewUIHelper.bestPersonalVProject != null ||
                        overviewUIHelper.bestGroupVProject != null) {
                        if(overviewUIHelper.bestPersonalVProject != null) {
                            Text(
                                text = stringResource(personal),
                                fontSize = 20.sp
                            )
                            Divider(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                thickness = 1.dp
                            )
                            CreatePerformanceCard(
                                title = stringResource(best_performance),
                                project = overviewUIHelper.bestPersonalVProject!!
                            )
                            val worstProject = overviewUIHelper.getWorstPersonalProject()
                            if (worstProject != null) {
                                CreatePerformanceCard(
                                    title = stringResource(worst_performance),
                                    project = worstProject
                                )
                            }
                        }
                        if(overviewUIHelper.bestGroupVProject != null) {
                            Text(
                                text = stringResource(group),
                                fontSize = 20.sp
                            )
                            Divider(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                thickness = 1.dp
                            )
                            CreatePerformanceCard(
                                title = stringResource(best_performance),
                                project = overviewUIHelper.bestGroupVProject!!
                            )
                            val worstProject = overviewUIHelper.getWorstGroupProject()
                            if (worstProject != null) {
                                CreatePerformanceCard(
                                    title = stringResource(worst_performance),
                                    project = worstProject
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            content = {
                                Text(
                                    text = stringResource(no_projects_yet),
                                    fontSize = 18.sp
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            var totalValue = 0
            personalValue = 0
            projects.forEach { project ->
                val value = project.updatesNumber
                totalValue += value
                if (!project.hasGroups())
                    personalValue += value
            }
            CreateCompareCard(
                title = stringResource(updates),
                total = totalValue,
                personalValue = personalValue,
                onClick = { showUpdatesStatus.value = true }
            )
            pandoroModalSheet.PandoroModalSheet(
                show = showUpdatesStatus,
                title = updates_status
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            start = 5.dp,
                            end = 5.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CreateUpdateCard(
                        title = stringResource(scheduled),
                        projects = projects,
                        status = SCHEDULED
                    )
                    CreateUpdateCard(
                        title = stringResource(in_development),
                        projects = projects,
                        status = IN_DEVELOPMENT
                    )
                    CreateUpdateCard(
                        title = stringResource(published),
                        projects = projects,
                        status = PUBLISHED
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            totalValue = 0
            personalValue = 0
            projects.forEach { project ->
                val value = project.totalDevelopmentDays
                totalValue += value
                if(!project.hasGroups())
                    personalValue += value
            }
            CreateCompareCard(
                title = stringResource(development_days),
                total = totalValue,
                personalValue = personalValue
            )
            Spacer(modifier = Modifier.height(10.dp))
            totalValue = 0
            personalValue = 0
            projects.forEach { project ->
                val value = project.averageDevelopmentTime
                totalValue += value
                if(!project.hasGroups())
                    personalValue += value
            }
            CreateCompareCard(
                title = stringResource(average_development_days),
                total = totalValue,
                personalValue = personalValue
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    /**
     * Function to create a card for a compare
     *
     * @param title: the title of the card
     * @param total: the total value of the compare
     * @param personalValue: the personal value of the compare
     * @param onClick: the onclick action to execute when the card has been pressed
     */
    @Composable
    private fun CreateCompareCard(
        title: String,
        total: Int,
        personalValue: Int,
        onClick: (() -> Unit)? = null
    ) {
        CreateCard(
            title = title,
            total = total,
            personalValue = personalValue,
            onClick = onClick
        )
    }

    /**
     * Function to create a card for an update
     *
     * @param title: the title of the card
     * @param projects: the list of the projects
     * @param status: the status of the update
     * @param onClick: the onclick action to execute when the card has been pressed
     */
    @Composable
    private fun CreateUpdateCard(
        title: String,
        projects: ArrayList<Project>,
        status: Status,
        onClick: (() -> Unit)? = null
    ) {
        var total = 0
        var personalValue = 0
        var fromMe = 0
        projects.forEach { project ->
            project.updates.forEach { update ->
                if (update.status == status) {
                    total++
                    if (!project.hasGroups())
                        personalValue++
                    val author: PublicUser? = when (status) {
                        SCHEDULED -> update.author
                        IN_DEVELOPMENT -> update.startedBy
                        PUBLISHED -> update.publishedBy
                    }
                    if (author == null || author.id == user.id)
                        fromMe++
                }
            }
        }
        CreateCard(
            title = title,
            total = total,
            personalValue = personalValue,
            fromMe = fromMe,
            onClick = onClick
        )
    }

    /**
     * Function to create a card
     *
     * @param title: the title of the card
     * @param total: the total value of the compare
     * @param personalValue: the personal value of the compare
     * @param fromMe: value where you are the author of the action
     * @param onClick: the onclick action to execute when the card has been pressed
     */
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CreateCard(
        title: String,
        total: Int,
        personalValue: Int,
        fromMe: Int = -1,
        onClick: (() -> Unit)? = null
    ) {
        val groupValue = total - personalValue
        val personalPercentage =
            computeProportion(total.toDouble(), personalValue.toDouble()).round()
        val content: @Composable ColumnScope.() -> Unit = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        val donutChartData = PieChartData(
                            slices = listOf(
                                PieChartData.Slice(
                                    label = stringResource(personal),
                                    value = personalValue.toFloat(),
                                    color = PrimaryLight
                                ),
                                PieChartData.Slice(
                                    label = stringResource(group),
                                    value = groupValue.toFloat(),
                                    color = GREEN_COLOR
                                ),
                            ),
                            plotType = PlotType.Donut
                        )
                        DonutPieChart(
                            modifier = Modifier
                                .width(150.dp)
                                .height(150.dp)
                                .background(Color.White),
                            pieChartData = donutChartData,
                            pieChartConfig = donutChartConfig
                        )
                    }
                    Column (
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(string.total) + " - ",
                            )
                            Text(
                                text = total.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(personal) + " - ",
                            )
                            Text(
                                text = "$personalValue ",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "(${personalPercentage}%)",
                                fontSize = 14.sp
                            )
                        }
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(group) + " - ",
                            )
                            Text(
                                text = (total - personalValue).toString() + " ",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "(${(100 - personalPercentage).round()}%)",
                                color = GREEN_COLOR,
                                fontSize = 14.sp
                            )
                        }
                        if (fromMe != -1) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(by_me) + " - ",
                                )
                                Text(
                                    text = "$fromMe ",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "(${
                                        computeProportion(
                                            total.toDouble(),
                                            fromMe.toDouble()
                                        ).round()
                                    }%)",
                                    color = YELLOW_COLOR,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
        val modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
        PandoroCard(
            modifier = modifier,
            shape = RoundedCornerShape(20.dp),
            onClick = onClick,
            content = content
        )
    }

    /**
     * Function to create a card to show the performance of a project
     *
     * @param title: the title of the card
     * @param project: the project to show its performance
     */
    @Composable
    private fun CreatePerformanceCard(
        title: String,
        project: Project
    ) {
        PandoroCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = { navToProject(project) }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(14f)
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            bottom = 10.dp
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp,
                            ),
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Column(
                        modifier = Modifier.padding(start = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(name) + ": ${project.name}",
                        )
                        Text(
                            text = stringResource(description) + ": ${project.shortDescription}"
                        )
                        Text(
                            text = stringResource(updates_number) + ": ${project.updatesNumber}"
                        )
                        Text(
                            text = stringResource(development_days)
                                    + ": ${project.totalDevelopmentDays}"
                        )
                        Text(
                            text = stringResource(average_development_time)
                                    + ": ${project.averageDevelopmentTime}"
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    ColoredBorder(
                        if (title.contains("Best"))
                            GREEN_COLOR
                        else
                            ErrorLight
                    )
                }
            }
        }
    }

    /**
     * Function to round a real number value
     *
     * @param decimals: the decimals digits to show
     */
    private fun Double.round(decimals: Int = 2): Double {
        return "%.${decimals}f".format(this).replace(",", ".").toDouble()
    }

}*/