package codes.walid4444.notes.Helper.Database.Model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Note {
    @PrimaryKey
    lateinit var id :String
    var title: String
    var description: String
    var priority: Int
    var last_modify: Long
    var bg_color: String
    var owner_email :String;

    @Ignore
    var isSelected : Boolean =false

    constructor(
        title: String,
        description: String,
        priority: Int,
        last_modify: Long,
        bg_color: String,
        owner_email :String
        ) {
        this.title = title
        this.description = description
        this.priority = priority
        this.last_modify = last_modify
        this.bg_color = bg_color;
        this.owner_email = owner_email;
    }

    @Ignore
    constructor(
        id: String,
        title: String,
        description: String,
        priority: Int,
        last_modify: Long,
        bg_color: String,
        owner_email: String
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.priority = priority
        this.last_modify = last_modify
        this.bg_color = bg_color
        this.owner_email = owner_email
    }


    override fun toString(): String = id.toString()
}