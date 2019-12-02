package codes.walid4444.notes.Fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import codes.walid4444.notes.Fragment.ViewModel.RegisterViewModel

import codes.walid4444.notes.R
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment : Fragment() , View.OnClickListener {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        viewModel.init(view!!,emailTxt,passwordTxt,nameTxt,regBtn)
        regBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        viewModel.onClick(v!!)
    }
}
