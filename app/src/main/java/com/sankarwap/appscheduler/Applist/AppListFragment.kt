package com.sankarwap.appscheduler.Applist

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ferfalk.simplesearchview.SimpleSearchView
import com.sankarwap.appscheduler.activities.MainActivity
import com.sankarwap.appscheduler.util.OnToggleAppListener
import com.sankarwap.appscheduler.R
import com.sankarwap.appscheduler.data.AlarmDatabase
import com.sankarwap.appscheduler.data.App.App
import com.sankarwap.appscheduler.data.App.AppList


class AppListFragment : Fragment() , OnToggleAppListener {
    lateinit var searchView: SimpleSearchView
    private lateinit var recylerView: RecyclerView
    private var db: AlarmDatabase? = null

    lateinit var adapters: ApplistAdapter
    var data : ArrayList<AppList> = ArrayList()

    lateinit var progress:ContentLoadingProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_list, container, false).also {
            setHasOptionsMenu(true)
            recylerView = it.findViewById(R.id.recyclerview)
            searchView = it.findViewById(R.id.searchView)
            progress = it.findViewById(R.id.progress)
            progress.visibility=View.VISIBLE
            recylerView.visibility=View.GONE
            val toolbar: Toolbar =it.findViewById(R.id.toolbar)
            (requireActivity() as? MainActivity)?.setSupportActionBar(toolbar)
            adapters= ApplistAdapter(requireContext(),data,this)
            this.db = Room.databaseBuilder(requireContext(), AlarmDatabase::class.java, "alaram_database").allowMainThreadQueries().build()
            searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.d("SimpleSearchView", "Submit:$query")
                    adapters.filter.filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    Log.d("SimpleSearchView", "Text changed:$newText")
                    adapters.filter.filter(newText)
                    return false
                }

                override fun onQueryTextCleared(): Boolean {
                    Log.d("SimpleSearchView", "Text cleared")
                    adapters!!.notifyDataSetChanged()
                    return false
                }
            })

            Handler(Looper.getMainLooper()).postDelayed({
                app_list()
            }, 200.toLong())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        setupSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupSearchView(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)
    }

    private fun app_list() {
        val already=db!!.appDao()!!.getAllPackageName()
        data.clear()
        val packages = requireContext().packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (packageInfo in packages) {
            var enable=false
            if(already.contains(packageInfo.packageName)) {
                Log.d("data",packageInfo.processName)
                enable = true
            }
           data.add(AppList(packageInfo.loadLabel(requireContext().packageManager).toString(), packageInfo.loadIcon(requireContext().packageManager), packageInfo.packageName, enable))
        }

       data.sortByDescending  { it.enable }

        progress.visibility=View.GONE
        recylerView.visibility=View.VISIBLE
        recylerView.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=adapters
            hasFixedSize()
            isNestedScrollingEnabled=false
        }
    }

    override fun onToggle(app: AppList?,checked:Boolean?) {
        Log.d("enable", app!!.name)
        Log.d("enable", app.enable.toString())
        if(checked!!){
        val appData= App(app.name,"",app.packages,app.enable)
            db!!.appDao()!!.insertApps(appData)
       }else{
           db!!.appDao()!!.deleteBypackagename(app.packages)
       }
    }

}