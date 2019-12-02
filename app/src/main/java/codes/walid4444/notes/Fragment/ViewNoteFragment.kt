package codes.walid4444.notes.Fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import codes.walid4444.notes.Fragment.ViewModel.ViewNoteViewModel

import codes.walid4444.notes.R

class ViewNoteFragment : Fragment() {

    companion object {
        fun newInstance() = ViewNoteFragment()
    }

    private lateinit var viewModel: ViewNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_note_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
