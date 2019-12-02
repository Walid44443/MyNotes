package codes.walid4444.notes.Adpaters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import codes.walid4444.notes.Adpaters.CallBacks.NoteDiffCallback
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.Helper.Utility.Companion.getContrastColor
import codes.walid4444.notes.R
import java.text.SimpleDateFormat
import java.util.*


class HomeNotesAdapter (val context: Context,notes: List<Note>): PagedListAdapter<Note, HomeNotesAdapter.ViewHolder>(
    NoteDiffCallback()){
    var notes: List<Note> = ArrayList()
    var mContext :Context;
    private val FADE_DURATION = 1000 //FADE_DURATION in milliseconds


    init {
        this.notes = notes
        this.mContext = context;
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_note_item, parent, false)
        return ViewHolder(itemView)
    }

    fun setHomeNotes(Notes: List<Note>) {
        notes = Notes
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteItem = notes[position]
        holder.bind(holder.noteItem!!)

        val note_background_color = Color.parseColor(holder.noteItem!!.bg_color)
        holder.note_item_layout.backgroundTintList = ColorStateList.valueOf(note_background_color)
        //adjust text color based on background color
        holder.item_title.setTextColor(
            getContrastColor(
                note_background_color
            )
        )
        holder.item_content.setTextColor(
            getContrastColor(
                note_background_color
            )
        )
        holder.item_date.setTextColor(
            getContrastColor(
                note_background_color
            )
        )

        setFadeAnimation(holder.itemView)
        setScaleAnimation(holder.itemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{
        var noteItem: Note? = null
        var note_item_layout: ConstraintLayout
        var item_title: TextView
        var item_date: TextView
        var item_content: TextView

        init {
            note_item_layout = view.findViewById(R.id.note_item_layout)
            item_title = view.findViewById(R.id.title_txt)
            item_date = view.findViewById(R.id.date_txt)
            item_content = view.findViewById(R.id.content_txt)
            view.setOnClickListener(this)
        }

        fun bind(noteItem: Note) {
            item_title.text = noteItem!!.title
            item_date.text = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date(noteItem!!.last_modify))

            if (noteItem!!.description.length > 80 ) {
                item_content.text = StringBuilder().append(noteItem!!.description.trim().subSequence(0..80)).append("...")
            }else
                item_content.text = noteItem!!.description.trim()
        }



        override fun onClick(view: View) {
            var bundle = Bundle()
            bundle.putInt("item_id",noteItem!!.id)
            view.findNavController().navigate(R.id.action_homeFragment_to_noteDetailsFragment,bundle)
        }
    }

    private var lastPosition = -1


    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FADE_DURATION.toLong()
        view.startAnimation(anim)
    }

    private fun setScaleAnimation(view: View) {
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            0.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = FADE_DURATION.toLong()/2
        view.startAnimation(anim)
    }
}