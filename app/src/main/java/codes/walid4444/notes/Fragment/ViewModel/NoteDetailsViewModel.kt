package codes.walid4444.notes.Fragment.ViewModel

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import codes.walid4444.notes.Helper.Database.Model.Callbacks.NoteCallBack
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.Helper.NoteRepository
import codes.walid4444.notes.Helper.Utility
import codes.walid4444.notes.R
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.madrapps.pikolo.ColorPicker
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import java.util.*


class NoteDetailsViewModel : ViewModel() {
    private lateinit var mContext : Context;
    private var noteColorStr : String = "#f39c12";
    private lateinit var colorPickerView : ColorPicker;
    private lateinit var noteLiveItem : LiveData<Note>;
    lateinit var note_title_edt : EditText;
    lateinit var note_content_edt : EditText;
    lateinit var numberPicker : NumberPicker;
    lateinit var repository :NoteRepository;
    lateinit var note_details_layout : ConstraintLayout;
    private var navController : NavController? = null;
    private  var item_id : Int =0;

    //Random Colors would be default assigned to color pick
    var colorList  = arrayOf(
        "#dfe6e9".toUpperCase(),
        "#a29bfe".toUpperCase(),
        "#74b9ff".toUpperCase(),
        "#81ecec".toUpperCase(),
        "#55efc4".toUpperCase(),
        "#b2bec3".toUpperCase(),
        "#6c5ce7".toUpperCase(),
        "#0984e3".toUpperCase(),
        "#00cec9".toUpperCase(),
        "#00b894".toUpperCase(),
        "#636e72".toUpperCase(),
        "#fd79a8".toUpperCase(),
        "#d63031".toUpperCase(),
        "#fdcb6e".toUpperCase(),
        "#ffeaa7".toUpperCase(),
        "#f1c40f".toUpperCase(),
        "#f39c12".toUpperCase(),
        "#e74c3c".toUpperCase()
    )


    lateinit var db_ref  : DatabaseReference
    fun Init(v : View ,note_details_layout : ConstraintLayout ,colorPickerView : ColorPicker,note_title_edt : EditText , note_content_edt : EditText, numberPicker: NumberPicker,item_id : Int){
        mContext = v.context
        repository = NoteRepository(mContext)
        this.navController = Navigation.findNavController(v!!);

        this.item_id = item_id
        this.colorPickerView = colorPickerView;
        this.note_title_edt = note_title_edt
        this.note_content_edt = note_content_edt
        this.numberPicker = numberPicker;
        this.note_details_layout =note_details_layout;

        db_ref = FirebaseDatabase.getInstance()!!.getReference("Notes");

        repository.getNote(item_id, object : NoteCallBack {
            override fun onSuccess(noteLiveData: LiveData<Note>) {
                noteLiveItem = noteLiveData
                (mContext as Activity).runOnUiThread(Runnable { BindValues() })
            }
        })
        

        colorPickerView.setColorSelectionListener(object : SimpleColorSelectionListener() {
            override fun onColorSelected(color: Int) {
                noteColorStr = java.lang.String.format("#%06X", 0xFFFFFF and color)
                note_details_layout.setBackgroundTintList(ColorStateList.valueOf(color) )
                changeColorsAttr(noteColorStr)

            }
        })

    }

    fun BindValues(){
        numberPicker.minValue = 0;
        numberPicker.maxValue = 5;

        if (::noteLiveItem.isInitialized && item_id!=0){
                noteLiveItem.observe(mContext as LifecycleOwner, androidx.lifecycle.Observer {
                    if (it !=null) {
                        note_title_edt.setText(it.title);
                        note_content_edt.setText(it.description);
                        numberPicker.value = (it.priority);
                        BindUiColors(it.bg_color)
                    }
                });
        }else
            BindUiColors(noteColorStr)

    }
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: androidx.lifecycle.Observer<T>) {
        observe(lifecycleOwner, object : androidx.lifecycle.Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
    fun BindUiColors(color : String){
        noteColorStr = color
        note_details_layout.setBackgroundTintList(
            ColorStateList.valueOf(
                Color.parseColor(color)
            )
        )
        changeColorsAttr(color)
        colorPickerView.setColor(Color.parseColor(color))
    }

    fun changeColorsAttr(currentColor : String){
        this.note_title_edt.setTextColor(Utility.getContrastColor(Color.parseColor(currentColor)))
        this.note_content_edt.setTextColor(Utility.getContrastColor(Color.parseColor(currentColor)))
    }

    fun onOptionsItemSelected (item: MenuItem){
        when(item.itemId){
            R.id.save_note->saveNote();
            R.id.update_note->UpdateNote();
            R.id.delete_note->DeleteNote();

        }
    }

    fun saveNote(){
        var title : String = note_title_edt.text.toString()
        var content : String = note_content_edt.text.toString()
        var periority : Int = numberPicker.value

        if (title.trim().isEmpty() || content.trim().isEmpty()){
            Toast.makeText(mContext,"Insert Valid Title & Content",Toast.LENGTH_SHORT).show()
            return;
        }

        var note : Note = Note(title,content,periority,Date().time,noteColorStr)
        repository.insert(note)
        var FirebaseID : String = db_ref.push().key!!
        note.owner = ""
        db_ref.child(FirebaseID).setValue(note)

        navController!!.navigate(R.id.action_noteDetailsFragment_to_homeFragment)
    }


    fun UpdateNote(){
        var id = item_id
        var title : String = note_title_edt.text.toString()
        var content : String = note_content_edt.text.toString()
        var periority : Int = numberPicker.value

        if (title.trim().isEmpty() || content.trim().isEmpty()){
            Toast.makeText(mContext,"Insert Valid Title & Content",Toast.LENGTH_SHORT).show()
            return;
        }


        var note : Note = Note(title,content,periority,Date().time,noteColorStr)
        note.id = id
        repository.update(note)
        navController!!.navigate(R.id.action_noteDetailsFragment_to_homeFragment)
    }

    fun DeleteNote(){
        var id = item_id

        var note : Note = Note("","",0,5,"")
        note.id =id
        repository.delete(note)
        navController!!.navigate(R.id.action_noteDetailsFragment_to_homeFragment)
    }
    fun onClick(v: View?) {
        when (v!!.id) {
            R.id.background_color_txt -> {
                 ColorPickerDialog.Builder(v.context)
                    .setTitle("ColorPicker Dialog")
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton("Confirm",
                        ColorEnvelopeListener { envelope, fromUser ->
                            {
                                Log.i("COLOR",envelope.hexCode)
                                note_details_layout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#"+envelope.hexCode)))
                            }
                        })
                    .setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
                    .attachAlphaSlideBar(false)
                    .attachBrightnessSlideBar(false)
                    .show()
            }
        }
    }
}
