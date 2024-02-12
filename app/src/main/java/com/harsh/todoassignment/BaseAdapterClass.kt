package com.harsh.todoassignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BaseAdapterClass(var arrayList: ArrayList<NoteEntity>) : BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Any {
        return "Any"
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var initView = LayoutInflater.from(p2?.context).inflate(R.layout.item_lv,p2,false)
        var title : TextView = initView.findViewById(R.id.txt_Title)
        var description : TextView = initView.findViewById(R.id.txt_Description)
        var date : TextView = initView.findViewById(R.id.txt_Date)
        title.setText(arrayList[p0].title)
        description.setText(arrayList[p0].description)
        date.setText(arrayList[p0].date)
        return initView

    }

    fun updateList(newList: List<NoteEntity>) {
        arrayList = newList as ArrayList<NoteEntity>
        notifyDataSetChanged()
    }
}