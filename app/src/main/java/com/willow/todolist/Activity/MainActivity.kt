package com.willow.todolist.Activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.willow.todolist.Adapters.MissionAdapter
import com.willow.todolist.Datas.Database
import com.willow.todolist.Models.Mission
import com.willow.todolist.R
import com.willow.todolist.databinding.ActivityMainBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: Database
    private lateinit var missionAdapter: MissionAdapter
    val time = Calendar.getInstance().time
    var dateFormat: SimpleDateFormat? = SimpleDateFormat("dd/MM/yyyy")
    val current = dateFormat?.format(time)
    val date = current?.format(dateFormat).toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Database(this)

        missionAdapter = MissionAdapter(db.getAllMission(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = missionAdapter

        setMenu()
        binding.addMission.setOnClickListener {
            addMission()
        }

    }

    override fun onResume() {
        super.onResume()
        missionAdapter.refreshData(db.getAllMission())
    }

    private fun addMission() {
        var fmtTime: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
        var myCalendar: Calendar = Calendar.getInstance()

        val builder = AlertDialog.Builder(this)

        val layoutAlert: View = layoutInflater.inflate(R.layout.layout_alert, null)
        builder.setView(layoutAlert)
        builder.setCancelable(true)

        val setTime = layoutAlert.findViewById<ImageButton>(R.id.btn_time)
        val setDate = layoutAlert.findViewById<ImageButton>(R.id.btn_date)
        val txtTime = layoutAlert.findViewById<TextView>(R.id.txtTime)
        val txtDate = layoutAlert.findViewById<TextView>(R.id.txtDate)
        val txtContent = layoutAlert.findViewById<EditText>(R.id.txtMission).text

        txtDate.text = dateFormat?.format(myCalendar.time)
        txtTime.text = fmtTime.format(myCalendar.time)

        var d = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            txtDate.text = dateFormat?.format(myCalendar.time)
        }
        var t = OnTimeSetListener { view, hourOfDay, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            txtTime.text = fmtTime.format(myCalendar.time)
        }

        setTime.setOnClickListener {
            TimePickerDialog(
                this@MainActivity, t,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE), true
            ).show()
        }

        setDate.setOnClickListener {
            DatePickerDialog(
                this@MainActivity, d,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        builder.setNegativeButton("Hủy") { dialog, which ->

        }
        builder.setPositiveButton("Lưu") { dialog, which ->
            if (txtContent.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung!!", Toast.LENGTH_SHORT).show()
                addMission()
            } else {
                val content = txtContent.toString()
                val time = txtTime.text.toString()
                val date = txtDate.text.toString()
                val mission = Mission(0, content, time, date, status = 0)

                db.insertData(mission)

                Toast.makeText(this, "Mission saved!", Toast.LENGTH_SHORT).show()

                onResume()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun setMenu() {
        binding.chipNavigationBar.setItemSelected(R.id.list, true)
        binding.chipNavigationBar.setOnItemSelectedListener {
            if (it == R.id.calendar) {
                updateUI(this, CalendarActivity::class.java)
                finish()
            }
        }
    }

    fun updateUI(context: Context, targetActivity: Class<*>) {
        val intent = Intent(context, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        context.startActivity(intent)
    }

}



