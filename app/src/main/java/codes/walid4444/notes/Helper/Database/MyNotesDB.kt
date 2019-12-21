package codes.walid4444.notes.Helper.Database

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import codes.walid4444.notes.Helper.Database.Dao.NoteDao
import codes.walid4444.notes.Helper.Database.Model.Note
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = false
)
abstract class MyNotesDB : RoomDatabase() {
    abstract fun daoAccess(): NoteDao
    private class WelcomeNoteAsyncTask(myNotesDB: MyNotesDB?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val noteDao: NoteDao
        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.insert(
                Note(
                    FirebaseFirestore.getInstance()!!.collection("notes").id,
                    "Welcome to MyNotes",
                    "Welcome to Our New Note Application",
                    1,
                    Date().time,
                    "#FFFFFF",
                    ""
                )
            )
            return null
        }

        init {
            noteDao = myNotesDB!!.daoAccess()
        }
    }

    companion object {
        private const val DATABASE_NAME = "MyNotesDB.db"
        private var INSTANCE: MyNotesDB? = null
        fun getAppDatabase(context: Context): MyNotesDB? {
            if (INSTANCE == null) {
                synchronized(MyNotesDB::class.java) {
                    if (INSTANCE == null) INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            MyNotesDB::class.java,
                            DATABASE_NAME
                        )
                            .addCallback(roomCallback)
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                WelcomeNoteAsyncTask(INSTANCE).execute()
            }
        }
    }
}