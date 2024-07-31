package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    private var startTime: Long = 0L
    private var timeInMilliseconds: Long = 0L
    private var updatedTime: Long = 0L
    private var timeSwapBuff: Long = 0L
    private var isRunning: Boolean = false
    private var isPaused: Boolean = false

    private val handler = Handler(Looper.getMainLooper())

    private val updateTimerThread = object : Runnable {
        override fun run() {
            if (isRunning) {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime
                updatedTime = timeSwapBuff + timeInMilliseconds

                val secs = (updatedTime / 1000).toInt()
                val mins = secs / 60
                val hrs = mins / 60
                val milliseconds = (updatedTime % 1000).toInt()
                timerTextView.text = String.format("%02d:%02d:%02d:%03d", hrs, mins, secs % 60, milliseconds)
                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timerTextView)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis()
                handler.post(updateTimerThread)
                isRunning = true
                isPaused = false
                startButton.visibility = Button.GONE
                pauseButton.visibility = Button.VISIBLE
                stopButton.visibility = Button.VISIBLE
                resetButton.visibility = Button.GONE
            }
        }

        pauseButton.setOnClickListener {
            if (isRunning && !isPaused) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(updateTimerThread)
                isRunning = false
                isPaused = true
                pauseButton.visibility = Button.GONE
                startButton.visibility = Button.VISIBLE
                stopButton.visibility = Button.VISIBLE
            }
        }

        stopButton.setOnClickListener {
            if (isRunning || isPaused) {
                timeSwapBuff += timeInMilliseconds
                handler.removeCallbacks(updateTimerThread)
                isRunning = false
                isPaused = false
                resetButton.visibility = Button.VISIBLE
                startButton.visibility = Button.VISIBLE
                pauseButton.visibility = Button.GONE
                stopButton.visibility = Button.GONE
            }
        }

        resetButton.setOnClickListener {
            startTime = 0L
            timeInMilliseconds = 0L
            timeSwapBuff = 0L
            updatedTime = 0L
            timerTextView.text = "00:00:00:000"
            isRunning = false
            isPaused = false
            resetButton.visibility = Button.GONE
            startButton.visibility = Button.VISIBLE
            pauseButton.visibility = Button.GONE
            stopButton.visibility = Button.GONE
        }
    }
}
