package kg.notesapp.notes.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kg.notesapp.notes.R
import kg.notesapp.notes.data.model.Note
import kg.notesapp.notes.navigation.NavRoute
import kg.notesapp.notes.ui.theme.MainViewModel
import kg.notesapp.notes.ui.theme.MainViewModelFactory
import kg.notesapp.notes.ui.theme.NotesTheme
import kg.notesapp.notes.utils.DB_TYPE
import kg.notesapp.notes.utils.TYPE_ROOM
import kg.notesapp.notes.utils.TYPE_REMOTE
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteScreen(navController: NavHostController, viewModel: MainViewModel, noteId: String?) {
    val notes = viewModel.readAllNotes()?.observeAsState(initial = listOf())?.value
    val note = when (DB_TYPE.value) {
        TYPE_ROOM -> {
            notes?.firstOrNull { it.id == noteId?.toInt() } ?: Note()
        }
        TYPE_REMOTE -> {
            notes?.firstOrNull { it.firebaseID == noteId } ?: Note()
        }
        else -> Note()
    }

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }

    ModalBottomSheetLayout(sheetState = bottomSheetState,
        sheetElevation = 5.dp,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            Surface {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 32.dp)) {
                    Text(text = "Edit note",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp))

                    OutlinedTextField(value = title,
                        onValueChange = { title = it },
                        label = { Text(text = "Title") },
                        isError = title.isEmpty())

                    OutlinedTextField(value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text(text = "Subtitle") },
                        isError = subtitle.isEmpty())

                    Button(modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            viewModel.update(note = Note(
                                id = note.id,
                                title = title,
                                subtitle = subtitle,
                                firebaseID = note.firebaseID)) {
                                navController.navigate(NavRoute.Main.route)
                            }
                        }) {
                        Text(text = stringResource(R.string.update))
                    }
                }
            }
        }) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                    Column(modifier = Modifier.padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = note.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 32.dp)
                        )
                        Text(
                            text = note.subtitle,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            title = note.title
                            subtitle = note.subtitle
                            bottomSheetState.show()
                        }
                    }) {
                        Text(text = stringResource(id = R.string.update))
                    }
                    Button(onClick = {
                        viewModel.delete(note = note) {
                            navController.navigate(NavRoute.Main.route)
                        }
                    }) {
                        Text(text = stringResource(R.string.delete))
                    }
                    Button(modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth(),
                        onClick = { navController.navigate(NavRoute.Main.route) }) {
                        Text(text = stringResource(R.string.back))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevNoteScreen() {
    NotesTheme {
        val context = LocalContext.current
        val mViewModel: MainViewModel =
            viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

        NoteScreen(navController = rememberNavController(),
            viewModel = mViewModel,
            noteId = "1")
    }
}