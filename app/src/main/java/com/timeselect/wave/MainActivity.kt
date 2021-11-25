package com.timeselect.wave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.timeselect.timeselector.TimeChanger

class MainActivity : AppCompatActivity(), TimeChanger {
    lateinit var scaleView: com.timeselect.timeselector.TimerPickView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scaleView = findViewById(R.id.timepickview)
        scaleView.setTimeChangeListener(this)


        val currentTime = scaleView.getSelectedTime()
        Log.d("MainActivity", "time = $currentTime")
    }

    override fun timeChanged(it: Int) {
        Log.d("MainActivity", "time = $it")

        val currentTime = scaleView.getSelectedTime()
        Log.d("MainActivity", "timefromview = $currentTime")
    }
}