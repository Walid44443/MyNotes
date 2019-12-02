package codes.walid4444.notes.Fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import codes.walid4444.notes.Fragment.ViewModel.SplashViewModel

import codes.walid4444.notes.R
import kotlinx.android.synthetic.main.splash_fragment.*

class SplashFragment : Fragment() , View.OnClickListener {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        viewModel.init(context,enter_app_btn,enter_login_textview,loading_progress_bar,splash_screen_layout,view)
        enter_app_btn.setOnClickListener(this)
        enter_login_textview.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        viewModel.onClick(v)
        Log.i("onClick","Clicked")
    }
}
