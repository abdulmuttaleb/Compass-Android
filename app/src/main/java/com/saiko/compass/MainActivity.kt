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
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var compassNeedleImageView:AppCompatImageView

    lateinit var mSensorManager:SensorManager
    var mMagenticSensor: Sensor? = null

    private val DegreeStart: Float = -45f
    private var currentDegree:Float = DegreeStart

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
        compassNeedleImageView = findViewById(R.id.iv_compass_needle)
    }
    private fun activityInit(){
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mMagenticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mMagenticSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
       val degree: Float = event!!.values[0]

        val rotateAnimation = RotateAnimation(
            currentDegree,
            -degree,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.isFillEnabled = true
        rotateAnimation.fillAfter = true
        rotateAnimation.duration = 210

        compassNeedleImageView.startAnimation(rotateAnimation)
        currentDegree = -degree

        Log.e(TAG, "sensorChanged: $degree")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.e(TAG, "Accuracy changed $p0 and $p1")
    }

    companion object{
        const val NOT_SUPPORTED_MESSAGE = "Sorry, sensor isn't supported for this device!"
        const val TAG = "MainActivity"
    }
}
