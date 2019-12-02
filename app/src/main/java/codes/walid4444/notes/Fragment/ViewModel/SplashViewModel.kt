package codes.walid4444.notes.Fragment.ViewModel

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import codes.walid4444.notes.Helper.SharedPrefManger
import codes.walid4444.notes.R
import kotlinx.android.synthetic.main.splash_fragment.*

class SplashViewModel : ViewModel() {

    private var mContext :Context? =null;
    private var navController : NavController? = null;

    fun init (mContext : Context?, enter_app_btn : Button,enter_login_textview : TextView , loading_progress_bar : ProgressBar, splash_screen_layout : ViewGroup, view : View?){
        this.mContext = mContext;
        this.navController = Navigation.findNavController(view!!);

        var sharedPrefManger :SharedPrefManger = SharedPrefManger(mContext)

        if (sharedPrefManger.isLoggedIn)
            navController!!.navigate(R.id.action_splashFragment_to_homeFragment)

        val timer = object: CountDownTimer(2000, 100) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                TransitionManager.beginDelayedTransition(splash_screen_layout)
                loading_progress_bar.visibility= View.GONE;
                enter_app_btn.visibility = View.VISIBLE;
                enter_login_textview.visibility = View.VISIBLE;
            }
        }
        timer.start()
    }

     fun onClick(v: View?) {
         Log.i("Clicked", "item clicked"+ v.toString())
        when(v!!.id){
            R.id.enter_app_btn -> navController!!.navigate(R.id.action_splashFragment_to_homeFragment)
            R.id.enter_login_textview -> navController!!.navigate(R.id.action_splashFragment_to_loginFragment)
        }
     }
}