package com.example.isgoman_kotlin.activity.registeration

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.manager.HeaderManager
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.mobatia.nasmanila.api.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity:AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var editTextName: EditText
    lateinit var editPhoneNo: EditText
    lateinit var editTextStudentID: EditText
    lateinit var editTextEmailID: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextConfirmPassword: EditText
    lateinit var relativeHeader: RelativeLayout
    lateinit var headermanager: HeaderManager
    lateinit var back: ImageView
    lateinit var btnContinue: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mContext=this
        initFn()
    }

    private fun initFn() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        editTextName = findViewById<View>(R.id.editTextName) as EditText
        editPhoneNo = findViewById<View>(R.id.editPhoneNo) as EditText
        editTextStudentID = findViewById<View>(R.id.editTextStudentID) as EditText
        editTextEmailID = findViewById<View>(R.id.editTextEmailID) as EditText
        editTextPassword = findViewById<View>(R.id.editTextPassword) as EditText
        editTextConfirmPassword = findViewById<View>(R.id.editTextConfirmPassword) as EditText
        btnContinue = findViewById<View>(R.id.btnContinue) as Button
        headermanager = HeaderManager(this@RegistrationActivity, getString(R.string.registration))
        headermanager.getHeader(relativeHeader, 2)
        back = headermanager.leftButton
//        headermanager.setButtonLeftSelector(R.drawable.homeicon,
//                R.drawable.homeicon);
        //        headermanager.setButtonLeftSelector(R.drawable.homeicon,
//                R.drawable.homeicon);
        headermanager.setButtonLeftSelector(
            R.drawable.arrowblack,
            R.drawable.arrowblack
        )
        back.setOnClickListener { finish() }


        btnContinue.setOnClickListener {
            if (editTextName.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
                showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_name),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else if (editPhoneNo.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true)) {
              showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_phoneno),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else if (editPhoneNo.text.toString().trim { it <= ' ' }.length < 8) {
               showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_phone_eight_digitno),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else if (editTextEmailID.text.toString().trim { it <= ' ' }
                    .equals("", ignoreCase = true)) {
                showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_email),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else if (!isValidEmail(editTextEmailID.text.toString())) {
                showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.invalid_email),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else if (editTextStudentID.text.toString().trim { it <= ' ' } == "") {
                showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_studentid),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            } else {
                createProfileApi()
                //showDialogRegSuccessAlert((Activity) mContext, getString(R.string.alert_heading), getString(R.string.req_success), R.drawable.tick,  R.drawable.roundblue);
            }
            /* Intent intent=new Intent(mContext, ConfirmLogin.class);
                    startActivity(intent);
                    finish();*/
        }
    }

    private fun createProfileApi() {
        val call: Call<ResponseBody> = ApiClient.getApiService().signup(11, AppPreferenceManager().getAccessToken(mContext).toString(),editTextName.getText().toString(),editTextEmailID.getText().toString(),editPhoneNo.getText().toString(),editTextStudentID.getText().toString(),
            "2","eT_PPpbiTqORcd2dmZ9gG7:APA91bGD2YnUGpQMfVskKXjnQAvAR5I20g7ZY-tN845zmp75FzEs1yfLL42bD9YL7IZKOfCVTbAiqSANhUYrqqcsktaRaQtnvvQ3j6mE_Q7aFBu5wUVEaDR0cu5HHQM_VPE9FxGzaSor",
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.e("response", response.body()!!.toString())

            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
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
        //		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show()
    }
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}