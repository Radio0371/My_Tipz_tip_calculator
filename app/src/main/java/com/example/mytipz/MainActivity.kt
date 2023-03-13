package com.example.mytipz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytipz.ui.theme.MyTipzTheme
import java.text.NumberFormat
import kotlin.math.round

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTipzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyTipz()
                }
            }
        }
    }
}

@Preview
@Composable
fun MyTipz() {
    var billAmountString by remember { mutableStateOf("") }
    var tipPercentString by remember { mutableStateOf("") }
    var roundUpTotal by remember { mutableStateOf(false) }
    var splitTip by remember { mutableStateOf(false) }
    var splitBillNumWays by remember { mutableStateOf(1) }

    val focusManager = LocalFocusManager.current

    val billDollars = billAmountString.toDoubleOrNull() ?: 0.0
    val tipPercent = tipPercentString.toDoubleOrNull() ?: 0.0

    val checkTotal = calculateTotal(subTotal = billDollars, tipPercent = tipPercent, round = roundUpTotal)
    val splitTotal = splitTotal(total = checkTotal, splitNum = splitBillNumWays)

    val tipCurrency = NumberFormat.getCurrencyInstance().format(checkTotal - billDollars)
    val splitCurrency = NumberFormat.getCurrencyInstance().format(splitTotal)
    val checkTotalCurrency = NumberFormat.getCurrencyInstance().format(checkTotal)


    Column(
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        TitleBanner()
        InputField(
            textLabel = R.string.bill_amount_input_label,
            value = billAmountString,
            valueChange = { billAmountString = it },
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            textLabel = R.string.tip_percent_input_label,
            value = tipPercentString,
            valueChange = { tipPercentString = it },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() } ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        SwitchControl(
            switchText = R.string.round_total,
            trueState = roundUpTotal,
            onSwitch = { roundUpTotal = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SwitchControl(
            switchText = R.string.split_tip,
            trueState = splitTip,
            onSwitch = { splitTip = it }
        )
        if (splitTip) {
            Spacer(modifier = Modifier.height(8.dp))
            SplitSelection(
                splitNum = splitBillNumWays,
                increaseSplitNum = { splitBillNumWays = increaseSplit(splitNum = splitBillNumWays) },
                decreaseSplitNum = { splitBillNumWays = decreaseSplit(splitNum = splitBillNumWays) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.tip_amount, tipCurrency),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.check_total, checkTotalCurrency),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        if (splitTip) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.per_person_total, splitCurrency),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TitleBanner() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painterResource(R.drawable.tipz_logo),
            contentDescription = stringResource(R.string.app_logo_content_desc),
            modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.tip_calculator_title),
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(48.dp))
}

@Composable
fun InputField(
    @StringRes textLabel: Int,
    value: String,
    valueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = valueChange,
        label = { Text(text = stringResource(textLabel)) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun SwitchControl(
    switchText: Int,
    trueState: Boolean,
    onSwitch: (Boolean) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        //.size(64.dp)
    ) {
        Text(
            text = stringResource(switchText),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 12.dp)
                .wrapContentWidth(Alignment.Start)
        )
        Switch(
            checked = trueState,
            onCheckedChange = onSwitch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
                .wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green,
                checkedTrackColor = colorResource(id = R.color.dark_green),
                checkedTrackAlpha = 0.8f,
                uncheckedThumbColor = Color.DarkGray,
                uncheckedTrackColor = Color.Gray,
                uncheckedTrackAlpha = 0.8f
            )
        )
    }
}

@Composable
fun SplitSelection (
    splitNum: Int,
    increaseSplitNum: () -> Unit,
    decreaseSplitNum: () -> Unit,
) {
    val plusButtonImage = when (splitNum) {
        1 -> R.drawable.active_plus_button
        else -> R.drawable.active_plus_button
    }
    val minusButtonImage = when (splitNum) {
        1 -> R.drawable.inactive_minus_button
        else -> R.drawable.active_minus_button
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 120.dp, end = 120.dp)
    ) {
        Image(painter = painterResource(id = minusButtonImage),
            contentDescription = stringResource(R.string.minus_button_context_desc),
            modifier = Modifier
                .clickable(onClick = decreaseSplitNum)
                .size(height = 40.dp, width = 40.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Surface(modifier = Modifier
            .weight(1f)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RectangleShape
            ),
            color = Color.Gray
        ) {
            Text(
                text = "$splitNum",
                fontSize = 28.sp,
                modifier = Modifier
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(start = 30.dp, end = 30.dp, top = 3.dp, bottom = 3.dp),
                //color = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Image(painter = painterResource(id = plusButtonImage),
            contentDescription = stringResource(R.string.plus_button_context_desc),
            modifier = Modifier
                .clickable(onClick = increaseSplitNum)
                .size(height = 40.dp, width = 40.dp)
        )
    }
}

fun increaseSplit(splitNum: Int): Int {
    return splitNum + 1
}

fun decreaseSplit(splitNum: Int): Int {
    val num = when(splitNum) {
        1 -> 1
        else -> splitNum - 1
    }
    return num
}

fun calculateTotal(
    subTotal: Double,
    tipPercent: Double,
    round: Boolean
): Double {
    var total = subTotal * ( 1 + tipPercent/100 )
    if (round) {
        total = round(total)
    }
    return total
}

fun splitTotal(
    total: Double,
    splitNum: Int
): Double {
    return total / splitNum
}
/*
@Composable
fun TipSelection(

) {
    Scroll
}
*/