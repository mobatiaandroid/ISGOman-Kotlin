package com.example.isgoman_kotlin.activity.aboutus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.login.HomeActivity
import com.example.isgoman_kotlin.activity.login.LoginActivity
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.example.isgoman_kotlin.manager.HeaderManager

class AboutUsDetailActivity : Activity() {
    lateinit var headermanager: HeaderManager
    lateinit var relativeHeader: RelativeLayout
    lateinit var mProgressRelLayout: RelativeLayout
    lateinit var backImgView: ImageView
    lateinit var mContext: Context
    lateinit var extras: Bundle
    var title: String? = null
    var content: String? = null
    lateinit var web: WebView
    private lateinit var mwebSettings: WebSettings
    private val loadingFlag = true
    var mLoadUrl: String? = null
    private val mErrorFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus_detail)
        mContext=this
        resetDisconnectTimer()
        initUI()
        webViewSettings
    }

    private fun initUI() {
        extras = intent.extras!!
        if (extras != null) {
            title = extras!!.getString("title")
            content = extras!!.getString("content")
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        headermanager = HeaderManager(this@AboutUsDetailActivity, "About Us")
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1)
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
            headermanager.getHeader(relativeHeader, 3)
        }
        mProgressRelLayout = findViewById<View>(R.id.progressDialog) as RelativeLayout
        mProgressRelLayout!!.visibility = View.GONE
        //web.setWebViewClient(new myWebClient());
        web = findViewById<View>(R.id.webView) as WebView
        backImgView = headermanager.getLeftButton()
        headermanager.setButtonLeftSelector(
            R.drawable.backbtn,
            R.drawable.backbtn
        )
        backImgView!!.setOnClickListener {
            stopDisconnectTimer()
            AppUtilityMethods.hideKeyBoard(mContext)
            finish()
        }
    }

    // web.setBackgroundColor(Color.WHITE);
    private val webViewSettings: Unit
        private get() {
            web!!.settings.javaScriptEnabled = true
            web!!.setPadding(0, 0, 0, 0)
            web!!.setBackgroundColor(Color.TRANSPARENT)
            web!!.settings.javaScriptEnabled = true
            web!!.settings.loadWithOverviewMode = true
            web!!.settings.useWideViewPort = false
            web!!.settings.setSupportZoom(true)
            web!!.settings.builtInZoomControls = true
            web!!.settings.pluginState = WebSettings.PluginState.ON
            val sdk = Build.VERSION.SDK_INT
            if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                // web.setBackgroundColor(Color.WHITE);
                web!!.loadUrl("about:blank")
            }
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                web!!.clearView()
            }
            if (content != null) {
                try {
                    val head =
                        "<head><style>@font-face {font-family: 'Roboto', sans-serif;src: url(\"https://fonts.googleapis.com/css?family=Roboto\");}body {font-family: 'verdana';}</style>$title<br><br>$content</head>"
                    web!!.loadData(head, "text/html; charset=utf-8", "utf-8")
                    web!!.setBackgroundColor(Color.TRANSPARENT)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    /* public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

   private Handler disconnectHandler = new Handler(){
       public void handleMessage(Message msg) {
       }
   };

   private Runnable disconnectCallback = new Runnable() {
       @Override
       public void run() {
           // Perform any required operation on disconnect
           AppPreferenceManager.setIsGuest(AboutUsDetailActivity.this, false);
           AppPreferenceManager.setUserId(AboutUsDetailActivity.this, "");
           AppPreferenceManager.setIsUserAlreadyLoggedIn(AboutUsDetailActivity.this, false);
           AppPreferenceManager.setSchoolSelection(AboutUsDetailActivity.this,"ISG");
           if (AppUtilityMethods.isAppInForeground(mContext))
           {
            Intent mIntent = new Intent(AboutUsDetailActivity.this, LoginActivity.class);
           mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(mIntent);
           finish();
           }
//            Intent mIntent = new Intent(AboutUsDetailActivity.this, LoginActivity.class);
//            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(mIntent);
//            finish();
       }
   };*/
    fun resetDisconnectTimer() {
        HomeActivity().disconnectHandler.removeCallbacks(HomeActivity().disconnectCallback)
        HomeActivity().disconnectHandler.postDelayed(
            HomeActivity().disconnectCallback,
            HomeActivity().DISCONNECT_TIMEOUT
        )
    }

    fun stopDisconnectTimer() {
        HomeActivity().disconnectHandler.removeCallbacks(HomeActivity().disconnectCallback)
    }

    override fun onUserInteraction() {
        resetDisconnectTimer()
    }

    public override fun onResume() {
        super.onResume()
        resetDisconnectTimer()
    }

    public override fun onStop() {
        super.onStop()
        //        stopDisconnectTimer();
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopDisconnectTimer()
    }

    override fun onRestart() {
        super.onRestart()
        if (AppPreferenceManager().getUserId(mContext).equals("")) {
            val mIntent = Intent(this@AboutUsDetailActivity, LoginActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mIntent)
            finish()
        }
    }
}
