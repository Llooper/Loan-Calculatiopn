package com.fll.loan.calculation.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fll.loan.calculation.R
import com.fll.loan.calculation.models.CalculateModel
import com.fll.loan.calculation.models.LoanBean
import com.fll.loan.calculation.ui.common.bounceClick
import com.fll.loan.calculation.ui.subscreen.CombinationScreen
import com.fll.loan.calculation.ui.subscreen.CommercialScreen
import com.fll.loan.calculation.ui.subscreen.ProvidentFundScreen

enum class CalculationScreen(@StringRes val title: Int, val icon: ImageVector) {
    CommercialLoan(title = R.string.commercial_Loan, icon = Icons.Filled.AccountCircle),
    ProvidentFundLoan(title = R.string.provident_fund_loan, icon = Icons.Filled.Face),
    CombinationLoan(title = R.string.combination_loan, icon = Icons.Filled.AddCircle)
}

val items = listOf(
    CalculationScreen.CommercialLoan,
    CalculationScreen.ProvidentFundLoan,
    CalculationScreen.CombinationLoan,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LonCalculationMain(
    modifier: Modifier = Modifier,
    calculateModel: CalculateModel,
    navController: NavHostController = rememberNavController(),
    onCalculateClicked: (loan: LoanBean) -> Unit = {},
    onFloatBtnClicked: () -> Unit = {},
) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = CalculationScreen.valueOf(
        backStackEntry?.destination?.route ?: CalculationScreen.CommercialLoan.name
    )

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier.statusBarsPadding(),
                title = { Text(stringResource(currentScreen.title)) },
            )
        },

        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                windowInsets = WindowInsets.navigationBars
            ) {
                items.forEach { screen ->
                    BottomNavigationItem(
                        modifier = modifier.padding(top = 8.dp),
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = {
                            Text(
                                stringResource(screen.title),
                                textAlign = TextAlign.Center
                            )
                        },
                        selected = currentScreen.name.let { currentScreen.name == screen.name },
                        onClick = {
                            navController.navigate(screen.name) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.bounceClick(),
                onClick = {
                    onFloatBtnClicked()
                },
                shape = CircleShape,
            ) {
                Icon(Icons.Filled.Settings, stringResource(id = R.string.settings))
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CalculationScreen.CommercialLoan.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = CalculationScreen.CommercialLoan.name) {
                CommercialScreen(
                    onCalculateClicked = onCalculateClicked,
                    calculateModel = calculateModel
                )
            }
            composable(route = CalculationScreen.ProvidentFundLoan.name) {
                ProvidentFundScreen(
                    calculateModel = calculateModel,
                    onCalculateClicked = onCalculateClicked
                )
            }
            composable(route = CalculationScreen.CombinationLoan.name) {
                CombinationScreen(
                    calculateModel = calculateModel,
                    onCalculateClicked = onCalculateClicked
                )
            }
        }
    }
}