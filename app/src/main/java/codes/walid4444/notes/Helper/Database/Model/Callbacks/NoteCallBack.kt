package codes.walid4444.notes.Helper.Database.Model.Callbacks

import androidx.lifecycle.LiveData
import codes.walid4444.notes.Helper.Database.Model.Note

interface NoteCallBack {
    fun onSuccess(noteLive : LiveData<Note>)
}