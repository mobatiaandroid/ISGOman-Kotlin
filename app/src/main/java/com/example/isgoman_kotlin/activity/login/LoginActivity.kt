package com.example.isgoman_kotlin.activity.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.isgoman_kotlin.recyclerviewmanager.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isg_oman_kotlin.activity.splash.SplashActivity
import com.example.isgoman_kotlin.BuildConfig
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.login.adapter.StudentAdapter
import com.example.isgoman_kotlin.activity.login.model.Login_model
import com.example.isgoman_kotlin.activity.login.model.StudentModels
import com.example.isgoman_kotlin.activity.registeration.RegistrationActivity
import com.example.isgoman_kotlin.constants.JsonTagConstants
import com.example.isgoman_kotlin.constants.StausCodes
import com.example.isgoman_kotlin.constants.StausCodes.*
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.example.isgoman_kotlin.recyclerviewmanager.RecyclerItemListener
import com.mobatia.nasmanila.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity :AppCompatActivity(),  JsonTagConstants, StausCodes {
    lateinit var mContext: Context
    lateinit var forGotpasswordText:TextView
    lateinit var app_version:TextView
    lateinit var edtUserName:EditText
    lateinit var edtPassword:EditText
    lateinit var forgotEmail:EditText
    lateinit var forgotPhone:EditText
    lateinit var btnLogn:Button
    lateinit var btnSignUp:Button
    lateinit var btnGuestUser:Button
    var dialog: Dialog? = null
    var versionName: String? = null
    var studentModelsArrayList: ArrayList<StudentModels> = ArrayList<StudentModels>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext=this
        initFn()
    }

    private fun initFn() {
        edtUserName = findViewById<View>(R.id.edtUserName) as EditText
        edtPassword = findViewById<View>(R.id.edtPassword) as EditText
        //relButtonsHolder= (LinearLayout) findViewById(R.id.relButtonsHolder);
        //relButtonsHolder= (LinearLayout) findViewById(R.id.relButtonsHolder);
        btnLogn = findViewById<View>(R.id.btnLogin) as Button
        btnGuestUser = findViewById<View>(R.id.btnGuestUser) as Button
        btnSignUp = findViewById<View>(R.id.btnSignUp) as Button
        app_version = findViewById(R.id.app_version)
        /// relSignUp= (LinearLayout) findViewById(R.id.relSignUp);
        // relGuestUser= (LinearLayout) findViewById(R.id.relGuestUser);
        /// relSignUp= (LinearLayout) findViewById(R.id.relSignUp);
        // relGuestUser= (LinearLayout) findViewById(R.id.relGuestUser);
        forGotpasswordText = findViewById<View>(R.id.txtPassword) as TextView
        // edtUserName.setText("9946063677");
        /*set underline for forgot password text*/
        // edtUserName.setText("9946063677");
        /*set underline for forgot password text*/forGotpasswordText.paintFlags =
            forGotpasswordText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        forGotpasswordText.text = getString(R.string.forgot_password)
        dialog = Dialog(mContext, R.style.NewDialog)

        btnLogn.setOnClickListener {
            if (edtUserName.text.isBlank()) {
                showDialogAlertDismiss(
                    mContext,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_phoneno),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                );
            } else if (edtPassword.text.isBlank()) {
                showDialogAlertDismiss(
                    mContext,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_password),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                );
            }
            else
            {

                loginApiCall()
            }

        }
        btnSignUp.setOnClickListener {
            showDialogSignUpAlert()
        }

        forGotpasswordText.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun loginApiCall() {
        val deviceBrand = Build.MANUFACTURER
        val deviceModel = Build.MODEL
        val osVersion = Build.VERSION.RELEASE
        val devicename = "$deviceBrand $deviceModel $osVersion"
        val version: String = BuildConfig.VERSION_NAME
        val androidId = Settings.Secure.getString(
            mContext.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )
        Log.e("acess_check",AppPreferenceManager().getAccessToken(mContext).toString())

        //val call: Call<Login_model> = ApiClient.getApiService().login(model)
        val call: Call<Login_model> = ApiClient.getApiService().login_2(
            11,
            AppPreferenceManager().getAccessToken(mContext).toString(),
            edtUserName.text.toString(),
            edtPassword.text.toString(),
            "2",
            "cYx8HgByjTc:APA91bGIFxFdiZ35PnVPIa-inFlB9sxXuIBI3J62Gfk_-no8QxM2fIYHWw4sDb7gmd2OivzGmAuxzTGFpHr7SZs9fPZETJ0f50TcPz5zZAwy8r4ENC8YQuCiGKZk4nqChg3AjyBT_s95",
        )
        call.enqueue(object : Callback<Login_model> {
            override fun onResponse(
                call: Call<Login_model>,
                response: Response<Login_model>
            ) {
                Log.e("response", response.body()!!.toString())
                Log.e("status", response.body()!!.responsecode.toString())
                val status_code=response.body()!!.response.statuscode
                val response_code= response.body()!!.responsecode
                if (response_code.equals("200")) {
                if (status_code.equals("201")) {
                    val loginmodel: Login_model? = response.body()
                    AppPreferenceManager().setUserId(mContext, loginmodel!!.response.parentId)
                    studentModelsArrayList.addAll(loginmodel!!.response.data)
                    Log.e("studentArray", studentModelsArrayList.toString())
                    AppPreferenceManager().setStudentsResponseFromLoginAPI(mContext, studentModelsArrayList.toString())
                    if (studentModelsArrayList.size >0)
                    {
                    if (studentModelsArrayList.size == 1) {
                        if (studentModelsArrayList[0].boardid.equals("1")) {
                            val selectedFromList = "ISG"
                            AppPreferenceManager().setSchoolSelection(mContext, selectedFromList)
                        } else {
                            val selectedFromList = "ISG-INT"
                            AppPreferenceManager().setSchoolSelection(mContext, selectedFromList)
                        }
                        val intent = Intent(mContext, HomeActivity::class.java)
                        AppPreferenceManager().setIsGuest(mContext, false)
                        AppPreferenceManager().setIsUserAlreadyLoggedIn(mContext, true)
                        startActivity(intent)
                        finish()
                        AppPreferenceManager().setStudentId(
                            mContext,
                            studentModelsArrayList[0].id
                        )
                        AppPreferenceManager().setStudentName(
                            mContext,
                            studentModelsArrayList[0].name
                        )
                        AppPreferenceManager().setStudentClassName(
                            mContext,
                            studentModelsArrayList[0].class_name
                        )
                        AppPreferenceManager().setStudentSectionName(
                            mContext,
                            studentModelsArrayList[0].student_division_name
                        )
                    } else {
                        showStudentList(studentModelsArrayList)
                    }
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
                        STATUSCODE_INVALIDUSERNAMEORPASWD,
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
                } else if (status_code.equals(STATUSCODE_MISSING_PARAMETER, ignoreCase = true)) {
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
             else if (response_code.equals(RESPONSE_INTERNALSERVER_ERROR, ignoreCase = true))
            {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.error_heading),
                    getString(R.string.internal_server_error),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else if (response_code.equals(RESPONSE_INVALID_ACCESSTOKEN, ignoreCase = true) || response_code.equals(RESPONSE_ACCESSTOKEN_MISSING, ignoreCase = true) || response_code.equals(RESPONSE_ACCESSTOKEN_EXPIRED, ignoreCase = true))
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

            override fun onFailure(call: Call<Login_model?>, t: Throwable) {
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

    fun showDialogSignUpAlert() {
        val dialog = Dialog(mContext, R.style.NewDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_signup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        // set the custom dialog components - edit text, button
        val sdk = Build.VERSION.SDK_INT
        //mMailEdtText.setOnTouchListener(this);
        val alertHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        //
        val dialogSubmitButton = dialog
            .findViewById<View>(R.id.btn_signup) as Button

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(mContext, RegistrationActivity::class.java)
            startActivity(intent)
        }
        val dialogMayBelaterutton = dialog.findViewById<View>(R.id.btn_maybelater) as Button
        dialogMayBelaterutton.setOnClickListener {
            //AppUtilityMethods.hideKeyBoard(mContext)
            dialog.dismiss()
        }
        dialog.show()


    }

    fun showDialogAlertDismiss(
        activity: Context?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialog_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
    private fun showForgotPasswordDialog() {
        dialog = Dialog(mContext, R.style.NewDialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_forgot_password)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(true)
        // set the custom dialog components - edit text, button
        val sdk = Build.VERSION.SDK_INT
        forgotEmail = dialog!!.findViewById<View>(R.id.text_email) as EditText
        forgotPhone = dialog!!.findViewById<View>(R.id.text_phone) as EditText
        //mMailEdtText.setOnTouchListener(this);
        val alertHead = dialog!!.findViewById<View>(R.id.alertHead) as TextView
        //
        val dialogSubmitButton = dialog!!
            .findViewById<View>(R.id.btn_signup) as Button

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener {
           // AppUtilityMethods.hideKeyBoard(mContext)
            if (forgotPhone.text.toString() == "") {
                showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_phoneno),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else if (forgotPhone.text.toString().trim { it <= ' ' }.length < 8) {
                showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_phone_eight_digitno),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else {
                //AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick,  R.drawable.roundblue);

            }
        }
        val dialogMayBelaterutton = dialog!!.findViewById<View>(R.id.btn_maybelater) as Button
        dialogMayBelaterutton.setOnClickListener {
            //AppUtilityMethods.hideKeyBoard(mContext)
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    @SuppressLint("WrongConstant")
    fun showStudentList(mStudentArray: java.util.ArrayList<StudentModels>) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_student_list)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        iconImageView.setImageResource(R.drawable.boy)
        val studentList = dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        /* if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.l));

        } else {
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button));

        }*/studentList.addItemDecoration(DividerItemDecoration(mContext.resources.getDrawable(R.drawable.line)))
        studentList.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentList.layoutManager = llm
        val studentAdapter = StudentAdapter(mContext, mStudentArray)
        studentList.adapter = studentAdapter
        dialogDismiss.setOnClickListener { dialog.dismiss() }
        studentList.addOnItemTouchListener(
            RecyclerItemListener(mContext, studentList,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        dialog.dismiss()
                        if (mStudentArray[position].boardid.equals("1")) {
                            val selectedFromList = "ISG"
                            AppPreferenceManager().setSchoolSelection(mContext, selectedFromList)
                        } else {
                            val selectedFromList = "ISG-INT"
                            AppPreferenceManager().setSchoolSelection(mContext, selectedFromList)
                        }
                        val intent = Intent(mContext, HomeActivity::class.java)
                        AppPreferenceManager().setIsGuest(mContext, false)
                        AppPreferenceManager().setIsUserAlreadyLoggedIn(mContext, true)
                        AppPreferenceManager().setStudentId(
                            mContext,
                            studentModelsArrayList[position].id
                        )
                        AppPreferenceManager().setStudentName(
                            mContext,
                            studentModelsArrayList[position].name
                        )
                        AppPreferenceManager().setStudentClassName(
                            mContext,
                            studentModelsArrayList[position].class_name
                        )
                        AppPreferenceManager().setStudentSectionName(
                            mContext,
                            studentModelsArrayList[position].student_division_name
                        )
                        startActivity(intent)
                        finish()
                    }

                    override fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )
        dialog.show()
    }
}