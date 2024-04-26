package com.willow.todolist.Activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.willow.todolist.Adapters.MissionAdapter
import com.willow.todolist.Datas.Database
import com.willow.todolist.R
import com.willow.todolist.databinding.ActivityCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var db: Database
    private lateinit var missionAdapter: MissionAdapter

    val time = Calendar.getInstance().time
    val dateFormat: SimpleDateFormat? = SimpleDateFormat("dd/MM/yyyy")
    val current = dateFormat?.format(time)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMenu()

        db = Database(this)

        var date = current?.format(dateFormat).toString()

        missionAdapter = MissionAdapter(db.getByDate(date), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = missionAdapter


        var myCalendar: Calendar = Calendar.getInstance()
        binding.calendarView.setOnDateChangeListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date = dateFormat?.format(myCalendar.time).toString()

            Log.e(TAG, "$date")
            missionAdapter = MissionAdapter(db.getByDate(date), this)
            binding.recyclerView.adapter = missionAdapter

        }


    }

    private fun setMenu() {
        binding.chipNavigationBar.setItemSelected(R.id.calendar, true)
        binding.chipNavigationBar.setOnItemSelectedListener {
            if (it == R.id.list) {
                updateUI(this, MainActivity::class.java)
                finish()
            }
        }
    }

    private fun updateUI(context: Context, targetActivity: Class<*>) {
        val intent = Intent(context, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        context.startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        updateUI(this, MainActivity::class.java)
        finish()
    }
}