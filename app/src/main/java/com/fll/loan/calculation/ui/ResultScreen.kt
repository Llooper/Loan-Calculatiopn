package com.fll.loan.calculation.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fll.loan.calculation.R
import com.fll.loan.calculation.models.LoanBean
import com.fll.loan.calculation.models.ResultModel
import com.fll.loan.calculation.ui.common.ResultItemLayout
import com.fll.loan.calculation.ui.common.ResultItemLayoutType

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultMain(
    modifier: Modifier = Modifier,
    loanBean: LoanBean?,
    onBack: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier.statusBarsPadding(),
                title = { Text( stringResource(id = R.string.calculate_result) ) },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            HorizontalDivider(thickness = 0.5.dp)

            val resultModel = ResultModel()
            loanBean?.apply {
                // 设置“期”
                resultModel.setValueTips(stringResource(id = R.string.term))
                resultModel.calculate(loanBean)?.forEach { item ->
                    var tips = ""
                    item.titleTips?.apply {
                        tips += " (" + stringResource(id = this) + ")"
                    }

                    ResultItemLayout(
                        resultItemLayoutType = item.itemType,
                        itemTitle = stringResource(id = item.itemTitle) + tips,
                        itemValue = item.itemValue
                    )
                }
            }
        }
    }
}

data class ResultItem(
    val itemType: ResultItemLayoutType,
    @StringRes val itemTitle: Int,
    @StringRes val titleTips: Int? = null,
    val itemValue: String? = null
)

enum class ResultTitle(@StringRes val itemTitle: Int) {
    BasicInformation(itemTitle = R.string.basic_information),
    EqualInterest(itemTitle = R.string.equal_interest),
    EqualPrincipal(itemTitle = R.string.equal_principal),
    LoanAmount(itemTitle = R.string.total_loan_amoun),
    YearCount(itemTitle = R.string.loan_term),
    MonthlyRepayment(itemTitle = R.string.monthly_repayment),
    TotalRepayment(itemTitle = R.string.total_repayment),
    TotalInterest(itemTitle = R.string.total_interest),
    FirstMonthRepayment(itemTitle = R.string.first_month_repayment),
    MonthlyDecrease(itemTitle = R.string.monthly_decrease),
}
