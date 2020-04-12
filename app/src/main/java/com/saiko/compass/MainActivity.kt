package com.saiko.compass

import android.annotation.TargetApi
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var tempTextView:TextView
    lateinit var mSensorManager:SensorManager
    var mTemperature: Sensor? = null


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar!!.hide()

        activityUiInit()
        activityInit()
    }

    private fun activityUiInit(){
        tempTextView = findViewById(R.id.tv_temp)
    }
    private fun activityInit(){
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if(mTemperature == null)
            tempTextView.text =
                NOT_SUPPORTED_MESSAGE
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
       val ambient_temp = event!!.values[0]
        tempTextView.text = "Ambient Temperature is: $ambient_temp"
        Log.e(TAG, "sensorChanged: $ambient_temp")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.e(TAG, "Accuracy changed $p0 and $p1")
    }

    companion object{
        const val NOT_SUPPORTED_MESSAGE = "Sorry, sensor isn't supported for this device!"
        const val TAG = "MainActivity"
    }
}
