package codes.walid4444.notes.Helper.FireStore

import android.content.Context
import android.util.Log
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.Helper.FireStore.callback.RegisterUser
import codes.walid4444.notes.Helper.FireStore.callback.onSaveNote
import codes.walid4444.notes.Helper.FireStore.callback.onUpdate
import codes.walid4444.notes.Helper.NoteRepository
import codes.walid4444.notes.Helper.Utility
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreUtility {
    val TAG = "FireStoreUtility"
    var db_ref  : FirebaseFirestore

    init {
        db_ref = FirebaseFirestore.getInstance()!!;
    }

    fun registerNewUser(name: String, email: String,uid : String,registerUser: RegisterUser) {
        db_ref.collection("users")
            .add(mapOf(
                "name" to name,
                "email" to email,
                "uid" to uid
            ))
            .addOnSuccessListener({ registerUser.onSuccess(true) })
            .addOnFailureListener({ registerUser.onSuccess(false) });
    }


    fun insertNote(note: Note,saveNote: onSaveNote) {
        note.id = db_ref.collection("notes").document().id
        db_ref.collection("notes").document(note.id)
            .set(note)
            .addOnSuccessListener(OnSuccessListener { saveNote.onSuccess(note.id) })
            .addOnFailureListener(OnFailureListener { saveNote.onError(it.message!!) });
    }

    fun updateNote(note: Note,onUpdate: onUpdate) {
        val washingtonRef = db_ref.collection("notes").document(note.id)
        washingtonRef
            .update(mapOf(
                "title" to note.title,
                "description" to note.description,
                "bg_color" to note.bg_color,
                "priority" to note.priority,
                "last_modify" to  FieldValue.serverTimestamp()
            )).addOnSuccessListener { onUpdate.onSuccess(true) }
            .addOnFailureListener({onUpdate.onSuccess(false)});
    }



    fun deleteNote(note: Note) {
        db_ref.collection("notes").document(note.id)
            .delete();
    }

    fun getNotes(email: String,mContext : Context){
        var mNotes : MutableList<Note> = ArrayList();
        var noteRepository : NoteRepository = NoteRepository(mContext);
        db_ref.collection("notes")
            //.whereEqualTo("owner_email", email)
            .get()
            .addOnSuccessListener { documents -> Utility.fireStoreItemsToNoteList(documents)
                noteRepository.SyncNotes(Utility.fireStoreItemsToNoteList(documents),mContext)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

}