package kg.notesapp.notes.data

import androidx.lifecycle.LiveData
import kg.notesapp.notes.data.model.Note

interface DBRepository {

    val readAll: LiveData<List<Note>>

    suspend fun create(note: Note, onSuccess: () -> Unit)
    suspend fun update(note: Note, onSuccess: () -> Unit)
    suspend fun delete(note: Note, onSuccess: () -> Unit)

    fun signOut()

    fun connectDB(onSuccess: () -> Unit, onFailure: (String) -> Unit)
}