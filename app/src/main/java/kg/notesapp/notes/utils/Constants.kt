package kg.notesapp.notes.utils

import androidx.compose.runtime.mutableStateOf
import kg.notesapp.notes.data.DBRepository

const val TYPE_ROOM = "type_local"

const val TYPE_REMOTE = "type_remote"

var REPOSITORY: DBRepository? = null

var LOGIN: String? = null
var PASSWORD: String? = null
val DB_TYPE = mutableStateOf("")