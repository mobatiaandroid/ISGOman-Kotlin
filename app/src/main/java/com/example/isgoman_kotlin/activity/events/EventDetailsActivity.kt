package com.example.isgoman_kotlin.activity.events

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.events.adapter.EventListAdapter
import com.example.isgoman_kotlin.activity.events.model.EventModels
import com.example.isgoman_kotlin.activity.events.model.EventResponseModel
import com.example.isgoman_kotlin.activity.login.HomeActivity
import com.example.isgoman_kotlin.activity.login.LoginActivity
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.example.isgoman_kotlin.manager.HeaderManager
import com.mobatia.nasmanila.api.ApiClient
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat

class EventDetailsActivity:AppCompatActivity() {
    lateinit var mContext: Context

    var mEventModels: ArrayList<EventModels>? = null
    var extras: Bundle? = null
    var relativeHeader: RelativeLayout? = null
    var registrationRelative: RelativeLayout? = null
    var parentConsernRelative: RelativeLayout? = null
    var headermanager: HeaderManager? = null
    lateinit var back: ImageView
    var bannerImage:android.widget.ImageView? = null
    var pdfDownloadImgView:android.widget.ImageView? = null
    var parentConsernpdfDownloadImgView:android.widget.ImageView? = null
    var addToCalendar:android.widget.ImageView? = null
    var position = 0
    var eventTitle: TextView? = null
    var eventAttendStatus:TextView? = null
    var start_date:TextView? = null
    var start_time:TextView? = null
    var end_date:TextView? = null
    var end_time:TextView? = null
    var eventDesc:TextView? = null
    var submitAttend: Button? = null
    var parentConsernForm: Button? = null
    var registrationForm:android.widget.Button? = null
    var check_register = false
    var dialog: Dialog? = null
    //var pDialog: CustomProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        mContext = this
        extras = intent.extras
        if (extras != null) {
            mEventModels = extras!!.getSerializable("array") as ArrayList<EventModels>?
            position = extras!!.getInt("position")
        }

        initUI()
        resetDisconnectTimer()

