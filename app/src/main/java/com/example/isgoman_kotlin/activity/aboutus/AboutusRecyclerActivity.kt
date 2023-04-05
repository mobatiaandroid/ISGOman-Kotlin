package com.example.isgoman_kotlin.activity.aboutus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.aboutus.adapter.AboutusRecyclerviewAdapter
import com.example.isgoman_kotlin.activity.aboutus.model.AboutResponseModel
import com.example.isgoman_kotlin.activity.aboutus.model.AboutUsModel
import com.example.isgoman_kotlin.activity.login.HomeActivity
import com.example.isgoman_kotlin.activity.login.LoginActivity
import com.example.isgoman_kotlin.constants.JsonTagConstants
import com.example.isgoman_kotlin.constants.StausCodes
import com.example.isgoman_kotlin.constants.URLConstants
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.example.isgoman_kotlin.manager.HeaderManager
import com.example.isgoman_kotlin.recyclerviewmanager.ItemOffsetDecoration
import com.example.isgoman_kotlin.recyclerviewmanager.RecyclerItemListener
import com.mobatia.nasmanila.api.ApiClient
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class AboutusRecyclerActivity : Activity(), URLConstants, JsonTagConstants,
    StausCodes {
   lateinit var recyclerview_about_us_list: RecyclerView
   lateinit var headermanager: HeaderManager
   lateinit var relativeHeader: RelativeLayout
    lateinit var backImgView: ImageView
    lateinit var banner_img: ImageView
    lateinit var mContext: Context
    var dataArrayStrings: ArrayList<AboutUsModel> = ArrayList<AboutUsModel>()
   // lateinit var pDialog: CustomProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        initUI()
        resetDisconnectTimer()
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
           /* pDialog = CustomProgressBar(
                mContext,
                R.drawable.spinner
            )
            pDialog.show()*/
            getAboutUsList()
        } else {
            AppUtilityMethods.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }

    private fun initUI() {
        mContext = this
        recyclerview_about_us_list =
            findViewById<View>(R.id.recyclerview_about_us_list) as RecyclerView
        banner_img = findViewById<View>(R.id.about_us_bannerImgView) as ImageView
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        recyclerview_about_us_list!!.setHasFixedSize(true)
        headermanager = HeaderManager(this@AboutusRecyclerActivity, "About Us")
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1)
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
            headermanager.getHeader(relativeHeader, 3)
        }
        //headermanager.getHeader(relativeHeader, 0);
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
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        //        recyclerview_about_us_list.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        val spacing = 10 // 50px
        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        recyclerview_about_us_list!!.addItemDecoration(itemDecoration)
        recyclerview_about_us_list!!.layoutManager = llm
        recyclerview_about_us_list!!.addOnItemTouchListener(
            RecyclerItemListener(
                applicationContext, recyclerview_about_us_list,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        stopDisconnectTimer()
                        val intent =
                            Intent(this@AboutusRecyclerActivity, AboutUsDetailActivity::class.java)
                        intent.putExtra("content", dataArrayStrings[position].content)
                        intent.putExtra("title", dataArrayStrings[position].title)
                        startActivity(intent)
                    }

                    override fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )
    }

    private fun getAboutUsList() {

        var boardId = ""
        boardId = if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            "1"
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
            "2"
        } else {
            "1"
        }
        val call: Call<AboutResponseModel> = ApiClient.getApiService().aboutus(
            AppPreferenceManager().getAccessToken(mContext).toString(),
            boardId)
        call.enqueue(object : retrofit2.Callback<AboutResponseModel> {
            override fun onResponse(
                call: Call<AboutResponseModel>,
                response: Response<AboutResponseModel>
            ) {
                Log.e("response", response.body()!!.toString())
                Log.e("status", response.body()!!.responsecode.toString())
                val status_code=response.body()!!.response.statuscode
                val response_code= response.body()!!.responsecode
                if (response_code.equals("200")) {
                    if (status_code.equals("201")) {
                        dataArrayStrings.addAll(response.body()!!.response.about)
                        if(response.body()!!.response.banner_img.equals(""))
                        {
                            Picasso.with(mContext).load(
                                response.body()!!.response.banner_img.replace(" ".toRegex(), "%20")
                            ).fit()
                                .into(banner_img, object : Callback {
                                    override fun onSuccess() {
                                       // pDialog.dismiss()
                                    }

                                    override fun onError() {
                                       // pDialog.dismiss()
                                    }
                                })
                        }
                        else
                        {
                           // pDialog.dismiss()
                        }
                        AppPreferenceManager().setStudentsResponseFromLoginAPI(
                            mContext,
                            dataArrayStrings.toString()
                        )


                        if (dataArrayStrings.size >0)
                        {

                            val mAboutusRecyclerviewAdapter =
                                AboutusRecyclerviewAdapter(mContext, dataArrayStrings)
                            recyclerview_about_us_list.adapter = mAboutusRecyclerviewAdapter
                        } else {
                            AppUtilityMethods.showDialogAlertDismiss(
                                mContext as Activity,
                                getString(R.string.alert_heading),
                                getString(R.string.no_datafound),
                                R.drawable.infoicon,
                                R.drawable.roundblue
                            )
                        }

                    } else if (status_code.equals(
                            StausCodes.STATUSCODE_INVALIDUSERNAMEORPASWD,
                            ignoreCase = true
                        )
                    ) {
                        AppPreferenceManager().setIsUserAlreadyLoggedIn(mContext, false)
                        AppUtilityMethods.showDialogAlertDismiss(
                            mContext as Activity,
                            getString(R.string.error_heading),
                            getString(R.string.invalid_usr_pswd),
                            R.drawable.infoicon,
                            R.drawable.roundblue
                        )
                    } else if (status_code.equals(StausCodes.STATUSCODE_MISSING_PARAMETER, ignoreCase = true)) {
                        AppPreferenceManager().setIsUserAlreadyLoggedIn(mContext, false)
                        AppUtilityMethods.showDialogAlertDismiss(
                            mContext as Activity,
                            getString(R.string.error_heading),
                            getString(R.string.missing_parameter),
                            R.drawable.infoicon,
                            R.drawable.roundblue
                        )
                    } else {
                        AppPreferenceManager().setIsUserAlreadyLoggedIn(mContext, false)
                        AppUtilityMethods.showDialogAlertDismiss(
                            mContext as Activity,
                            getString(R.string.common_error),
                            getString(R.string.invalid_usr_pswd),
                            R.drawable.infoicon,
                            R.drawable.roundblue
                        )
                    }
                }
                else if (response_code.equals(StausCodes.RESPONSE_INTERNALSERVER_ERROR, ignoreCase = true))
                {
                    AppUtilityMethods.showDialogAlertDismiss(
                        mContext as Activity,
                        getString(R.string.error_heading),
                        getString(R.string.internal_server_error),
                        R.drawable.infoicon,
                        R.drawable.roundblue
                    )
                } else if (response_code.equals(StausCodes.RESPONSE_INVALID_ACCESSTOKEN, ignoreCase = true) || response_code.equals(
                        StausCodes.RESPONSE_ACCESSTOKEN_MISSING, ignoreCase = true) || response_code.equals(
                        StausCodes.RESPONSE_ACCESSTOKEN_EXPIRED, ignoreCase = true))
                {
                    /*SplashActivity().to
                    sendLoginToServer(URL_PARENT_LOGIN)*/
                } else
                {
                    AppUtilityMethods.showDialogAlertDismiss(
                        mContext as Activity,
                        "Alert",
                        mContext.getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.roundblue
                    )
                }
            }

            override fun onFailure(call: Call<AboutResponseModel?>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Fail to get the data..",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.e("succ", t.message.toString())
            }
        })
            }




    /*    public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(AboutusRecyclerActivity.this, false);
            AppPreferenceManager.setUserId(AboutusRecyclerActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(AboutusRecyclerActivity.this, false);
            AppPreferenceManager.setSchoolSelection(AboutusRecyclerActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {
                Intent mIntent = new Intent(AboutusRecyclerActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
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
            val mIntent = Intent(this@AboutusRecyclerActivity, LoginActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mIntent)
            finish()
        }
    }
}