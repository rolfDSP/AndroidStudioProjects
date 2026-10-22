package com.example.thefirstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thefirstapp.ui.theme.DarkRed
import com.example.thefirstapp.ui.theme.TheFirstAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val greetingStore = GreetingStore(applicationContext)
        setContent {
            TheFirstAppTheme {
                MainScreen(greetingStore)
            }
        }
    }
}

@Composable
fun MainScreen(greetingStore: GreetingStore) {
    val greetingText by greetingStore.greetingText.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    MainScreenContent(
        greetingText = greetingText ?: "",
        onGreetingTextChange = { newText ->
            scope.launch { greetingStore.saveGreetingText(newText) }
        }
    )
}

@Composable
fun MainScreenContent(
    greetingText: String,
    onGreetingTextChange: (String) -> Unit
) {
    var showEditDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkRed,
        contentColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Greeting(text = greetingText)
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("Edit")
            }
        }
    }

    if (showEditDialog) {
        EditTextDialog(
            initialText = greetingText,
            onConfirm = { newText ->
                onGreetingTextChange(newText)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }
}

@Composable
fun EditTextDialog(
    initialText: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var draft by rememberSaveable { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit text") },
        text = {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                label = { Text("Text") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(draft) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun Greeting(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TheFirstAppTheme {
        MainScreenContent(
            greetingText = GreetingStore.DEFAULT_GREETING,
            onGreetingTextChange = {}
        )
    }
}
