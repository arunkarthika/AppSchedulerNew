package com.sankarwap.appscheduler.createalarm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sankarwap.appscheduler.R
import com.sankarwap.appscheduler.data.Alarm
import java.util.*


class CreateAlarmFragment : Fragment() {


    lateinit var fragmentCreatealarmScheduleAlarmHeading: TextView
    lateinit var fragmentCreatealarmTimePickerLayout: LinearLayout
    lateinit var timePicker: TimePicker
    lateinit var title: EditText
    lateinit var recurring: CheckBox
    lateinit var mon: CheckBox
    lateinit var tue: CheckBox
    lateinit var wed: CheckBox
    lateinit var thu: CheckBox
    lateinit var fri: CheckBox
    lateinit var sat: CheckBox
    lateinit var sun: CheckBox
    lateinit var scheduleAlarm: Button

    lateinit var recurringOptions: LinearLayout

    private var createAlarmViewModel: CreateAlarmViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_createalarm, container, false).also {


            fragmentCreatealarmScheduleAlarmHeading = it.findViewById(R.id.fragment_createalarm_scheduleAlarmHeading) as TextView
            fragmentCreatealarmTimePickerLayout = it.findViewById(R.id.fragment_createalarm_timePickerLayout) as LinearLayout
            timePicker = it.findViewById(R.id.fragment_createalarm_timePicker) as TimePicker
            title = it.findViewById(R.id.fragment_createalarm_title) as EditText
            recurring = it.findViewById(R.id.fragment_createalarm_recurring) as CheckBox
            recurringOptions = it.findViewById(R.id.fragment_createalarm_recurring_options) as LinearLayout
            mon = it.findViewById(R.id.fragment_createalarm_checkMon) as CheckBox
            tue = it.findViewById(R.id.fragment_createalarm_checkTue) as CheckBox
            wed = it.findViewById(R.id.fragment_createalarm_checkWed) as CheckBox
            thu = it.findViewById(R.id.fragment_createalarm_checkThu) as CheckBox
            fri = it.findViewById(R.id.fragment_createalarm_checkFri) as CheckBox
            sat = it.findViewById(R.id.fragment_createalarm_checkSat) as CheckBox
            sun = it.findViewById(R.id.fragment_createalarm_checkSun) as CheckBox
            scheduleAlarm = it.findViewById(R.id.fragment_createalarm_scheduleAlarm) as Button
        }

        recurring.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                recurringOptions!!.setVisibility(View.VISIBLE);
            } else {
                recurringOptions!!.setVisibility(View.GONE);
            }
        }

//        recurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                recurringOptions.setVisibility(View.VISIBLE);
//            } else {
//                recurringOptions.setVisibility(View.GONE);
//            }
//        });
        scheduleAlarm.setOnClickListener { v: View? ->
            scheduleAlarm()
            Navigation.findNavController(v!!).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment)
        }

        time_picker()

        return view
    }

    private fun time_picker() {
        val calendar = Calendar.getInstance()
        val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select Schedule time")
                .build()
        picker.show(requireActivity().supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            Log.d("time_hour", picker.hour.toString())
            Log.d("time_hour", picker.minute.toString())

        }
    }

    private fun scheduleAlarm() {
        val alarmId = Random().nextInt(Int.MAX_VALUE)
        val alarm = Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                title!!.text.toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked,
                mon.isChecked,
                tue.isChecked,
                wed.isChecked,
                thu.isChecked,
                fri.isChecked,
                sat.isChecked,
                sun.isChecked
        )
        createAlarmViewModel!!.insert(alarm)
        alarm.schedule(requireContext())
    }
}