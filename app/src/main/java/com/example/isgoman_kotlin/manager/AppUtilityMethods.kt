package com.example.isgoman_kotlin.manager

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Dialog
import android.app.KeyguardManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.login.LoginActivity
import com.example.isgoman_kotlin.constants.JsonTagConstants
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AppUtilityMethods :  JsonTagConstants {
    var mContext: Context? = null
    private var count = 0

    //internet checking
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return if (ni == null) {
            // There are no active networks.
            false
        } else true
    }

    //valid email checking
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    //date parsing
    fun dateParsingToDdMmmYyyy(date: String?): String {
        var strCurrentDate = ""
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var newDate: Date? = null
        try {
            newDate = format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        format = SimpleDateFormat("dd MMMM yyyy HH:mm a")
        strCurrentDate = format.format(newDate)
        return strCurrentDate
    }

    fun dateParsingToDdMmmYyyywithoutTime(date: String?): String {
        var strCurrentDate = ""
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var newDate: Date? = null
        try {
            newDate = format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        format = SimpleDateFormat("dd MMMM yyyy")
        strCurrentDate = format.format(newDate)
        return strCurrentDate
    }

    fun dateParsingToDdmmYyyy(date: String?): String {
        var strCurrentDate = ""
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var newDate: Date? = null
        try {
            newDate = format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        format = SimpleDateFormat("dd MMM yyyy")
        strCurrentDate = format.format(newDate)
        return strCurrentDate
    }

    fun dateParsingToDdmmYyyyAmPm(date: String?): String {
        var strCurrentDate = ""
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var newDate: Date? = null
        try {
            newDate = format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        format = SimpleDateFormat("dd MMM yyyy hh:mm a")
        strCurrentDate = format.format(newDate)
        return strCurrentDate
    }

    //function to encode string into base 64
    fun base64EncodedString(inputString: String): String {
        val data: ByteArray
        var base64 = ""
        try {
            data = inputString.toByteArray(charset("UTF-8"))
            base64 = Base64.encodeToString(data, Base64.DEFAULT)
            Log.i("Base 64 ", base64)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return base64.replace("+/=", "-_~")
    }

    //decode base 64 to string
    fun stringFromBase64Encoded(inputString: String?): String {
        val decodeValue = Base64.decode(inputString, Base64.DEFAULT)
        Log.i("Base 64 ", String(decodeValue).replace("-_~", "+/="))
        return String(decodeValue).replace("-_~", "+/=")
    }



    fun showDialogAlertDismiss(
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

    //hide keyboard
    fun hideKeyBoard(context: Context) {
        val imm = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        /* if (imm.isAcceptingText()) {

            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);

        }*/
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.window.decorView.rootView.windowToken, 0
        )
    }

    /*public static void showDialogAlertDismissFinish(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_ok_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

    }
*/
    fun showDialogAlertLogout(
        activity: Activity,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
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
            AppPreferenceManager().setIsGuest(activity, false)
            AppPreferenceManager().setUserId(activity, "")
            AppPreferenceManager().setIsUserAlreadyLoggedIn(activity, false)
            AppPreferenceManager().setSchoolSelection(activity, "ISG")
            val mIntent = Intent(activity, LoginActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(mIntent)
            activity.finish()
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.btn_maybelater) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun showAlertFinish(
        activity: Activity, message: String?,
        okBtnTitle: String?, cancelBtnTitle: String?, okBtnVisibility: Boolean
    ) {
        // custom dialog
        val dialog = Dialog(activity, R.style.NewDialog)
        dialog.setContentView(R.layout.custom_alert_dialog)
        dialog.setCancelable(false)

        // set the custom dialog components - text, image, button
        val text = dialog.findViewById<View>(R.id.text) as TextView
        text.text = message
        val sdk = Build.VERSION.SDK_INT
        val dialogCancelButton = dialog
            .findViewById<View>(R.id.dialogButtonCancel) as Button
        dialogCancelButton.text = cancelBtnTitle
        /*if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            dialogCancelButton.setBackgroundDrawable(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        } else {
            dialogCancelButton.setBackground(AppUtils
                    .getButtonDrawableByScreenCathegory(activity,
                            R.color.split_bg, R.color.list_selector));
        }*/
        // if button is clicked, close the custom dialog
        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
            activity.finish()
        }
        val dialogOkButton = dialog
            .findViewById<View>(R.id.dialogButtonOK) as Button
        dialogOkButton.visibility = View.GONE
        dialogOkButton.text = okBtnTitle
        if (okBtnVisibility) {
            dialogOkButton.visibility = View.VISIBLE
            /*if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                dialogOkButton.setBackgroundDrawable(AppUtils
                        .getButtonDrawableByScreenCathegory(activity,
                                R.color.split_bg, R.color.list_selector));
            } else {
                dialogOkButton.setBackground(AppUtils
                        .getButtonDrawableByScreenCathegory(activity,
                                R.color.split_bg, R.color.list_selector));
            }*/
            // if button is clicked, close the custom dialog
            dialogOkButton.setOnClickListener {
                dialog.dismiss()
                activity.finish()
            }
        }
        dialog.show()
    }

    //convert the input date to 12 January(dd MMMM) form
    fun separateDate(inputDate: String?): String {
        var mDate = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time
            val formatterFullDate = SimpleDateFormat("dd MMM yyyy")
            mDate = formatterFullDate.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mDate
    }

    fun separateTime(inputDate: String?): String {
        var mTime = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time
            val formatterTime = SimpleDateFormat("hh:mm a")
            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm:ss");
            mTime = formatterTime.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mTime
    }

    //convert input datetime into 12 January 2017 (dd MMMM yyyy) form
    fun separateDateTodDmMmMyYyY(inputDate: String?): String {
        var mDate = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time
            val formatterFullDate = SimpleDateFormat("dd MMMM yyyy")
            mDate = formatterFullDate.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mDate
    }

    fun replace(str: String): String {
        return str.replace(" ".toRegex(), "%20")
    }

    val currentDateToday: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val calendar = Calendar.getInstance()
            return dateFormat.format(calendar.time)
        }

    fun dateConversionY(inputDate: String?): String {
        var mDate = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            val formatterFullDate = SimpleDateFormat("dd MMMM yyyy")
            mDate = formatterFullDate.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mDate
    }

    fun htmlparsing(text: String): String {
        var encodedString: String
        encodedString = text.replace("&lt;".toRegex(), "<")
        encodedString = encodedString.replace("&gt;".toRegex(), ">")
        encodedString = encodedString.replace("&amp;".toRegex(), "")
        encodedString = encodedString.replace("amp;".toRegex(), "")
        return encodedString
    }

    fun changeDateFormat(inputDate: String?): String {
        var mDate = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time
            val formatterFullDate = SimpleDateFormat("dd MMMM yyyy ")
            mDate = formatterFullDate.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mDate
    }

    fun removeDate(inputDate: String?): String {
        var mTime = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            val formatterTime = SimpleDateFormat("hh:mm:ss")
            mTime = formatterTime.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mTime
    }

    fun removeDatetoAMPM(inputDate: String?): String {
        var mTime = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            val formatterTime = SimpleDateFormat("hh:mm a")
            mTime = formatterTime.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mTime
    }

    fun removeDateOnly(inputDate: String?): String {
        var mTime = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            val formatterTime = SimpleDateFormat("HH:mm:ss")
            mTime = formatterTime.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mTime
    }

    fun removeTime(inputDate: String?): String {
        var mTime = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time

            //SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a");
            val formatterTime = SimpleDateFormat("yyyy-MM-dd")
            mTime = formatterTime.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mTime
    }

    fun getMiliseconds(date: String, time: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date1 = sdf.parse("$date $time")
            val cal1 = Calendar.getInstance()
            cal1.time = date1
            val beginCal = Calendar.getInstance()
            beginCal[cal1[Calendar.YEAR], cal1[Calendar.MONTH], cal1[Calendar.DAY_OF_MONTH], cal1[Calendar.HOUR_OF_DAY]] =
                cal1[Calendar.MINUTE]
            beginCal.timeInMillis
        } catch (e: Exception) {
            Date().time
        }
    }

    fun durationInSecondsToString(sec: Int): String {
        val hours = sec / 3600
        val minutes = sec / 60 - hours * 60
        val seconds = sec - hours * 3600 - minutes * 60
        return String.format(
            "%d:%02d:%02d", hours, minutes,
            seconds
        )
    }

    fun isAppInForeground(context: Context): Boolean {
        println("Working.....")
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfo = manager.getRunningTasks(1)
        val componentInfo = runningTaskInfo[0].topActivity
        val className = componentInfo!!.className
        println("className::$className")
        return if (className.equals(
                "com.algubra.activity.login.LoginActivity",
                ignoreCase = true
            )
        ) {
            false
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val foregroundTaskInfo = am.getRunningTasks(1)[0]
                val foregroundTaskPackageName = foregroundTaskInfo.topActivity!!.packageName
                foregroundTaskPackageName.lowercase(Locale.getDefault()) == context.packageName.lowercase(
                    Locale.getDefault()
                )
            } else {
                val appProcessInfo = RunningAppProcessInfo()
                ActivityManager.getMyMemoryState(appProcessInfo)
                if (appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    return true
                }
                val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                // App is foreground, but screen is locked, so show notification
                km.inKeyguardRestrictedInputMode()
            }
        }
    }

    fun dateConversionyyyyMMddToDDMMYYYY(inputDate: String?): String {
        var mDate = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            val formatterFullDate = SimpleDateFormat("dd-MM-yyyy hh:mm a")
            mDate = formatterFullDate.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mDate
    }

    fun dateConversionStandardFormat(inputDate: String?): String {
        var mDate = ""
        try {
            val date: Date
            val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            date = formatter.parse(inputDate)
            //Subtracting 6 hours from selected time
            val time = date.time

            //SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
            val formatterFullDate = SimpleDateFormat("dd-MM-yyyy")
            mDate = formatterFullDate.format(time)
        } catch (e: Exception) {
            Log.d("Exception", "" + e)
        }
        return mDate
    }

    //force update
    fun showDialogAlertUpdate(mContext: Context) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_update_version)
        val btnUpdate = dialog.findViewById<View>(R.id.btnUpdate) as Button
        btnUpdate.setOnClickListener {
            dialog.dismiss()
            val appPackageName =
                mContext.packageName // getPackageName() from Context or Activity object
            try {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }
        dialog.show()
    }

    fun getVersionInfo(mContext: Context): String {
        var versionName = ""
        var versionCode = -1
        try {
            val packageInfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            versionName = packageInfo.versionName
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
        //    TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
//    textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
    }





}
