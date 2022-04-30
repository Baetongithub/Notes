package kg.notesapp.notes.data.room

import androidx.lifecycle.LiveData
import kg.notesapp.notes.data.DBRepository
import kg.notesapp.notes.data.model.Note

class RoomRepo(private val noteDao: NoteDao) : DBRepository {
    override val readAll: LiveData<List<Note>>
        get() = noteDao.getAllNotes()

    override suspend fun create(note: Note, onSuccess: () -> Unit) {
        noteDao.addNote(note)
        onSuccess()
    }

    override suspend fun update(note: Note, onSuccess: () -> Unit) {
        noteDao.update(note)
        onSuccess()
    }

    override suspend fun delete(note: Note, onSuccess: () -> Unit) {
        noteDao.update(note)
        onSuccess()
    }

    override fun signOut() {

    }

    override fun connectDB(onSuccess: () -> Unit, onFailure: (String) -> Unit) {

    }
}