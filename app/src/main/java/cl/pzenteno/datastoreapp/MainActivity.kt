package cl.pzenteno.datastoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.pzenteno.datastoreapp.ui.theme.DataStoreAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkModeStore = StoreDarkMode(this)
            val darkMode = darkModeStore.getDarkMode.collectAsState(initial = false)
            DataStoreAppTheme(
                darkTheme = darkMode.value
            ) {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(darkModeStore, darkMode.value)
                }
            }
        }
    }
}

@Composable
fun Greeting(darkModeStore: StoreDarkMode, darkMode: Boolean) {
    val context = LocalContext.current;
    val scope = rememberCoroutineScope()
    val dataStore = StoreUserEmail(context)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val userEmail = dataStore.getEmail.collectAsState(initial = "")
        var email by rememberSaveable { mutableStateOf("") }
        TextField(value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                dataStore.saveEmail(email)
            }
        }) {
            Text(text = "Guardar Email")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = userEmail.value)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                if (darkMode) {
                    darkModeStore.saveDarkMode(false)
                } else {
                    darkModeStore.saveDarkMode(true)
                }
            }
        }) {
            Text(text = "Dark Mode")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Switch(
            checked = darkMode, onCheckedChange = {checked->
            scope.launch {
                darkModeStore.saveDarkMode(checked)
            }
        })
    }
}
