package com.fll.loan.calculation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.fll.loan.calculation.ui.theme.LoanCalculationTheme

enum class ResultItemLayoutType {
    HeaderTxt,
    ResultTxt,
}

@Composable
fun ResultItemLayout(
    resultItemLayoutType: ResultItemLayoutType,
    itemTitle: String,
    itemValue: String?,
    modifier: Modifier = Modifier
) {
    when (resultItemLayoutType) {
        ResultItemLayoutType.HeaderTxt -> {
            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {

                val (titleRef, divider) = createRefs()

                Text(text = itemTitle,
                    fontSize = 12.sp,
                    modifier = modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .constrainAs(titleRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.wrapContent
                        })

                HorizontalDivider(modifier = modifier.constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                    width = Dimension.matchParent
                }, thickness = 0.5.dp)
            }
        }

        ResultItemLayoutType.ResultTxt -> {
            ConstraintLayout(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .height(64.dp)
            ) {

                val (titleRef, valueRef, divider) = createRefs()

                Text(text = itemTitle,
                    modifier = modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .constrainAs(titleRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.wrapContent
                        })

                itemValue?.apply {
                    Text(text = itemValue,
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .constrainAs(valueRef) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.wrapContent
                            })
                }

                HorizontalDivider(modifier = modifier.constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                    width = Dimension.matchParent
                }, thickness = 0.5.dp)
            }
        }
    }
}

@Preview
@Composable
fun ResultItemLayoutPreview() {
    LoanCalculationTheme {
        Column {
            ResultItemLayout(
                resultItemLayoutType = ResultItemLayoutType.HeaderTxt,
                itemTitle = "title1",
                itemValue = "金额"
            )
            ResultItemLayout(
                resultItemLayoutType = ResultItemLayoutType.ResultTxt,
                itemTitle = "title2",
                itemValue = "金额"
            )
        }
    }
}