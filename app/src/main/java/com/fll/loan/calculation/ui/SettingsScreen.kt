package com.fll.loan.calculation.ui


import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.fll.loan.calculation.R
import com.fll.loan.calculation.SettingsActivity.Companion.COM_RATE_DEFAULT
import com.fll.loan.calculation.SettingsActivity.Companion.FUND_RATE_DEFAULT
import com.fll.loan.calculation.SettingsActivity.Companion.SP_COM_RATE_BASELINE
import com.fll.loan.calculation.SettingsActivity.Companion.SP_FUND_RATE_BASELINE
import com.fll.loan.calculation.ui.common.ListItemPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMain(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
    sp: SharedPreferences,
) {
    var isSelectDialogShowed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier.statusBarsPadding(),
                title = { Text(stringResource(id = R.string.settings)) },
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
        val currentItemSpKey = remember { mutableStateOf(SP_COM_RATE_BASELINE) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            HorizontalDivider(thickness = 0.5.dp)
            SettingsItemLayout(itemTitle = stringResource(id = R.string.commercial_loan_rate_baseline),
                itemValue = sp.getFloat(SP_COM_RATE_BASELINE, COM_RATE_DEFAULT).toString() + " %",
                onItemClicked = {
                    isSelectDialogShowed = true
                    currentItemSpKey.value = SP_COM_RATE_BASELINE
                })
            HorizontalDivider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.size(4.dp))

            SettingsItemLayout(itemTitle = stringResource(id = R.string.fund_loan_rate_baseline),
                itemValue = sp.getFloat(
                SP_FUND_RATE_BASELINE, FUND_RATE_DEFAULT).toString() + " %",
                onItemClicked = {
                    isSelectDialogShowed = true
                    currentItemSpKey.value = SP_FUND_RATE_BASELINE
                })
            HorizontalDivider(thickness = 0.5.dp)
        }

        val inputText = remember { mutableStateOf(TextFieldValue()) }
        val pattern = remember { Regex("\\d*\\.?\\d*") }

        if (isSelectDialogShowed) {
            AlertDialog(
                onDismissRequest = {
                    isSelectDialogShowed = false
                },
                title = {
                    Text(
                        text = "Please input",
                        fontWeight = FontWeight.W700,
                        fontSize = 18.sp
                    )
                },
                text = {
                    // todo input value
                    OutlinedTextField(
                        value = inputText.value,
                        label = { Text("Rate") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        onValueChange = {
                            if (it.text.matches(pattern)) {
                                inputText.value = it
                            }
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isSelectDialogShowed = false
                            if (inputText.value.text.isNotEmpty() and (inputText.value.text.matches(pattern))) {
                                with(sp.edit()) {
                                    putFloat(currentItemSpKey.value, inputText.value.text.toFloat())
                                    apply()
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

@Composable
fun SettingsItemLayout(
    itemTitle: String,
    itemValue: String?,
    onItemClicked: () -> Unit = { },
    modifier: Modifier = Modifier,
) {
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
                .widthIn(48.dp, 512.dp)
                .constrainAs(titleRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.wrapContent
                })

        itemValue?.apply {
            Button(onClick = { onItemClicked() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                modifier = modifier
                    .widthIn(64.dp, 192.dp)
                    .constrainAs(valueRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {

                Text(text = itemValue)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        }

        HorizontalDivider(modifier = modifier.constrainAs(divider) {
            bottom.linkTo(parent.bottom)
            width = Dimension.matchParent
        }, thickness = 0.5.dp)
    }
}
