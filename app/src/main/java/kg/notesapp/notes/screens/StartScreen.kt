package kg.notesapp.notes.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kg.notesapp.notes.navigation.NavRoute
import kg.notesapp.notes.ui.theme.MainViewModel
import kg.notesapp.notes.ui.theme.MainViewModelFactory
import kg.notesapp.notes.ui.theme.NotesTheme
import kg.notesapp.notes.utils.TYPE_LOCAL
import kg.notesapp.notes.utils.TYPE_REMOTE

@Composable
fun StartScreen(navController: NavHostController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val mViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Where to save?")
            Button(onClick = {
                mViewModel.initDatabase(TYPE_LOCAL) {
                    navController.navigate(route = NavRoute.Main.route)
                }
            },
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 8.dp)) {
                Text(text = "Local database")
            }
            Button(onClick = {
                mViewModel.initDatabase(TYPE_REMOTE) {
                    navController.navigate(route = NavRoute.Main.route)
                }
            },
                modifier = Modifier
                    .width(200.dp)
                    .padding(vertical = 8.dp)) {
                Text(text = "Remote database")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevStartScreen() {
    NotesTheme {
        val context = LocalContext.current
        val mViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

        StartScreen(navController = rememberNavController(), viewModel = mViewModel)
    }
}