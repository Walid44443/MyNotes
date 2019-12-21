package codes.walid4444.notes.Helper

import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import codes.walid4444.notes.Helper.Database.Model.Note
import com.google.firebase.firestore.QuerySnapshot


class Utility {
    companion object {
        val TAG = "Utility"

        @ColorInt
        fun getContrastColor(@ColorInt color: Int): Int { // Counting the perceptive luminance - human eye favors green color...
            val a =
                1 - (0.299 * Color.red(color) + 0.587 * Color.green(
                    color
                ) + 0.114 * Color.blue(color)) / 255
            val d: Int
            d = if (a < 0.5) {
                0 // bright colors - black font
            } else {
                255 // dark colors - white font
            }
            return Color.rgb(d, d, d)
        }


        fun fireStoreItemsToNoteList(documents: QuerySnapshot) : List<Note>{
            var noteList : MutableList<Note> = ArrayList<Note>()
            for (d in documents) {
                var note :Note = Note(d.get("id") as String,d.get("title") as String,d.get("description") as String,(d.get("priority") as Long).toInt(),d.get("last_modify") as Long,d.get("bg_color") as String,d.get("owner_email") as String)
                noteList.add(note)
            }
            return noteList;
        }
    }
}
