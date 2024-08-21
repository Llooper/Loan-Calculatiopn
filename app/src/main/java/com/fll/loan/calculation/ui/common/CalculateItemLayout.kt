package com.fll.loan.calculation.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.fll.loan.calculation.ui.theme.LoanCalculationTheme

enum class CalculateItemLayoutType {
    TextAnd2Btn,
    TextAndInput,
    TextAndSelect
}

@Composable
fun CalculateItemLayout(
    calculateItemLayoutType: CalculateItemLayoutType,
    itemTitle: String,
    button1Title: String = "",
    button2Title: String = "",
    button1Click: MutableState<Boolean> = remember { mutableStateOf(true) },
    inputHint: String = "",
    selectValue: String = "",
    onFirstButtonClicked: () -> Unit = {},
    onSecondButtonClicked: () -> Unit = {},
    onInputValueChange: (String) -> Unit = {},
    onSelectClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    when (calculateItemLayoutType) {
        CalculateItemLayoutType.TextAnd2Btn -> {
            ConstraintLayout(
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(64.dp)
            ) {

                val (titleRef, button1Ref, button2Ref, divider) = createRefs()

                val buttonColor =
                    if (button1Click.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                val button2Color =
                    if (button1Click.value) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.primary

                Text(text = itemTitle, modifier = modifier
                    .constrainAs(titleRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.wrapContent
                    })

                Button(
                    onClick = {
                        button1Click.value = true
                        onFirstButtonClicked()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                    ),
                    modifier = modifier
                        .padding(start = 8.dp)
                        .constrainAs(button1Ref) {
                            end.linkTo(button2Ref.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Text(text = button1Title)
                }
                Button(
                    onClick = {
                        button1Click.value = false
                        onSecondButtonClicked()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = button2Color,
                    ),
                    modifier = modifier
                        .padding(start = 8.dp)
                        .constrainAs(button2Ref) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Text(text = button2Title)
                }

                HorizontalDivider(
                    modifier = modifier.constrainAs(divider) {
                        bottom.linkTo(parent.bottom)
                        width = Dimension.matchParent
                    },
                    thickness = 0.5.dp,
                )
            }
        }

        CalculateItemLayoutType.TextAndInput -> {
            ConstraintLayout(
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(64.dp)
            ) {

                val (inputTitle, inputEdit, divider) = createRefs()

                val pattern = remember { Regex("\\d*\\.?\\d*") }
                val inputTextValue = remember { mutableStateOf(TextFieldValue()) }

                Text(text = itemTitle, modifier = modifier
                    .constrainAs(inputTitle) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                        width = Dimension.wrapContent
                    })

                TextField(
                    modifier = modifier
                        .constrainAs(inputEdit) {
                            end.linkTo(parent.end)
                            centerVerticallyTo(parent)
                        }
                        .width(130.dp)
                        .padding(start = 8.dp),
                    value = inputTextValue.value,
                    singleLine = true,
                    label = {
                        Text(
                            text = inputHint,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    onValueChange = {
                        if ((it.text.length <= 10) and (it.text.matches(pattern))) {
                            inputTextValue.value = it
                            if (it.text.isNotEmpty()) {
                                onInputValueChange(inputTextValue.value.text)
                            }
                        }
                    }
                )

                HorizontalDivider(
                    modifier = modifier.constrainAs(divider) {
                        bottom.linkTo(parent.bottom)
                        width = Dimension.matchParent
                    },
                    thickness = 0.5.dp,
                )
            }
        }

        CalculateItemLayoutType.TextAndSelect -> {
            ConstraintLayout(
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(64.dp)
            ) {

                val (inputTitle, selectText, divider) = createRefs()

                Text(text = itemTitle, modifier = modifier
                    .constrainAs(inputTitle) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                        width = Dimension.wrapContent
                    })

                Button(onClick = { onSelectClicked() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    modifier = modifier
                        .constrainAs(selectText) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }) {

                    Text(text = selectValue)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                    )
                }

                HorizontalDivider(
                    modifier = modifier.constrainAs(divider) {
                        bottom.linkTo(parent.bottom)
                        width = Dimension.matchParent
                    },
                    thickness = 0.5.dp,
                )
            }
        }
    }
}

@Preview
@Composable
fun CombinationTextPreview() {
    LoanCalculationTheme {
        CalculateItemLayout(
            calculateItemLayoutType = CalculateItemLayoutType.TextAndSelect,
            itemTitle = "title1",
            inputHint = "金额"
        )
    }
}