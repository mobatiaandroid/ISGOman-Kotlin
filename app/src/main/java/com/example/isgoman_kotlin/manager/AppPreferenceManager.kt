package com.example.isgoman_kotlin.manager

import android.content.Context

class AppPreferenceManager {
    val PREFSNAME = "NAS"
    fun setAccessToken(context: Context, accesstoken: String) {
        val pref = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("accesstoken", accesstoken)
        editor.apply()
    }

    fun getAccessToken(context: Context): String? {
        val pref = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        return pref.getString("accesstoken", "")
    }
    fun setHomePos(mContext: Context, accesstoken: String?) {
        val prefs = mContext.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("HomePos", accesstoken)
        editor.commit()
    }

    fun getHomePos(mContext: Context): String? {
        var tokenValue = ""
        val prefs = mContext.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        tokenValue = prefs.getString("HomePos", "0")!!
        return tokenValue
    }

    fun getIsGuest(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean("is_guest", true)
    }

    /*******************************************************
     * Method name : setIfHomeItemClickEnabled() Description : set if home list
     * item click is enabled Parameters : context, result Return type : void
     * Date : 11-Nov-2014 Author : Vandana Surendranath
     */
    fun setIsGuest(context: Context, result: Boolean) {
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean("is_guest", result)
        editor.commit()
    }

    fun getSchoolSelection(context: Context): String? {
        var tokenValue = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        tokenValue = prefs.getString("selection", "ISG")!!
        return tokenValue
    }

    /*******************************************************
     * Method name : setIfHomeItemClickEnabled() Description : set if home list
     * item click is enabled Parameters : context, result Return type : void
     * Date : 11-Nov-2014 Author : Vandana Surendranath
     */
    fun setSchoolSelection(context: Context, result: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("selection", result)
        editor.commit()
    }

    fun getMobileNo(context: Context): String? {
        var mobile = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        mobile = prefs.getString("mobile", "")!!
        return mobile
    }

    fun setMobileNo(context: Context, mobile: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("mobile", mobile)
        editor.commit()
    }

    fun getIsUserAlreadyLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean("is_loggedin", false)
    }

    /*******************************************************
     * Method name : setIfHomeItemClickEnabled() Description : set if home list
     * item click is enabled Parameters : context, result Return type : void
     * Date : 11-Nov-2014 Author : Vandana Surendranath
     */
    fun setIsUserAlreadyLoggedIn(context: Context, result: Boolean) {
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean("is_loggedin", result)
        editor.commit()
    }

    fun getUserId(context: Context): String? {
        var userId = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        userId = prefs.getString("userId", "")!!
        return userId
    }

    fun setUserId(context: Context, userId: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("userId", userId)
        editor.commit()
    }

    fun getStudentsResponseFromLoginAPI(context: Context): String? {
        var student_list = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        student_list = prefs.getString("student_list", "")!!
        return student_list
    }

    fun setStudentsResponseFromLoginAPI(context: Context, student_list: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("student_list", student_list)
        editor.commit()
    }

    fun getStudentId(context: Context): String? {
        var studentId = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        studentId = prefs.getString("studentId", "").toString()
        return studentId
    }

    fun setStudentId(context: Context, studentId: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("studentId", studentId)
        editor.commit()
    }

    fun getStudentName(context: Context): String? {
        var studentName = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        studentName = prefs.getString("studentName", "").toString()
        return studentName
    }

    fun setStudentName(context: Context, studentName: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("studentName", studentName)
        editor.commit()
    }

    fun getStudentClassName(context: Context): String? {
        var studentClassName = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        studentClassName = prefs.getString("studentClassName", "").toString()
        return studentClassName
    }

    fun setStudentClassName(context: Context, studentClassName: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("studentClassName", studentClassName)
        editor.commit()
    }

    fun getStudentSectionName(context: Context): String? {
        var studentSectionName = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        studentSectionName = prefs.getString("studentSectionName", "").toString()
        return studentSectionName
    }

    fun setStudentSectionName(context: Context, studentSectionName: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("studentSectionName", studentSectionName)
        editor.commit()
    }

    fun getPhno(context: Context): String? {
        var Phno = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        Phno = prefs.getString("Phno", "").toString()
        return Phno
    }

    fun setPhno(context: Context, Phno: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("Phno", Phno)
        editor.commit()
    }

    fun getBadgecount(context: Context): String? {
        var badgecount = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        badgecount = prefs.getString("badgecount", "0")!!
        return badgecount
    }

    fun setBadgecount(context: Context, badgecount: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("badgecount", badgecount)
        editor.commit()
    }

    fun getUserRespFromLoginAPI(context: Context): String? {
        var user_resp = ""
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        user_resp = prefs.getString("user_resp", "").toString()
        return user_resp
    }

    fun setUserRespFromLoginAPI(context: Context, user_resp: String?) {
        val prefs = context.getSharedPreferences("ALGUBRA", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("user_resp", user_resp)
        editor.commit()
    }

    /*********** Force Update  */
    fun setVersionFromApi(context: Context, result: String?) {
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("android_version", result)
        editor.commit()
    }

    fun getVersionFromApi(context: Context): String? {
        val prefs = context.getSharedPreferences(
            "ALGUBRA",
            Context.MODE_PRIVATE
        )
        return prefs.getString("android_version", "")
    }
}



