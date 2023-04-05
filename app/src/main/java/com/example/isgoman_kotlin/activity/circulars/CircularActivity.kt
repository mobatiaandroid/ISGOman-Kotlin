package com.example.isgoman_kotlin.activity.circulars

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.circulars.adapter.CircularAdapter
import com.example.isgoman_kotlin.activity.circulars.model.CircularModel
import com.example.isgoman_kotlin.activity.circulars.model.CiricularResponseModel
import com.example.isgoman_kotlin.activity.login.HomeActivity
import com.example.isgoman_kotlin.activity.login.LoginActivity
import com.example.isgoman_kotlin.activity.pdf.PDFViewActivity
import com.example.isgoman_kotlin.constants.StausCodes
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.example.isgoman_kotlin.manager.HeaderManager
import com.example.isgoman_kotlin.recyclerviewmanager.ItemOffsetDecoration
import com.example.isgoman_kotlin.recyclerviewmanager.RecyclerItemListener
import com.mobatia.nasmanila.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CircularActivity:AppCompatActivity() {
    var messageList: RecyclerView? = null
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    lateinit var back: ImageView
    var mContext: Context = this
    var listTimetable: ArrayList<CircularModel> = ArrayList<CircularModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initUI()
        resetDisconnectTimer()
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            getList()
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

    private fun getList() {
        var boardId = ""
        boardId = if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            "1"
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
            "2"
        } else {
            "1"
        }
        val call: Call<CiricularResponseModel> = ApiClient.getApiService().ciricular(
            AppPreferenceManager().getAccessToken(mContext).toString(),
            boardId,
            AppPreferenceManager().getStudentId(mContext).toString(),
            AppPreferenceManager().getUserId(mContext).toString()
        )
        call.enqueue(object : Callback<CiricularResponseModel> {
            override fun onResponse(
                call: Call<CiricularResponseModel>,
                response: Response<CiricularResponseModel>
            ) {
                Log.e("response", response.body()!!.toString())
                Log.e("status", response.body()!!.responsecode.toString())
                val status_code=response.body()!!.response.statuscode
                val response_code= response.body()!!.responsecode
                if (response_code.equals("200")) {
                    if (status_code.equals("201")) {

                       // AppPreferenceManager().setUserId(mContext, loginmodel!!.response.parentId)
                        listTimetable.addAll(response.body()!!.response.specialExams)
                        Log.e("studentArray", listTimetable.get(0).id.toString())
                       // AppPreferenceManager().setStudentsResponseFromLoginAPI(mContext, studentModelsArrayList.toString())
                        if (listTimetable.size >0)
                        {
                            /*CoursesAdapter mAboutusRecyclerviewAdapter=new CoursesAdapter(mContext,coursesModelArrayList);
                                messageList.setAdapter(mAboutusRecyclerviewAdapter);*/
                            val timeTableAdapter = CircularAdapter(mContext, listTimetable)
                            messageList!!.adapter = timeTableAdapter
                        } else {
                            AppUtilityMethods.showDialogAlertDismiss(
                                mContext as Activity,
                                getString(R.string.error_heading),
                                getString(R.string.no_student_available),
                                R.drawable.infoicon,
                                R.drawable.roundred
                            )
                        }
                        /*val intent = Intent(mContext, HomeActivity::class.java)
                            startActivity(intent)*/
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

            override fun onFailure(call: Call<CiricularResponseModel?>, t: Throwable) {
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

    private fun initUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout

        messageList = findViewById<View>(R.id.settingItemList) as RecyclerView

        messageList!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        messageList!!.layoutManager = llm

        val spacing = 10 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        messageList!!.addItemDecoration(itemDecoration)
        headermanager = HeaderManager(this@CircularActivity, "Circulars")
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            headermanager!!.getHeader(relativeHeader, 1)
        } else {
            headermanager!!.getHeader(relativeHeader, 3)
        }
        back = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.backbtn,
            R.drawable.backbtn
        )

        back.setOnClickListener(View.OnClickListener {
            stopDisconnectTimer()
            finish()
        })


        messageList!!.addOnItemTouchListener(
            RecyclerItemListener(
                applicationContext, messageList!!,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        if (!listTimetable[position].pdf_link.equals("")) {
                            /* Intent intent = new Intent(CircularActivity.this, PdfReader.class);
                            intent.putExtra("pdf_url", listTimetable.get(position).getPdf_link());
                            startActivity(intent);*/
                            /*      String format = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s";
                            String fullPath = String.format(Locale.ENGLISH, format, listTimetable.get(position).getPdf_link());
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fullPath));
                            startActivity(browserIntent);


*/
                            System.out.println("PDF_URL: " + listTimetable[position].pdf_link)
                            val browserIntent =
                                Intent(this@CircularActivity, PDFViewActivity::class.java)
                            browserIntent.putExtra("pdf_url", listTimetable[position].pdf_link)
                            browserIntent.putExtra("title", "Circular")
                            browserIntent.putExtra("filename", listTimetable[position].name)
                            startActivity(browserIntent)
                        } else {
                            AppUtilityMethods.showDialogAlertDismiss(
                                mContext as Activity,
                                "Alert",
                                mContext.getString(R.string.nomoreinfo),
                                R.drawable.exclamationicon,
                                R.drawable.roundblue
                            )
                        }
                    }

                    override fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )
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
            AppPreferenceManager.setIsGuest(CircularActivity.this, false);
            AppPreferenceManager.setUserId(CircularActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(CircularActivity.this, false);
            AppPreferenceManager.setSchoolSelection(CircularActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {
                Intent mIntent = new Intent(CircularActivity.this, LoginActivity.class);
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

    override fun onResume() {
        super.onResume()
        resetDisconnectTimer()
    }

    override fun onStop() {
        super.onStop()
//        stopDisconnectTimer();
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopDisconnectTimer()
    }

    override fun onRestart() {
        super.onRestart()
        println("TestRestart:hshhs")
        if (AppPreferenceManager().getUserId(mContext).equals("")) {
            val mIntent = Intent(this@CircularActivity, LoginActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mIntent)
            finish()
        }
    }
}