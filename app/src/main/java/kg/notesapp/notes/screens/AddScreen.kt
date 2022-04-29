package kg.notesapp.notes.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kg.notesapp.notes.data.model.Note
import kg.notesapp.notes.navigation.NavRoute
import kg.notesapp.notes.ui.theme.MainViewModel
import kg.notesapp.notes.ui.theme.MainViewModelFactory
import kg.notesapp.notes.ui.theme.NotesTheme

@Composable
fun AddScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    var title by remember {
        mutableStateOf(value = "")
    }

    var subtitle by remember {
        mutableStateOf(value = "")
    }

    var isBtnEnabled by remember { mutableStateOf(false) }

    Scaffold {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "New note",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    isBtnEnabled = title.isNotEmpty() && subtitle.isNotEmpty()
                },
                label = { Text(text = "Title") },
                isError = title.isEmpty()
            )
            OutlinedTextField(
                value = subtitle,
                onValueChange = {
                    subtitle = it
                    isBtnEnabled = title.isNotEmpty() && subtitle.isNotEmpty()
                },
                label = { Text(text = "Subtitle") },
                isError = subtitle.isEmpty()
            )
            Button(
                onClick = {
                    mainViewModel.addNote(note = Note(title = title, subtitle = subtitle)) {
                        navController.navigate(NavRoute.Main.route)
                    }
                },
                enabled = isBtnEnabled,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Add note")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevAddScreen() {
    NotesTheme {
        val context = LocalContext.current
        val mViewModel: MainViewModel =
            viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

        AddScreen(navController = rememberNavController(), mainViewModel = mViewModel)
    }
}