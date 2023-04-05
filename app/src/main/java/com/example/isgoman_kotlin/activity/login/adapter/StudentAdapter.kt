package com.example.isgoman_kotlin.activity.login.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.login.model.StudentModels
import com.squareup.picasso.Picasso

class StudentAdapter(private val mContext: Context, mStudentList: ArrayList<StudentModels>) :
    RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {
    private val mStudentList: ArrayList<StudentModels>
    var dept: String? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView
        var listTxtClass: TextView
        var imgIcon: ImageView

        init {
            mTitleTxt = view.findViewById<View>(R.id.listTxtTitle) as TextView
            listTxtClass = view.findViewById<View>(R.id.listTxtClass) as TextView
            imgIcon = view.findViewById<View>(R.id.imagicon) as ImageView
        }
    }

    init {
        this.mStudentList = mStudentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_adapter_student_listitem, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitleTxt.setText(mStudentList[position].name)
        holder.imgIcon.visibility = View.VISIBLE
        if (!mStudentList[position].student_photo.equals("")) {
            Picasso.with(mContext)
                .load(mStudentList[position].student_photo).fit()
                .placeholder(R.drawable.noimage)
                .into(holder.imgIcon)
        }
        holder.listTxtClass.setText(mStudentList[position].class_name + " " + mStudentList[position].student_division_name)
    }

    override fun getItemCount(): Int {
        return mStudentList.size
    }
}