        setValues()
    }

    private fun initUI() {
       /* pDialog = CustomProgressBar(
            mContext,
            R.drawable.spinner
        )
        pDialog.show()*/
        dialog = Dialog(mContext)
        headermanager = HeaderManager(this@EventDetailsActivity, getString(R.string.events))
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        registrationRelative = findViewById<View>(R.id.registrationRelative) as RelativeLayout
        parentConsernRelative = findViewById<View>(R.id.parentConsernRelative) as RelativeLayout
        start_date = findViewById<View>(R.id.start_date) as TextView
        start_time = findViewById<View>(R.id.start_time) as TextView
        addToCalendar = findViewById<View>(R.id.addToCalendar) as ImageView
        pdfDownloadImgView = findViewById<View>(R.id.pdfDownloadImgView) as ImageView
        parentConsernpdfDownloadImgView =
            findViewById<View>(R.id.parentConsernpdfDownloadImgView) as ImageView
        end_date = findViewById<View>(R.id.end_date) as TextView
        end_time = findViewById<View>(R.id.end_time) as TextView
        eventAttendStatus = findViewById<View>(R.id.eventAttendStatus) as TextView
        eventTitle = findViewById<View>(R.id.eventTitle) as TextView
        eventDesc = findViewById<View>(R.id.eventDesc) as TextView
        submitAttend = findViewById<View>(R.id.submitAttend) as Button
        registrationForm = findViewById<View>(R.id.registrationForm) as Button
        parentConsernForm = findViewById<View>(R.id.parentConsernForm) as Button
        bannerImage = findViewById<View>(R.id.bannerImage) as ImageView
        pdfDownloadImgView!!.setImageResource(R.drawable.pdficonwhite)
        parentConsernpdfDownloadImgView!!.setImageResource(R.drawable.pdficonwhite)
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            headermanager!!.getHeader(relativeHeader, 1)
            submitAttend!!.setBackgroundResource(R.drawable.event_attend_drwable_btn_green)
            registrationForm!!.setBackgroundResource(R.drawable.event_attend_drwable_btn_green)
            parentConsernForm!!.setBackgroundResource(R.drawable.event_attend_drwable_btn_green)
//        pdfDownloadImgView.setBackgroundResource(R.drawable.pdfgreen);
//        parentConsernpdfDownloadImgView.setBackgroundResource(R.drawable.pdfgreen);
        } else {
            headermanager!!.getHeader(relativeHeader, 3)
            submitAttend!!.setBackgroundResource(R.drawable.event_attend_drwable_btn_blue)
            registrationForm!!.setBackgroundResource(R.drawable.event_attend_drwable_btn_blue)
            parentConsernForm!!.setBackgroundResource(R.drawable.event_attend_drwable_btn_blue)

//        parentConsernpdfDownloadImgView.setBackgroundResource(R.drawable.pdfblue);
//        pdfDownloadImgView.setBackgroundResource(R.drawable.pdfblue);
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


        submitAttend!!.setOnClickListener {
            //check_register = true;
            if (mEventModels!![position].pay_type.equals("0")) {
                if (submitAttend!!.text.toString() == "ATTENDING") {
                    showDialogEventConfirmAlert(
                        mContext as Activity,
                        getString(R.string.alert_heading),
                        "Do you want to register for the " + mEventModels!![position].name
                            .toString() + "?",
                        R.drawable.question,
                        R.drawable.roundblue
                    )
                } else {
                    showDialogEventUnJoinConfirmAlert(
                        mContext as Activity,
                        getString(R.string.alert_heading),
                        "Do you want to unregister for " + mEventModels!![position].name
                            .toString() + "?",
                        R.drawable.question,
                        R.drawable.roundblue
                    )
                }
            } else if (mEventModels!![position].pay_type.equals("1")) {
                if (submitAttend!!.text.toString() == "ATTENDING") {
                    showDialogEventConfirmAlert(
                        mContext as Activity,
                        getString(R.string.alert_heading),
                        "Do you want to register for the " + mEventModels!![position].name
                            .toString() + "?",
                        R.drawable.question,
                        R.drawable.roundblue
                    )
                } else {
                    showDialogEventUnJoinConfirmAlert(
                        mContext as Activity,
                        getString(R.string.alert_heading),
                        "Do you want to unregister for " + mEventModels!![position].name
                            .toString() + "?",
                        R.drawable.question,
                        R.drawable.roundblue
                    )
                }
                //                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.alert_event_reg_paid), R.drawable.infoicon,  R.drawable.roundblue);
            }
        }

        pdfDownloadImgView!!.setOnClickListener {
            if (!mEventModels!![position].pdf_link.equals("")) {
                //                stopDisconnectTimer();
                /*   Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                    intent.putExtra("pdf_url", mEventModels.get(position).getPdf_link());
                    startActivity(intent);*/
              /*  val browserIntent = Intent(this@EventDetailsActivity, PDFViewActivity::class.java)
                browserIntent.putExtra("pdf_url", mEventModels!![position].pdf_link)
                browserIntent.putExtra("title", "Events")
                browserIntent.putExtra("filename", mEventModels!![position].name)
                startActivity(browserIntent)*/
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.reg_form_not_available),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            }
        }
        registrationForm!!.setOnClickListener {
            if (!mEventModels!![position].pdf_link.equals("")) {
                //                stopDisconnectTimer();
                /*   Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                    intent.putExtra("pdf_url", mEventModels.get(position).getPdf_link());
                    startActivity(intent);*/
                /*   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mEventModels.get(position).getPdf_link()));
                    startActivity(browserIntent);*/
               /* val browserIntent = Intent(this@EventDetailsActivity, PDFViewActivity::class.java)
                browserIntent.putExtra("pdf_url", mEventModels!![position].pdf_link)
                browserIntent.putExtra("title", "Events")
                browserIntent.putExtra("filename", mEventModels!![position].name)
                startActivity(browserIntent)*/
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.reg_form_not_available),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            }
        }
        parentConsernpdfDownloadImgView!!.setOnClickListener {
            if (!mEventModels!![position].parent_consent_pdf_link.equals("")) {
                //                stopDisconnectTimer();
                /*          Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                    intent.putExtra("pdf_url", mEventModels.get(position).getParent_consent_pdf_link());
                    startActivity(intent);*/
               /* val browserIntent = Intent(this@EventDetailsActivity, PDFViewActivity::class.java)
                browserIntent.putExtra(
                    "pdf_url",
                    mEventModels!![position].parent_consent_pdf_link
                )
                browserIntent.putExtra("title", "Events")
                browserIntent.putExtra("filename", mEventModels!![position].name)
                startActivity(browserIntent)*/
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.parent_form_not_available),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            }
        }
        parentConsernForm!!.setOnClickListener {
            if (!mEventModels!![position].parent_consent_pdf_link.equals("")) {
                //                stopDisconnectTimer();
                /*  Intent intent = new Intent(EventDetailsActivity.this, PdfReader.class);
                    intent.putExtra("pdf_url", mEventModels.get(position).getParent_consent_pdf_link());
                    startActivity(intent);*/
               /* val browserIntent = Intent(this@EventDetailsActivity, PDFViewActivity::class.java)
                browserIntent.putExtra(
                    "pdf_url",
                    mEventModels!![position].parent_consent_pdf_link
                )
                browserIntent.putExtra("title", "Events")
                browserIntent.putExtra("filename", mEventModels!![position].name)
                startActivity(browserIntent)*/
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    getString(R.string.parent_form_not_available),
                    R.drawable.infoicon,
                    R.drawable.roundblue
                )
            }
        }
        addToCalendar!!.setOnClickListener {
            var startTime: Long = 0
            var endTime: Long = 0
            try {
                val dateStart = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                    mEventModels!![position].start_date
                )
                startTime = dateStart.time
                val dateEnd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(
                    mEventModels!![position].end_date
                )
                endTime = dateEnd.time
            } catch (e: Exception) {
            }
            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("beginTime", startTime)
            intent.putExtra("allDay", false)
            intent.putExtra("rrule", "FREQ=YEARLY")
            intent.putExtra("endTime", endTime)
            intent.putExtra("title", mEventModels!![position].name)
            startActivity(intent)
        }
    }
    private fun setValues() {
        eventTitle!!.setText(mEventModels!![position].name)
        if (mEventModels!![position].type.equals("1")) {
            if (mEventModels!![position].pdf_link.equals("")) {
                registrationRelative!!.visibility = View.GONE
            } else {
                registrationRelative!!.visibility = View.VISIBLE
            }
            if (mEventModels!![position].parent_consent_pdf_link.equals("")) {
                parentConsernRelative!!.visibility = View.GONE
            } else {
                parentConsernRelative!!.visibility = View.VISIBLE
            }
        } else {
            registrationRelative!!.visibility = View.GONE
        }
        if (mEventModels!![position].parent_consent_pdf_link.equals("")) {
            parentConsernRelative!!.visibility = View.GONE
        } else {
            parentConsernRelative!!.visibility = View.VISIBLE
        }
        if (!mEventModels!![position].pdf_link.equals("")) {
            pdfDownloadImgView!!.visibility = View.VISIBLE
        } else {
            pdfDownloadImgView!!.visibility = View.GONE
        }
        if (mEventModels!![position].type.equals("0")) {
            eventAttendStatus!!.visibility = View.GONE
            submitAttend!!.visibility = View.GONE
        }
        if (mEventModels!![position].id.equals(
                AppUtilityMethods.stringFromBase64Encoded(
                    AppPreferenceManager().getStudentId(mContext)
                )
            )
        ) {
            /* submitAttend.setBackgroundResource(R.drawable.event_attend_drwable_btn);
        submitAttend.setTextColor(getResources().getColor(R.color.white));
        submitAttend.setText("Attending");*/
            eventAttendStatus!!.text = "Status: Attending"
            submitAttend!!.text = "NOT ATTENDING"
            //submitAttend.setVisibility(View.GONE);
        } else {
            eventAttendStatus!!.text = "Status: Not Attending"
            submitAttend!!.text = "ATTENDING"
        }
        start_date!!.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mEventModels!![position].start_date))
        start_time!!.setText(
            AppUtilityMethods.separateTime(mEventModels!![position].start_date)
                .replace(".", "")
        )
        end_date!!.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mEventModels!![position].end_date))
        end_time!!.setText(
            AppUtilityMethods.separateTime(mEventModels!![position].end_date).replace(".", "")
        )
        eventDesc!!.text =
            Html.fromHtml(
                "<html><body style=\"text-align:justify\">" + mEventModels!![position].description
                    !!.replace("\n", "<br>").replace("\r", "&nbsp").toString() + "</body></html>"
            )
        /*Glide.with(mContext).load(mEventModels.get(position).getImage().replace(" ", "%20")).listener(new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            pDialog.dismiss();
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            pDialog.dismiss();
            return false;
        }
    }).centerCrop().placeholder(R.drawable.noimage).dontAnimate().into(bannerImage);*/if (!mEventModels!![position].image
                .equals("")
        ) {
            Picasso.with(mContext).load(mEventModels!![position].image!!.replace(" ", "%20"))
                .fit()
                .into(bannerImage, object : Callback {
                    override fun onSuccess() {
                       // pDialog.dismiss()
                    }

                    override fun onError() {
                       // pDialog.dismiss()
                    }
                })
        } else {
           // pDialog.dismiss()
        }
    }

    /*public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(EventDetailsActivity.this, false);
            AppPreferenceManager.setUserId(EventDetailsActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(EventDetailsActivity.this, false);
            AppPreferenceManager.setSchoolSelection(EventDetailsActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(EventDetailsActivity.this, LoginActivity.class);
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

    override fun onPostResume() {
        super.onPostResume()
        if (AppPreferenceManager().getUserId(mContext).equals("")) {
            val mIntent = Intent(this@EventDetailsActivity, LoginActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mIntent)
            finish()
        }
    }


    fun showDialogEventConfirmAlert(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_switch_dialog)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btn_signup) as Button
        dialogButton.setOnClickListener {
            resetDisconnectTimer()
            dialog.dismiss()
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
              //  callventRegistrationAPI()
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    "Network Error",
                    getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }

            //dialog.dismiss();
            /* showDialogRegSuccAlert((Activity)mContext,getString(R.string.alert_heading),
                        "Successfully registered for the event "+mEventModels.get(position).getName(),R.drawable.tick,  R.drawable.roundblue);*/
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_maybelater) as Button
        dialogButtonCancel.setOnClickListener {
            resetDisconnectTimer()
            dialog.dismiss()
            eventAttendStatus!!.text = "Attending"
        }
        dialog.show()
    }
    fun showDialogEventUnJoinConfirmAlert(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_switch_dialog)
        val icon = dialog.findViewById<ImageView>(R.id.iconImageView)
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<TextView>(R.id.text_dialog)
        val textHead = dialog.findViewById<TextView>(R.id.alertHead)
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<Button>(R.id.btn_signup)
        dialogButton.setOnClickListener {
            resetDisconnectTimer()
            dialog.dismiss()
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                //callUnJoinventRegistrationAPI(URL_CALLUNJOIN);
                calleventUnRegistrationAPI()
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    "Network Error",
                    getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }
        }
        val dialogButtonCancel = dialog.findViewById<Button>(R.id.btn_maybelater)
        dialogButtonCancel.setOnClickListener {
            resetDisconnectTimer()
            dialog.dismiss()
            // eventAttendStatus.setText("Attending");
        }
        dialog.show()
    }

    private fun calleventUnRegistrationAPI() {
        val call: Call<EventResponseModel> = ApiClient.getApiService().eventsreg(
            11,
            AppPreferenceManager().getAccessToken(mContext).toString(),
            mEventModels!!.get(position).event_id.toString(),
            AppPreferenceManager().getStudentId(mContext).toString(),
            AppPreferenceManager().getUserId(mContext).toString()
        )
        call.enqueue(object : retrofit2.Callback<EventResponseModel> {
            override fun onResponse(
                call: Call<EventResponseModel>,
                response: Response<EventResponseModel>
            ) {
               /* eventModelsArrayList=response.body()!!.response.about
                val mAboutusRecyclerviewAdapter = EventListAdapter(mContext, eventModelsArrayList)
                eventsList!!.adapter = mAboutusRecyclerviewAdapter*/

            }

            override fun onFailure(call: Call<EventResponseModel?>, t: Throwable) {
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
}