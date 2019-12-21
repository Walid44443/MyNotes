package codes.walid4444.notes.Helper

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import codes.walid4444.notes.Helper.Database.Dao.NoteDao
import codes.walid4444.notes.Helper.Database.Model.Callbacks.NoteCallBack
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.Helper.Database.MyNotesDB

class NoteRepository(mContext: Context) {
    private lateinit var noteDao: NoteDao
    var myNotes: LiveData<List<Note>>? = null
    var PagedNotes : DataSource.Factory<Int, Note>;
    var NoteItem :  LiveData<Note>? = null

    init{
        var myNoteDB : MyNotesDB = MyNotesDB.getAppDatabase(mContext)!!
        noteDao = myNoteDB.daoAccess();
        myNotes = noteDao.notes
        PagedNotes = noteDao.getAllPaged()
    }

    fun getNote(id : String, noteCallBack: NoteCallBack) {
        Thread(Runnable {
            noteCallBack.onSuccess(noteDao.getNote(id)!!)
        }
        ).start()
    }

    fun insert(note: Note?) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note?) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }
    fun delete(note: Note?) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNoteAsyncTask(noteDao).execute()
    }

    lateinit var observable : Observer<Note>;
    fun SyncNotes(noteList: List<Note>,mContext: Context){
        for (note in noteList){
            getNote(note.id,object : NoteCallBack{
                override fun onSuccess(noteLive: LiveData<Note>) {
                    (mContext as Activity).runOnUiThread( Runnable {
                        observable =  Observer<Note> (){

                            if (it !=null) {
                                if (note.last_modify > it.last_modify)
                                    update(note)
                            }else
                                insert(note)
                        }
                        noteLive.observeOnce(mContext as LifecycleOwner,observable );
                    })
                }
            })
        }
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    private class getNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<String, Void?, LiveData<Note>?>() {
        override fun doInBackground(vararg params: String?): LiveData<Note>? {
            return  noteDao.getNote(params[0]!!)
        }
    }


    private class InsertNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Note, Void?, Void?>() {
        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.insert(notes[0])
            return null
        }
    }

    private class UpdateNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Note, Void?, Void?>() {
        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.Update(notes[0])
            return null
        }
    }

    private class DeleteNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Note, Void?, Void?>() {
        override fun doInBackground(vararg notes: Note): Void? {
            noteDao.Delete(notes[0])
            return null
        }
    }

    private class DeleteAllNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.DeleteAllNotes();
            return null;
        }
    }


    private class getAllNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.DeleteAllNotes();
            return null;
        }
    }

}