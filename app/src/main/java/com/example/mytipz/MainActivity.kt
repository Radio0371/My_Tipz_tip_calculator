package com.example.mytipz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mytipz.ui.theme.MyTipzTheme

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
    var billAmount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf("") }
    var roundUpTotal by remember { mutableStateOf(false) }
    var splitTip by remember { mutableStateOf(false) }
    var splitBillNumWays by remembr { mutableStateOf(1) }
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputField(
            textLabel = R.string.bill_amount_input_label,
            value = billAmount,
            valueChange = ,
            keyboardActions = KeyboardActions(onNext = ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            textLabel = R.string.tip_percent_input_label,
            value = tipPercent,
            valueChange = ,
            keyboardActions = KeyboardActions(onDone = ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        if (splitTip == true) {
            SplitSelection(
                splitNum = splitBillNumWays,
                increaseSplitNum = { splitBillNumWays = increaseSplit(splitNum = splitBillNumWays) },
                decreaseSplitNum = { splitBillNumWays = decreaseSplit(splitNum = splitBillNumWays) }
            )
        }
    }
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
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(switchText),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.wrapContentWidth(Alignment.Start)
        )
        Switch(
            checked = trueState,
            onCheckedChange = onSwitch,
            modifier = Modifier.wrapContentWidth(Alignment.End),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green,
                checkedTrackColor = colorResource(id = R.color.dark_green),
                checkedTrackAlpha = 0.5f,
                uncheckedThumbColor = Color.DarkGray,
                uncheckedTrackColor = Color.Gray,
                uncheckedTrackAlpha = 0.0f
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
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(painter = painterResource(id = minusButtonImage),
            contentDescription = stringResource(R.string.minus_button_context_desc),
            modifier = Modifier.clickable{decreaseSplitNum}
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(modifier = Modifier.weight(0.3f)) {
            Text(
                text = "$splitNum",
                modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Image(painter = painterResource(id = plusButtonImage),
            contentDescription = stringResource(R.string.minus_button_context_desc),
            modifier = Modifier.clickable(increaseSplitNum)
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

@Composable
fun TipSelection(

) {
    Scroll
}
