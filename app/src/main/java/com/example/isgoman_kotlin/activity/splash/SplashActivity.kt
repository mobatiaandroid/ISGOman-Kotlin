package com.example.isg_oman_kotlin.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.login.LoginActivity
import com.example.isgoman_kotlin.activity.splash.Token_model
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.mobatia.nasmanila.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity:AppCompatActivity() {
    lateinit var mContext:Context
    private val SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)// will hide title
        supportActionBar?.hide() // hide the title bar

        setContentView(R.layout.activity_splash)
        mContext = this
        token()
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun token() {
        val call: Call<Token_model> = ApiClient.getApiService().token(11,
            "password","team_mobatia","eH3mGE9F0h","guestuser@mobatia.com",
            "guest@123"
        )
        call.enqueue(object : Callback<Token_model> {
            override fun onResponse(
                call: Call<Token_model>,
                response: Response<Token_model>
            ) {
                var access_token= response.body()!!.access_token
                var token_type= response.body()!!.token_type
                Log.e("accesstoken",access_token)
                AppPreferenceManager().setAccessToken(mContext,access_token)
            }
            override fun onFailure(call: Call<Token_model>, t: Throwable) {
                Log.e("success", t.message.toString())
            }
        })
    }
}