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
import androidx.compose.runtime.MutableState
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
import com.fll.loan.calculation.ui.common.bounceClick


@Composable
fun CommercialScreen(
    modifier: Modifier = Modifier,
    calculateModel: CalculateModel,
    onCalculateClicked: (loan: LoanBean) -> Unit = {},
) {
    val totalItemLists = listOf(
        CommercialList.CalculationMethod,
        CommercialList.CommercialAmount,
        CommercialList.AreaSquare,
        CommercialList.UnitPrint,
        CommercialList.DownPaymentRatio,
        CommercialList.ActualCommercialRate,
        CommercialList.LoanTerm
    )

    val percentStr = " " + stringResource(id = R.string.percent)
    val yearStr = " " + stringResource(id = R.string.year)
    val percentSymbolStr = " " + stringResource(id = R.string.percent_symbol)

    var isSelectDialogShowed by remember { mutableStateOf(false) }
    val byAmount = remember { mutableStateOf(true) }

    val rateLists = calculateModel.getCommercialRateLists()
    val dateLists = calculateModel.getDateLists()
    val percentLists = calculateModel.getPercentLists()
    var currentDialogLists by remember { mutableStateOf(rateLists) }

    var rate by remember { mutableStateOf(rateLists[3]) }
    var yearCount by remember { mutableStateOf(dateLists[dateLists.lastIndex]) }
    var state by remember { mutableStateOf(rate) }
    var percent by remember { mutableStateOf(percentLists[0]) }

    val commercialAmount = remember {
        mutableFloatStateOf(0.0F)
    }
    val totalArea = remember {
        mutableFloatStateOf(0.0F)
    }
    val unitPrice = remember {
        mutableFloatStateOf(0.0F)
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current

        totalItemLists.forEach { item ->
            when (item) {
                CommercialList.CalculationMethod -> {
                    CalculateItemLayout(calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        button1Title = stringResource(id = R.string.by_amount),
                        button2Title = stringResource(id = R.string.by_area),
                        button1Click = byAmount,
                        onFirstButtonClicked = {
                            totalItemLists[1].visibility.value = true
                            totalItemLists[2].visibility.value = false
                            totalItemLists[3].visibility.value = false
                            totalItemLists[4].visibility.value = false
                        }, onSecondButtonClicked = {
                            totalItemLists[1].visibility.value = false
                            totalItemLists[2].visibility.value = true
                            totalItemLists[3].visibility.value = true
                            totalItemLists[4].visibility.value = true
                        })
                }

                CommercialList.CommercialAmount -> {
                    if (item.visibility.value) {
                        CalculateItemLayout(calculateItemLayoutType = item.calculateItemLayoutType,
                            itemTitle = stringResource(id = item.title),
                            inputHint = stringResource(
                                id = R.string.input_amount
                            ),
                            onInputValueChange = {
                                commercialAmount.floatValue = it.toFloat()
                            })
                    }
                }

                CommercialList.AreaSquare -> {
                    if (item.visibility.value) {
                        CalculateItemLayout(calculateItemLayoutType = item.calculateItemLayoutType,
                            itemTitle = stringResource(id = item.title),
                            inputHint = stringResource(
                                id = R.string.input_area
                            ),
                            onInputValueChange = {
                                totalArea.floatValue = it.toFloat()
                            })
                    }
                }

                CommercialList.UnitPrint -> {
                    if (item.visibility.value) {
                        CalculateItemLayout(calculateItemLayoutType = item.calculateItemLayoutType,
                            itemTitle = stringResource(id = item.title),
                            inputHint = stringResource(
                                id = R.string.input_price
                            ),
                            onInputValueChange = {
                                unitPrice.floatValue = it.toFloat()
                            })
                    }
                }

                CommercialList.DownPaymentRatio -> {
                    if (item.visibility.value) {
                        CalculateItemLayout(
                            calculateItemLayoutType = item.calculateItemLayoutType,
                            itemTitle = stringResource(id = item.title),
                            selectValue = percent + percentStr,
                            onSelectClicked = {
                                isSelectDialogShowed = true
                                currentDialogLists = percentLists
                                state = percent
                            })
                    }
                }

                CommercialList.ActualCommercialRate -> {
                    CalculateItemLayout(
                        calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        selectValue = rate + percentSymbolStr,
                        onSelectClicked = {
                            isSelectDialogShowed = true
                            currentDialogLists = rateLists
                            state = rate
                        })
                }

                CommercialList.LoanTerm -> {
                    CalculateItemLayout(
                        calculateItemLayoutType = item.calculateItemLayoutType,
                        itemTitle = stringResource(id = item.title),
                        selectValue = yearCount + yearStr,
                        onSelectClicked = {
                            isSelectDialogShowed = true
                            currentDialogLists = dateLists
                            state = yearCount
                        })
                }
            }
        }


        Button(modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .bounceClick(), onClick = {
            // calculate
            if (byAmount.value and (commercialAmount.floatValue > 0.0f)) {
                onCalculateClicked(
                    LoanBean(
                        amount = commercialAmount.floatValue,
                        rate = rate.toFloat(),
                        yearCount = yearCount.toInt()
                    )
                )
            } else if ((totalArea.floatValue > 0.0f) and (unitPrice.floatValue > 0.0f)) {
                val areaAmount =
                    (unitPrice.floatValue * totalArea.floatValue) / 10000 * (10 - percent.toInt()) / 10
                onCalculateClicked(
                    LoanBean(
                        amount = areaAmount,
                        rate = rate.toFloat(),
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
                                rateLists -> {
                                    it + percentSymbolStr
                                }

                                dateLists -> {
                                    it + yearStr
                                }

                                percentLists -> {
                                    it + percentStr
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
                            // todo
                            when (currentDialogLists) {
                                rateLists -> {
                                    rate = state
                                }

                                dateLists -> {
                                    yearCount = state
                                }

                                percentLists -> {
                                    percent = state
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

enum class CommercialList(
    @StringRes val title: Int,
    val calculateItemLayoutType: CalculateItemLayoutType,
    val visibility: MutableState<Boolean>
) {
    CalculationMethod(
        title = R.string.calculation_method,
        calculateItemLayoutType = CalculateItemLayoutType.TextAnd2Btn,
        visibility = mutableStateOf(true)
    ),
    CommercialAmount(
        title = R.string.commercial_Loan_amount,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndInput,
        visibility = mutableStateOf(true)
    ),
    AreaSquare(
        title = R.string.area_square_meters,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndInput,
        visibility = mutableStateOf(false)
    ),
    UnitPrint(
        title = R.string.unit_price,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndInput,
        visibility = mutableStateOf(false)
    ),
    DownPaymentRatio(
        title = R.string.down_payment_ratio,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndSelect,
        visibility = mutableStateOf(false)
    ),
    ActualCommercialRate(
        title = R.string.actual_commercial_loan_rate,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndSelect,
        visibility = mutableStateOf(true)
    ),
    LoanTerm(
        title = R.string.loan_term,
        calculateItemLayoutType = CalculateItemLayoutType.TextAndSelect,
        visibility = mutableStateOf(true)
    )
}