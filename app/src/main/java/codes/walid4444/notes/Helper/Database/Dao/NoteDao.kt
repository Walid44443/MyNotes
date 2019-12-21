package codes.walid4444.notes.Helper.Database.Dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import codes.walid4444.notes.Helper.Database.Model.Note

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note?)

    @Update
    fun Update(note: Note?)

    @Delete
    fun Delete(note: Note?)

    @Query("DELETE FROM Note")
    fun DeleteAllNotes()

    @get:Query("SELECT * from Note ORDER BY priority,last_modify ASC")
    val notes: LiveData<List<Note>>?

    @Query("SELECT * from Note WHERE id = :id")
    fun getNote(id : String) :  LiveData<Note>?

    @Query("SELECT * FROM Note ORDER BY priority,last_modify ASC")
    fun getAllPaged() : DataSource.Factory<Int, Note>
}