package kg.notesapp.notes.ui.theme

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kg.notesapp.notes.data.model.Note
import kg.notesapp.notes.data.room.AppDatabase
import kg.notesapp.notes.data.room.RoomRepo
import kg.notesapp.notes.utils.REPOSITORY
import kg.notesapp.notes.utils.TYPE_LOCAL
import kg.notesapp.notes.utils.TYPE_REMOTE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val context = application

    fun initDatabase(initType: String, onSuccess: () -> Unit) {
        Log.d("checkData", "MainViewModel initDatabase type:$initType")
        when (initType) {
            TYPE_LOCAL -> {
                val dao = AppDatabase.getInstance(context).noteDao()
                REPOSITORY = RoomRepo(dao)
                onSuccess()
            }
        }
    }

    fun addNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY?.create(note = note) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun readAllNotes() = REPOSITORY?.readAll

    fun update(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY?.update(note = note) {
                viewModelScope.launch(Dispatchers.Main) { onSuccess() }
            }
        }
    }

    fun delete(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY?.delete(note = note) {
                viewModelScope.launch(Dispatchers.Main) { onSuccess() }
            }
        }
    }
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(application = application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}