package codes.walid4444.notes.Fragment.ViewModel

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import codes.walid4444.notes.Helper.SharedPrefManger
import codes.walid4444.notes.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginViewModel : ViewModel() {
    lateinit var mContext: Context;
    var mAuth = FirebaseAuth.getInstance()
    private var navController : NavController? = null;
    private lateinit var emailTxt : EditText;
    private lateinit var passwordTxt : EditText;
    private lateinit var confrim_login_btn : Button;
    private lateinit var register_account_btn : Button;

    fun init(view: View,emailTxt: EditText,passwordTxt: EditText,confrim_login_btn : Button,register_account_btn : Button) {
        this.mContext = view.context
        this.navController = Navigation.findNavController(view!!);
        this.emailTxt = emailTxt
        this.passwordTxt = passwordTxt
        this.confrim_login_btn = confrim_login_btn
        this.register_account_btn = register_account_btn
    }

    private fun login(emailTxt: EditText, passwordTxt: EditText) {
        var email = emailTxt.text.trim().toString()
        var password = passwordTxt.text.trim().toString()

        if (!email.isEmpty() && !password.isEmpty()) {
            this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mContext as Activity,
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            var sharedPrefManger :SharedPrefManger = SharedPrefManger(mContext)
                            mAuth.getCurrentUser()!!.getIdToken(true).addOnCompleteListener {
                                sharedPrefManger.Login(email,password,it.getResult()!!.token)
                            }

                            navController!!.navigate(R.id.action_loginFragment_to_homeFragment)
                            Toast.makeText(mContext, "Successfully Logged in :)", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(mContext, "Error Logging in :(", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })

        } else {
            Toast.makeText(mContext, "Please fill up the Credentials :|", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClick(view: View?){
        when(view!!.id){
            R.id.confrim_login_btn-> login(emailTxt,passwordTxt)
            R.id.register_account_btn -> navController!!.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
