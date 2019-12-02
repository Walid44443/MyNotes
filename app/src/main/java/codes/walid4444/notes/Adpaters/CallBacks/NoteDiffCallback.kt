package codes.walid4444.notes.Adpaters.CallBacks

import androidx.recyclerview.widget.DiffUtil
import codes.walid4444.notes.Helper.Database.Model.Note

class NoteDiffCallback : DiffUtil.ItemCallback<Note>(){
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.equals(newItem)
    }

}