package com.sankarwap.appscheduler.alarmslist

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.sankarwap.appscheduler.util.MiddleDividerItemDecoration
import com.sankarwap.appscheduler.R
import com.sankarwap.appscheduler.util.SwipeToDeleteCallback
import com.sankarwap.appscheduler.data.Alarm
import com.sankarwap.appscheduler.util.showcase.GuideView
import com.sankarwap.appscheduler.util.showcase.config.DismissType

class AlarmsListFragment : Fragment(), OnToggleAlarmListener {
    private var alarmRecyclerViewAdapter: AlarmRecyclerViewAdapter? = null
    private var alarmsListViewModel: AlarmsListViewModel? = null
    private var alarmsRecyclerView: RecyclerView? = null
    private var data:MutableList<Alarm> = ArrayList()
    private var animation_view:LottieAnimationView? = null

    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null
    private lateinit var add_btn: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmRecyclerViewAdapter = AlarmRecyclerViewAdapter(this)
        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel::class.java)
        alarmsListViewModel!!.alarmsLiveData.observe(this, { alarms ->
            if (alarms != null) {

                if(alarms.isEmpty()){
                    animation_view!!.visibility=View.VISIBLE
                    shou_case(add_btn)
                }else{
                animation_view!!.visibility=View.GONE
                data.clear()
                data.addAll(alarms)
                alarmRecyclerViewAdapter!!.setAlarms(data)
                }
            }else{
                animation_view!!.visibility=View.VISIBLE
                shou_case(add_btn)

            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_listalarms, container, false)
        alarmsRecyclerView = view.findViewById(R.id.fragment_listalarms_recylerView)
        animation_view = view.findViewById(R.id.animation_view)
        add_btn = view.findViewById(R.id.add_btn)

        animation_view!!.visibility=View.GONE
        alarmsRecyclerView!!.addItemDecoration(MiddleDividerItemDecoration(requireContext(), MiddleDividerItemDecoration.VERTICAL))


        alarmsRecyclerView!!.layoutManager = LinearLayoutManager(context)
        alarmsRecyclerView!!.adapter = alarmRecyclerViewAdapter


        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                adapter.removeAt(viewHolder.adapterPosition)
                val alaram=data[viewHolder.adapterPosition]
                if(alaram.isStarted){
                    alaram.cancelAlarm(requireContext())
                }
                alarmsListViewModel!!.delete_alaram(alaram.alarmId)

                data.removeAt(viewHolder.adapterPosition)
                alarmRecyclerViewAdapter!!.notifyDataSetChanged()
                Toast.makeText(requireContext(),"Deleted Schedule",Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(alarmsRecyclerView)


        add_btn!!.setOnClickListener { v: View? ->
            Navigation.findNavController(v!!).navigate(R.id.scheduleFragment)
        }
        return view
    }

    override fun onToggle(alarm: Alarm) {
        if (alarm.isStarted) {
            alarm.cancelAlarm(requireContext())
            alarmsListViewModel!!.update(alarm)
        } else {
            alarm.schedule(requireContext())
            alarmsListViewModel!!.update(alarm)
        }
    }

    private fun shou_case(view: View) {
        builder = GuideView.Builder(requireContext())
                .setTitle("Schedule the time using this Add Option")
                .setContentText("Your schedule is empty, Please add schedule.")
                .setGravity(com.sankarwap.appscheduler.util.showcase.config.Gravity.center)
                .setDismissType(DismissType.outside)
                .setTargetView(view)
                .setGuideListener { view ->
                    when (view) {
                        view -> {

                        }
                    }
                }

        mGuideView = builder!!.build()
        mGuideView!!.show()
    }

}