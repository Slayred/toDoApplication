package com.chibisov.todoapplication.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chibisov.todoapplication.R
import com.chibisov.todoapplication.data.model.Priority
import com.chibisov.todoapplication.data.model.ToDoData
import org.w3c.dom.Text

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    //List of Data witch will displayed in RecycleView
    var dataList = emptyList<ToDoData>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        //Inflate layout (one element) and return them
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.title_txt).text = dataList[position].title
        holder.itemView.findViewById<TextView>(R.id.description_txt).text = dataList[position].description

        val markPriority = holder.itemView.findViewById<CardView>(R.id.priority_indicator)


        when (dataList[position].priority){
            Priority.HIGH -> markPriority.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            Priority.MEDIUM -> markPriority.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow))
            Priority.LOW -> markPriority.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }
    }

    override fun getItemCount(): Int {
        //return size of elements in rv
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>){
        this.dataList = toDoData
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}