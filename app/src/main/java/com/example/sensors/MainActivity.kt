package com.example.sensors



import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sensors.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null
    private val screenWidth
        get() = resources.displayMetrics.widthPixels
    private val screenHeight
        get() = resources.displayMetrics.heightPixels

    companion object {
        private const val BALL_SPEED_FACTOR = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



    sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        when(p0!!.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val newX = p0.values[0]
                val newY = p0.values[1]
                val newZ = p0.values[2]
                binding.tvSensorData.text = "x: $newX\ny: $newY\nz: $newZ"

                // Calculate the new position of the ball
                val ballX = binding.ivBall.x - newX * BALL_SPEED_FACTOR
                val ballY = binding.ivBall.y + newY * BALL_SPEED_FACTOR

                // Ensure the ball stays within the screen bounds
                val maxX = screenWidth - binding.ivBall.width
                val maxY = screenHeight - binding.ivBall.height

                if (ballX < 0) {
                    binding.ivBall.x = 0f
                } else if (ballX > maxX) {
                    binding.ivBall.x = maxX.toFloat()
                } else {
                    binding.ivBall.x = ballX
                }

                if (ballY < 0) {
                    binding.ivBall.y = 0f
                } else if (ballY > maxY) {
                    binding.ivBall.y = maxY.toFloat()
                } else {
                    binding.ivBall.y = ballY
                }
            }
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


    override fun onStart() {
        super.onStart()
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }

}