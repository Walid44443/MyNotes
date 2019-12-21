package codes.walid4444.notes.Fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import codes.walid4444.notes.Fragment.ViewModel.NoteDetailsViewModel
import codes.walid4444.notes.R
import kotlinx.android.synthetic.main.note_details_fragment.*


class NoteDetailsFragment : Fragment() , View.OnClickListener{

    companion object {
        fun newInstance() = NoteDetailsFragment()
    }

    private lateinit var viewModel: NoteDetailsViewModel

    var item_id : String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("item_id","")?.let {
            item_id = it
        }
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.note_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(NoteDetailsViewModel::class.java)
        viewModel.Init(view!!,note_details_layout,colorPicker,note_title_edt,note_content_edt,periority_picker,item_id)
        background_color_txt.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (item_id.isEmpty())
            inflater.inflate(R.menu.update_menu,menu)
        else
            inflater.inflate(R.menu.add_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onOptionsItemSelected(item)
        return false
    }

    override fun onClick(v: View?) {
        viewModel.onClick(v)
    }
}