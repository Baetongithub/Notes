package kg.notesapp.notes.data.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kg.notesapp.notes.data.model.Note

class AllNotesLiveDAta : LiveData<List<Note>>() {
    private val mAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference
        .child(mAuth.currentUser?.uid.toString())

    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val listNotes = mutableListOf<Note>()
            snapshot.children.map {
                listNotes.add(it.getValue(Note::class.java) ?: Note())
            }
            value = listNotes
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    override fun onActive() {
        database.addValueEventListener(listener)
    }

    override fun onInactive() {
        database.removeEventListener(listener)
        super.onInactive()
    }
}