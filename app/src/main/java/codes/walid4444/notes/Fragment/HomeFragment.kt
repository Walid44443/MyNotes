package codes.walid4444.notes.Fragment

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import codes.walid4444.notes.Adpaters.HomeNotesAdapter
import codes.walid4444.notes.Fragment.ViewModel.HomeViewModel
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.R
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.Init(view!!)
        viewModel.RecyclerViewInit(note_home_recyclerview)

        viewModel.getMyNotes().observe(this, Observer<List<Note>> {
            viewModel.note_home_adapter.setHomeNotes(it)
        })

        add_float_btn.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        viewModel.onClick(v)
    }



    private fun sync() {
        if (verifyAvailableNetwork(context as Activity) != true) {
            Toast.makeText(context, "Syncing failed !! No internet", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Syncing , will take few seconds", Toast.LENGTH_SHORT).show()
        }
    }

    fun verifyAvailableNetwork(activity:Activity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}
