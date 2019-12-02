package codes.walid4444.notes.Fragment.ViewModel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import codes.walid4444.notes.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {
    lateinit var mContext: Context;
    var mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference

    private var navController : NavController? = null;
    private lateinit var emailTxt : EditText;
    private lateinit var passwordTxt : EditText;
    private lateinit var nameTxt : EditText;
    private lateinit var confirm_register_btn : Button;

    fun init(view: View,emailTxt: EditText,passwordTxt: EditText,nameTxt : EditText,confirm_register_btn : Button) {
        this.mContext = view.context
        this.navController = Navigation.findNavController(view!!);
        this.emailTxt = emailTxt
        this.passwordTxt = passwordTxt
        this.nameTxt = nameTxt
        this.confirm_register_btn = confirm_register_btn

        mDatabase = FirebaseDatabase.getInstance().getReference("Names")
    }

    private fun registerUser (emailTxt : EditText ,passwordTxt : EditText ,nameTxt : EditText) {
        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
        var name = nameTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(mContext as Activity, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val uid = user!!.uid
                    mDatabase.child(uid).child("Name").setValue(name)
                    navController!!.navigate(R.id.action_registerFragment_to_loginFragment)
                    Toast.makeText(mContext, "Successfully registered :)", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(mContext, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
                    Log.e("Firebase",task.exception.toString())
                }
            })
        }else {
            Toast.makeText(mContext,"Please fill up the Credentials :|", Toast.LENGTH_LONG).show()
        }
    }

    fun onClick(view : View){
        when(view!!.id){
            R.id.regBtn->registerUser(emailTxt,passwordTxt,nameTxt)
        }
    }

}
