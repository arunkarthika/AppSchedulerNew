package com.sankarwap.appscheduler.createalarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sankarwap.appscheduler.R
import com.sankarwap.appscheduler.data.Alarm
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Schedulefragment : Fragment(), View.OnClickListener {

    private lateinit var emailTextField: TextInputLayout
    private lateinit var title: TextInputEditText
    private lateinit var itemAlarmTime: AppCompatTextView
    private lateinit var recurring: MaterialCheckBox
    private lateinit var recurringOptions: LinearLayout
    private lateinit var mon: CheckBox
    private lateinit var tue: CheckBox
    private lateinit var wed: CheckBox
    private lateinit var thu: CheckBox
    private lateinit var fri: CheckBox
    private lateinit var sat: CheckBox
    private lateinit var sun: CheckBox
    private lateinit var addSchedule: MaterialButton

    var hour = 0
    var mints = 0


    private var createAlarmViewModel: CreateAlarmViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel::class.java)
        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        mints = calendar.get(Calendar.MINUTE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_scheduleadd, container, false).also {
            emailTextField = it.findViewById(R.id.emailTextField)
            title = it.findViewById(R.id.title)
            itemAlarmTime = it.findViewById(R.id.item_alarm_time)
            recurring = it.findViewById(R.id.fragment_createalarm_recurring)
            recurringOptions = it.findViewById(R.id.fragment_createalarm_recurring_options)
            mon = it.findViewById(R.id.fragment_createalarm_checkMon)
            tue = it.findViewById(R.id.fragment_createalarm_checkTue)
            wed = it.findViewById(R.id.fragment_createalarm_checkWed)
            thu = it.findViewById(R.id.fragment_createalarm_checkThu)
            fri = it.findViewById(R.id.fragment_createalarm_checkFri)
            sat = it.findViewById(R.id.fragment_createalarm_checkSat)
            sun = it.findViewById(R.id.fragment_createalarm_checkSun)
            addSchedule = it.findViewById(R.id.add_schedule)

            var alarmText: String? = String.format("%02d:%02d", hour, mints)
            try {
                val sdf = SimpleDateFormat("H:mm")
                val dateObj = sdf.parse(alarmText)
                alarmText = SimpleDateFormat("hh:mm a").format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            itemAlarmTime.text = alarmText

            recurring.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    recurringOptions.visibility = View.VISIBLE
                } else {
                    recurringOptions.visibility = View.GONE
                }
            }

            addSchedule.setOnClickListener(this)
            itemAlarmTime.setOnClickListener(this)

        }
    }

    private fun timePicker() {

        var tHour=0
        var tMinsts=0
        if(hour!=0&&mints!=0){
            tHour=hour
            tMinsts=mints
        }else{
            val calendar = Calendar.getInstance()
            tHour=calendar.get(Calendar.HOUR_OF_DAY)
            tMinsts=calendar.get(Calendar.MINUTE)
        }
        val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(tHour)
                .setMinute(tMinsts)
                .setTitleText("Select Schedule time")
                .build()
        picker.show(requireActivity().supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            hour = picker.hour
            mints = picker.minute
            var alarmText: String? = String.format("%02d:%02d", hour, mints)
            try {
                val sdf = SimpleDateFormat("H:mm")
                val dateObj = sdf.parse(alarmText)
                alarmText = SimpleDateFormat("hh:mm a").format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            itemAlarmTime.text = alarmText
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            addSchedule -> {
                if (title.text.toString().isNotEmpty()) {
                    val alarmId = Random().nextInt(Int.MAX_VALUE)
                    val alarm = Alarm(
                            alarmId,
                            hour,
                            mints,
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
                    Navigation.findNavController(v).navigate(R.id.alarmsListFragment)
                } else {
                    Toast.makeText(requireContext(), "Enter Title", Toast.LENGTH_SHORT).show()
                }
            }
            itemAlarmTime -> {
                timePicker()
            }
        }
    }
}

