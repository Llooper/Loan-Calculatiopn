package com.fll.loan.calculation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.preference.PreferenceManager
import com.fll.loan.calculation.ui.SettingsMain
import com.fll.loan.calculation.ui.theme.LoanCalculationTheme

class SettingsActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        setContent {
            LoanCalculationTheme {
                SettingsMain(sp = preferences,
                    onBack = {
                        goHome()
                    })
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goHome()
            }

        })
    }

    fun goHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        private const val TAG = "SettingsActivity"
        const val SP_COM_RATE_BASELINE = "commercial_rate_baseline"
        const val COM_RATE_DEFAULT = 3.50f
        const val SP_FUND_RATE_BASELINE = "fund_rate_baseline"
        const val FUND_RATE_DEFAULT = 2.85f
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    LoanCalculationTheme {
//        val lists = ArrayList<ResultItem>()
//        lists.add(ResultItem(itemType = ResultItemType.HeaderTxt, itemTitle = "1"))
//        ResultMain(resultLists = lists)
    }
}