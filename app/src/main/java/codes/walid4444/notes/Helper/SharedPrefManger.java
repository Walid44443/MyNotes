package codes.walid4444.notes.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefManger {

    private static SharedPreferences mSharedPref;


    public SharedPrefManger(Context mContext) {
        if (mSharedPref == null)
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void Login(String email,String password,String mToken){
        setEmail(email);
        setPassword(password);
        setmToken(mToken);
    }
    public void Logout(){
        setEmail("");
        setPassword("");
        setmToken("");
    }


    public boolean isLoggedIn(){
        return !getmToken().isEmpty();
    }

    public String getEmail() {
        return mSharedPref.getString("email", "");
    }

    public void setEmail(String email) {
        mSharedPref.edit().putString("email", email).apply();
    }

    public String getPassword() {
        return mSharedPref.getString("password", "");
    }

    public void setPassword(String password) {
        mSharedPref.edit().putString("password", password).apply();
    }

    public String getmToken() {
        return mSharedPref.getString("mToken", "");

    }

    public void setmToken(String mToken) {
        mSharedPref.edit().putString("mToken", mToken).apply();
    }
}
