package kg.notesapp.notes.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import kg.notesapp.notes.navigation.NavRoute
import kg.notesapp.notes.ui.theme.MainViewModel
import kg.notesapp.notes.ui.theme.MainViewModelFactory
import kg.notesapp.notes.ui.theme.NotesTheme
import kg.notesapp.notes.utils.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StartScreen(navController: NavHostController, viewModel: MainViewModel) {
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ModalBottomSheetLayout(sheetState = bottomSheetState,
        sheetElevation = 5.dp,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            Surface {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 32.dp)) {
                    Text(text = stringResource(id = R.string.sign_in),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp))

                    OutlinedTextField(value = login,
                        onValueChange = { login = it },
                        label = { Text(text = "Login") },
                        isError = login.isEmpty())

                    OutlinedTextField(value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Password") },
                        isError = password.isEmpty())

                    Button(modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            LOGIN = login
                            PASSWORD = password
                            viewModel.initDatabase(TYPE_REMOTE) {
                                DB_TYPE.value = TYPE_REMOTE
                                navController.navigate(NavRoute.Main.route)
                                Toast.makeText(viewModel.context, "Success sign in", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = login.isNotEmpty() && password.isNotEmpty()) {
                        Text(text = stringResource(R.string.sign_in))
                    }
                }
            }
        }) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Where to save?")
                Button(onClick = {
                    viewModel.initDatabase(TYPE_ROOM) {
                        DB_TYPE.value = TYPE_ROOM
                        navController.navigate(route = NavRoute.Main.route)
                    }
                },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 8.dp)) {
                    Text(text = "Local database")
                }
                Button(onClick = {
                    coroutineScope.launch { bottomSheetState.show() }
                },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 8.dp)) {
                    Text(text = "Remote database")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrevStartScreen() {
    NotesTheme {
        val context = LocalContext.current
        val mViewModel: MainViewModel =
            viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

        StartScreen(navController = rememberNavController(), viewModel = mViewModel)
    }
}