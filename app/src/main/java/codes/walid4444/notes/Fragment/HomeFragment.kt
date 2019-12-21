package codes.walid4444.notes.Fragment

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import codes.walid4444.notes.Fragment.ViewModel.HomeViewModel
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.Helper.SharedPrefManger
import codes.walid4444.notes.R
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPrefManger: SharedPrefManger;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefManger = SharedPrefManger(context)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.Init(view!!)
        viewModel.RecyclerViewInit(note_home_recyclerview)

        viewModel.setFragment(this)

        viewModel.getMyNotes().observe(this, Observer<List<Note>> {
            viewModel.note_home_adapter.setHomeNotes(it)
            if(it.size == 0) note_items_layout.visibility = View.VISIBLE
            else note_items_layout.visibility = View.GONE
        })

        add_float_btn.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        viewModel.onClick(v)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        viewModel!!.mMenu = menu
        if (!sharedPrefManger.isLoggedIn) {
            menu.findItem(R.id.logout_app).setVisible(false)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onOptionsItemSelected(item,sharedPrefManger)
        return false
    }


}
