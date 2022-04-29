package kg.notesapp.notes.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kg.notesapp.notes.data.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM table_notes")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert
    suspend fun addNote(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}