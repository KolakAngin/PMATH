package com.syamsudinnoor.aft.aviation.pertamina.ui.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityStopWatchBinding
import com.syamsudinnoor.aft.aviation.pertamina.ui.stopwatch.adapter.LapAdapter


class StopWatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStopWatchBinding
    private enum class TimerState {
        STOPPED, RUNNING, PAUSED
    }

    private var timerState = TimerState.STOPPED
    private var milliseconds : Long = 0L
    private val handler  : Handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    // Komponen UI
    private lateinit var tvTimer: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnStartStop: FloatingActionButton
    private lateinit var btnLapReset: FloatingActionButton
    private lateinit var rvLaps: RecyclerView

    // Data Lap
    private val lapList = mutableListOf<String>()
    private lateinit var lapAdapter: LapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window,false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(window,window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = "Stopwatch"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvTimer = binding.tvTimer
        tvStatus = binding.tvStatus
        btnStartStop = binding.btnStartStop
        btnLapReset = binding.btnLapReset
        rvLaps = binding.rvLaps
        lapAdapter = LapAdapter(lapList)
        rvLaps.adapter = lapAdapter
        rvLaps.layoutManager = LinearLayoutManager(this)

        binding.btnStartStop.setOnClickListener {
            when (timerState) {
                TimerState.STOPPED, TimerState.PAUSED -> startTimer()
                TimerState.RUNNING -> pauseTimer()
            }
        }
        btnLapReset.setOnClickListener {
            when (timerState) {
                TimerState.RUNNING -> recordLap()
                TimerState.PAUSED -> resetTimer()
                else -> {} // Do nothing if stopped
            }
        }

        updateButtons()
    }

    private fun startTimer() {
        timerState = TimerState.RUNNING
        val startTime = System.currentTimeMillis() - milliseconds

        runnable = Runnable {
            milliseconds = System.currentTimeMillis() - startTime
            tvTimer.text = formatTime(milliseconds)
            handler.postDelayed(runnable, 10) // Update setiap 10ms
        }
        handler.post(runnable)

        tvStatus.text = "Running"
        updateButtons()
    }

    private fun pauseTimer() {
        timerState = TimerState.PAUSED
        handler.removeCallbacks(runnable)
        tvStatus.text = "Paused"
        updateButtons()
    }

    private fun resetTimer() {
        timerState = TimerState.STOPPED
        milliseconds = 0L
        tvTimer.text = formatTime(milliseconds)
        lapList.clear()
        lapAdapter.notifyDataSetChanged()
        tvStatus.text = "Stopped"
        updateButtons()
    }

    private fun recordLap() {
        lapList.add(0, formatTime(milliseconds)) // Tambah di awal list
        lapAdapter.notifyItemInserted(0)
        rvLaps.scrollToPosition(0) // Scroll ke lap terbaru
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.STOPPED -> {
                btnStartStop.setImageResource(R.drawable.ic_play)
                btnLapReset.setImageResource(R.drawable.ic_flag)
                btnLapReset.isEnabled = false
            }
            TimerState.RUNNING -> {
                btnStartStop.setImageResource(R.drawable.ic_pause)
                btnLapReset.setImageResource(R.drawable.ic_flag)
                btnLapReset.isEnabled = true
            }
            TimerState.PAUSED -> {
                btnStartStop.setImageResource(R.drawable.ic_play)
                btnLapReset.setImageResource(R.drawable.ic_replay)
                btnLapReset.isEnabled = true
            }
        }
    }

    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val hundredths = (ms % 1000) / 10 // Ambil 2 digit milidetik
        return String.format("%02d:%02d.%02d", minutes, seconds, hundredths)
    }
}