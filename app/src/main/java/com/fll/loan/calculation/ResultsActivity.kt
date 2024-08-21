package com.fll.loan.calculation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fll.loan.calculation.models.LoanBean
import com.fll.loan.calculation.ui.ResultMain
import com.fll.loan.calculation.ui.theme.LoanCalculationTheme

class ResultsActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val loanBean = intent.getParcelableExtra(LOAN_KEY, LoanBean::class.java)

        setContent {
            LoanCalculationTheme {
                ResultMain(loanBean = loanBean, onBack = {
                    finish()
                })
            }
        }
    }

    companion object {
        private const val TAG = "ResultsActivity"
        const val LOAN_KEY = "loanBean"
    }
}

@Preview(showBackground = true)
@Composable
fun ResultPreview() {
    LoanCalculationTheme {
//        val lists = ArrayList<ResultItem>()
//        lists.add(ResultItem(itemType = ResultItemType.HeaderTxt, itemTitle = "1"))
//        ResultMain(resultLists = lists)
    }
}
