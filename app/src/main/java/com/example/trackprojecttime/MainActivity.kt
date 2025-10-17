package com.example.trackprojecttime

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackprojecttime.ui.theme.TrackProjectTimeTheme
import java.time.LocalDateTime
import java.time.Duration
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackProjectTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ClockInOut(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClockInOut(modifier: Modifier = Modifier) {
    var projectName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    val currentTime = remember {mutableStateOf(LocalDateTime.now())}
    val buttonText = remember {mutableStateOf("Clock In")}
    val clockedInStatus = remember {mutableStateOf(false)}
    val titleString = remember {mutableStateOf("""
        Clocked Out
        
        
    """.trimIndent())}
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()

    ) {
        Text(
            text = titleString.value,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                value = projectName,
                onValueChange = {projectName = it},
                singleLine = true,
                label = {Text(stringResource(R.string.projectName))},
                readOnly = clockedInStatus.value,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            TextField(
                value = taskDescription,
                onValueChange = {taskDescription = it},
                singleLine = true,
                label = {Text(stringResource(R.string.taskDescription))},
                readOnly = clockedInStatus.value,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        Button(
            onClick = {
                when (clockedInStatus.value) {
                    false -> {
                        clockIn(currentTime, clockedInStatus, buttonText, titleString)
                    }
                    true -> {
                        clockOut(currentTime, clockedInStatus, buttonText, titleString)
                    }
                }
            }
        ) {
            Text(buttonText.value)
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProjectMenu() {
//    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4")
//    var expanded by remember {mutableStateOf(false)}
//    var selectedOptionText by remember { mutableStateOf(options[0]) }
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = {expanded = !expanded},
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        TextField(
//            value = selectedOptionText,
//            onValueChange = {},
//            readOnly = true,
//            label = {Text("Select an option")},
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
//            modifier = Modifier
//                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
//        )
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = {expanded = false}
//        ) {
//            options.forEach { selectionOption ->
//                DropdownMenuItem(
//                    text = {Text(selectionOption)},
//                    onClick = {
//                        selectedOptionText = selectionOption
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
fun clockIn(currentTime: MutableState<LocalDateTime>, clockStatus: MutableState<Boolean>, buttonText: MutableState<String>, titleString: MutableState<String>) {
    currentTime.value = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("hh:mm")
    clockStatus.value = true
    buttonText.value = "Clock Out"
    titleString.value = """
        Clocked In
        ${currentTime.value.format(formatter)}
        
    """.trimIndent()
}

@RequiresApi(Build.VERSION_CODES.O)
fun clockOut(currentTime: MutableState<LocalDateTime>, clockStatus: MutableState<Boolean>, buttonText: MutableState<String>, titleString: MutableState<String>) {
    val workTime = Duration.between(currentTime.value, LocalDateTime.now())
    clockStatus.value = false
    buttonText.value = "Clock In"
    titleString.value = """
        Clocked Out
        You worked for
        ${workTime.toHours()} hours and ${workTime.toMinutes()} minutes
    """.trimIndent()
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TimeClockPreview() {
    TrackProjectTimeTheme {
        ClockInOut()
    }
}