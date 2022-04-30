package kg.notesapp.notes.data.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kg.notesapp.notes.data.DBRepository
import kg.notesapp.notes.data.model.Note
import kg.notesapp.notes.utils.LOGIN
import kg.notesapp.notes.utils.PASSWORD

class FirebaseRepo : DBRepository {
    private val mAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference
        .child(mAuth.currentUser?.uid.toString())

    override val readAll: LiveData<List<Note>>
        get() = AllNotesLiveDAta()

    override suspend fun create(note: Note, onSuccess: () -> Unit) {
        val noteID = database.push().key.toString()
        val mapNotes = hashMapOf<String, Any>()

        mapNotes["firebaseID"] = noteID
        mapNotes["title"] = note.title
        mapNotes["subtitle"] = note.subtitle

        database.child(noteID)
            .updateChildren(mapNotes)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { it.message.toString() }
    }

    override suspend fun update(note: Note, onSuccess: () -> Unit) {
        val noteID = note.firebaseID
        val mapNotes = hashMapOf<String, Any>()

        mapNotes["firebaseID"] = noteID
        mapNotes["title"] = note.title
        mapNotes["subtitle"] = note.subtitle

        database.child(noteID)
            .updateChildren(mapNotes)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { it.message.toString() }
    }

    override suspend fun delete(note: Note, onSuccess: () -> Unit) {
        database.child(note.firebaseID).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {  }
    }

    override fun signOut() {
        mAuth.signOut()
    }

    override fun connectDB(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        mAuth.signInWithEmailAndPassword(LOGIN!!, PASSWORD!!)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                mAuth.createUserWithEmailAndPassword(LOGIN!!, PASSWORD!!)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it.message.toString()) }
            }
    }
}