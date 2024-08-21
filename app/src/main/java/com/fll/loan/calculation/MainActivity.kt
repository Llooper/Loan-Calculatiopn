package com.fll.loan.calculation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager
import com.fll.loan.calculation.models.CalculateModel
import com.fll.loan.calculation.models.LoanBean
import com.fll.loan.calculation.ui.LonCalculationMain
import com.fll.loan.calculation.ui.theme.LoanCalculationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val calculateModel = CalculateModel(preferences)

        setContent {
            LoanCalculationTheme {
                LonCalculationMain(
                    calculateModel = calculateModel,
                    onCalculateClicked = {
                    calculate(it) },
                    onFloatBtnClicked = { goToSettings() },
                    )
            }
        }
    }

    private fun calculate(loan: LoanBean) {
        startActivity(Intent(this, ResultsActivity::class.java).apply {
            this.putExtra(ResultsActivity.LOAN_KEY, loan)
        })
    }

    private fun goToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoanCalculationTheme {
//        LonCalculationMain()
    }
}