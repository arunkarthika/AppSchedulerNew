package com.sankarwap.appscheduler.Applist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.switchmaterial.SwitchMaterial
import com.sankarwap.appscheduler.util.OnToggleAppListener
import com.sankarwap.appscheduler.R
import com.sankarwap.appscheduler.data.App.AppList
import java.util.*

class ApplistAdapter(
        val context: Context,
        items: List<AppList>,
        listenert: OnToggleAppListener
) : RecyclerView.Adapter<ApplistAdapter.ViewHolder>(), Filterable {

    private val original_list: List<AppList> = items
    private var ListFiltered: List<AppList> = items
    private var listenert=listenert

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.name.text = ListFiltered[p1].name
        holder.email.text = ListFiltered[p1].packages

        holder.userImage.load(ListFiltered[p1].icon){
            placeholder(R.drawable.placeholder)
            error(R.drawable.placeholder)
        }

        holder.app_start_toogle.setOnCheckedChangeListener(null)
        holder.app_start_toogle.isChecked = ListFiltered[p1].enable

        holder.app_start_toogle.setOnCheckedChangeListener { _, isChecked ->
                if(ListFiltered.getOrNull(p1)!=null){
                    original_list.get(original_list.indexOf(ListFiltered[p1])).enable=isChecked
                    listenert.onToggle(ListFiltered[p1], isChecked)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ListFiltered.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView =itemView.findViewById(R.id.email)
        val app_start_toogle: SwitchMaterial =itemView.findViewById(R.id.app_start)

    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) ListFiltered = original_list else {
                    val filteredList = ArrayList<AppList>()
                    original_list
                        .filter {
                            it.name.toLowerCase(Locale.getDefault())
                                .contains(charString.toLowerCase(Locale.getDefault())) or it.packages.contains(
                                    constraint!!
                            )
                        }
                        .forEach { filteredList.add(it) }
                    ListFiltered = filteredList
                }
                return FilterResults().apply { values = ListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ListFiltered = results?.values as ArrayList<AppList>
                notifyDataSetChanged()
            }
        }
    }

}