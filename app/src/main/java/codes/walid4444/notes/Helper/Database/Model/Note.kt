package codes.walid4444.notes.Helper.Database.Model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Note {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var title: String
    var description: String
    var priority: Int
    var last_modify: Long
    var bg_color: String
    @Ignore
    lateinit var owner :String;


    constructor(
        title: String,
        description: String,
        priority: Int,
        last_modify: Long,
        bg_color: String
        ) {
        this.title = title
        this.description = description
        this.priority = priority
        this.last_modify = last_modify
        this.bg_color = bg_color;
    }



    override fun toString(): String = id.toString()
}