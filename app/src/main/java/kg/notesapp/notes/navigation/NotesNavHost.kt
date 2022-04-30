package kg.notesapp.notes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kg.notesapp.notes.screens.AddScreen
import kg.notesapp.notes.screens.MainScreen
import kg.notesapp.notes.screens.NoteScreen
import kg.notesapp.notes.screens.StartScreen
import kg.notesapp.notes.ui.theme.MainViewModel

sealed class NavRoute(val route: String) {
    object Start : NavRoute("start_screen")
    object Main : NavRoute("main_screen")
    object Add : NavRoute("add_screen")
    object Note : NavRoute("note_screen")
}

@Composable
fun NotesNavHost(mViewModel: MainViewModel, navController: NavHostController) {

    NavHost(navController = navController, startDestination = NavRoute.Start.route) {
        composable(NavRoute.Start.route) { StartScreen(navController = navController, viewModel = mViewModel) }
        composable(NavRoute.Main.route) { MainScreen(navController = navController, viewModel = mViewModel) }
        composable(NavRoute.Add.route) { AddScreen(navController = navController, mainViewModel = mViewModel) }
        composable(NavRoute.Note.route + "/{id}") { backStackEntry ->
            NoteScreen(navController = navController,
                viewModel = mViewModel,
                noteId = backStackEntry.arguments?.getString("id"))
        }
    }
}