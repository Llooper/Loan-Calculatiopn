package com.fll.loan.calculation.ui.subscreen

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fll.loan.calculation.R
import com.fll.loan.calculation.models.CalculateModel
import com.fll.loan.calculation.models.LoanBean
import com.fll.loan.calculation.ui.common.CalculateItemLayout
import com.fll.loan.calculation.ui.common.CalculateItemLayoutType
import com.fll.loan.calculation.ui.common.ListItemPicker

@Composable
fun CombinationScreen(
    modifier: Modifier = Modifier,
    calculateModel: CalculateModel,
    onCalculateClicked: (loan: LoanBean) -> Unit = {}
) {

    val totalItemLists = listOf(
        CombinationList.CommercialAmount,
        CombinationList.ActualCommercialRate,
        CombinationList.ProvidentFundAmount,
        CombinationList.ActualProvidentFundRate,
        CombinationList.LoanTerm
    )

    val context = LocalContext.current

    val yearStr = " " + stringResource(id = R.string.year)
    val percentSymbolStr = " " + stringResource(id = R.string.percent_symbol)

    var isSelectDialogShowed by remember { mutableStateOf(false) }
    val commercialRateLists = calculateModel.getCommercialRateLists()
    val fundRateLists = calculateModel.getFundRateLists()
    val yearLists = calculateModel.getDateLists()
    var currentDialogLists by remember { mutableStateOf(commercialRateLists) }

    var commercialRate by remember { mutableStateOf(commercialRateLists[3]) }
    var fundRate by remember { mutableStateOf(fundRateLists[3]) }
    var yearCount by remember { mutableStateOf(yearLists[yearLists.lastIndex]) }
    var state by remember { mutableStateOf(commercialRate) }
    val commercialAmount = remember { mutableFloatStateOf(0.0F) }
    val fundAmount = remember { mutableFloatStateOf(0.0F) }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {

        totalItemLists.forEach { item ->
            when (item) {
                CombinationList.CommercialAmount -> {
                    CalculateItemLayout(calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        inputHint = stringResource(
                            id = R.string.input_amount
                        ),
                        onInputValueChange = {
                            commercialAmount.floatValue = it.toFloat()
                        })
                }

                CombinationList.ActualCommercialRate -> {
                    CalculateItemLayout(
                        calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        selectValue = commercialRate + percentSymbolStr,
                        onSelectClicked = {
                            isSelectDialogShowed = true
                            currentDialogLists = commercialRateLists
                            state = commercialRate
                        })
                }

                CombinationList.ProvidentFundAmount -> {
                    CalculateItemLayout(calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        inputHint = stringResource(
                            id = R.string.input_amount
                        ),
                        onInputValueChange = {
                            fundAmount.floatValue = it.toFloat()
                        })
                }

                CombinationList.ActualProvidentFundRate -> {
                    CalculateItemLayout(
                        calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        selectValue = fundRate + percentSymbolStr,
                        onSelectClicked = {
                            isSelectDialogShowed = true
                            currentDialogLists = fundRateLists
                            state = fundRate
                        })
                }

                CombinationList.LoanTerm -> {
                    CalculateItemLayout(
                        calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        selectValue = yearCount + yearStr,
                        onSelectClicked = {
                            isSelectDialogShowed = true
                            currentDialogLists = yearLists
                            state = yearCount
                        })
                }
            }
        }


        Button(modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(), onClick = {
            if ((commercialAmount.floatValue > 0.0f) and (fundAmount.floatValue > 0.0f)) {
                onCalculateClicked(
                    LoanBean(
                        amount = commercialAmount.floatValue,
                        rate = commercialRate.toFloat(),
                        fundAmount = fundAmount.floatValue,
                        fundRate = fundRate.toFloat(),
                        yearCount = yearCount.toInt()
                    )
                )
            } else {
                Toast.makeText(context, "Please input relevant value!", Toast.LENGTH_SHORT).show()
            }
            }) {
            Text(text = stringResource(id = R.string.calculate))
        }

        if (isSelectDialogShowed) {
            AlertDialog(
                onDismissRequest = {
                    isSelectDialogShowed = false
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.select),
                        fontWeight = FontWeight.W700,
                        fontSize = 18.sp
                    )
                },
                text = {
                    ListItemPicker(
                        modifier = modifier.fillMaxWidth(),
                        dividersColor = MaterialTheme.colorScheme.error,
                        label = {
                            when (currentDialogLists) {
                                commercialRateLists, fundRateLists -> {
                                    it + percentSymbolStr
                                }

                                yearLists -> {
                                    it + yearStr
                                }

                                else -> {
                                    it
                                }
                            }
                        },
                        value = state,
                        onValueChange = { state = it },
                        list = currentDialogLists,
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isSelectDialogShowed = false
                            when (currentDialogLists) {
                                commercialRateLists -> {
                                    commercialRate = state
                                }

                                fundRateLists -> {
                                    fundRate = state
                                }

                                yearLists -> {
                                    yearCount = state
                                }
                            }
                        },
                    ) {
                        Text(
                            stringResource(id = R.string.confirm),
                            fontSize = 18.sp
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            isSelectDialogShowed = false
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontSize = 18.sp
                        )
                    }
                }
            )
        }
    }
}

enum class CombinationList(
    @StringRes val title: Int,
    val calculateItemLayoutType: CalculateItemLayoutType
) {
    CommercialAmount(
        title = R.string.commercial_Loan_amount,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndInput
    ),
    ActualCommercialRate(
        title = R.string.actual_commercial_loan_rate,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndSelect
    ),
    ProvidentFundAmount(
        title = R.string.Total_Provident_Fund_Loan,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndInput
    ),
    ActualProvidentFundRate(
        title = R.string.actual_provident_fund_loan_rate,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndSelect
    ),
    LoanTerm(
        title = R.string.loan_term,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndSelect
    )
}