package codes.walid4444.notes.Fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import codes.walid4444.notes.Fragment.ViewModel.LoginViewModel

import codes.walid4444.notes.R
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.init(view!!,email_edt,password_edt,confrim_login_btn,register_account_btn)
        confrim_login_btn.setOnClickListener(this)
        register_account_btn.setOnClickListener(this)
        skip_txv.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        viewModel.onClick(v)
    }
}
