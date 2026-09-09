package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Android Foreground Service managing Live Ongoing Activity Notifications.
 *
 * Keeps the notification alive in status bar chip & notification drawer even when
 * the application is closed or minimized to background.
 *
 * @author Er. Mohd Amaan
 */
class ExpressiveLiveNotificationService : Service() {

    private lateinit var notificationManager: ExpressiveLiveNotificationManager
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var timerJob: Job? = null
    private var totalDurationSeconds = 300 // 5 Minutes
    private var remainingSeconds = 300
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        notificationManager = ExpressiveLiveNotificationManager(this)
        LiveServiceState.setServiceRunning(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val duration = intent.getIntExtra(EXTRA_DURATION, 300)
                startLiveSession(duration)
            }
            ACTION_PAUSE -> pauseLiveSession()
            ACTION_RESUME -> resumeLiveSession()
            ACTION_STOP -> stopLiveSession()
        }
        return START_STICKY
    }

    private fun startLiveSession(durationSeconds: Int) {
        totalDurationSeconds = durationSeconds
        remainingSeconds = durationSeconds
        isRunning = true

        val initialNotification = notificationManager.buildLiveNotification(
            timerString = formatTime(remainingSeconds),
            isRunning = true,
            progressPercent = calculateProgressPercent()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    ExpressiveLiveNotificationManager.NOTIFICATION_ID,
                    initialNotification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(
                    ExpressiveLiveNotificationManager.NOTIFICATION_ID,
                    initialNotification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
                )
            }
        } else {
            startForeground(ExpressiveLiveNotificationManager.NOTIFICATION_ID, initialNotification)
        }

        runTimerLoop()
    }

    private fun runTimerLoop() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (isActive && isRunning && remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--
                val formattedTime = formatTime(remainingSeconds)
                val progress = calculateProgressPercent()

                LiveServiceState.updateState(
                    formattedTime = formattedTime,
                    remainingSecs = remainingSeconds,
                    totalSecs = totalDurationSeconds,
                    active = true,
                    paused = false
                )

                notificationManager.updateNotification(formattedTime, true, progress)
            }

            if (remainingSeconds <= 0) {
                stopLiveSession()
            }
        }
    }

    private fun pauseLiveSession() {
        isRunning = false
        timerJob?.cancel()

        val formattedTime = formatTime(remainingSeconds)
        val progress = calculateProgressPercent()

        LiveServiceState.updateState(
            formattedTime = formattedTime,
            remainingSecs = remainingSeconds,
            totalSecs = totalDurationSeconds,
            active = true,
            paused = true
        )

        notificationManager.updateNotification(formattedTime, false, progress)
    }

    private fun resumeLiveSession() {
        isRunning = true
        runTimerLoop()
    }

    private fun stopLiveSession() {
        isRunning = false
        timerJob?.cancel()
        notificationManager.cancelNotification()
        LiveServiceState.resetState()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun calculateProgressPercent(): Int {
        if (totalDurationSeconds <= 0) return 0
        val elapsed = totalDurationSeconds - remainingSeconds
        return ((elapsed.toFloat() / totalDurationSeconds.toFloat()) * 100).toInt().coerceIn(0, 100)
    }

    private fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        LiveServiceState.setServiceRunning(false)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "com.ermohdamaan.justforpixel.ACTION_START_LIVE"
        const val ACTION_PAUSE = "com.ermohdamaan.justforpixel.ACTION_PAUSE_LIVE"
        const val ACTION_RESUME = "com.ermohdamaan.justforpixel.ACTION_RESUME_LIVE"
        const val ACTION_STOP = "com.ermohdamaan.justforpixel.ACTION_STOP_LIVE"
        const val EXTRA_DURATION = "extra_duration_seconds"
    }
}

/**
 * Global State Observer for Live Service UI observing in Compose.
 *
 * @author Er. Mohd Amaan
 */
object LiveServiceState {

    private val _formattedTime = MutableStateFlow("05:00")
    val formattedTime: StateFlow<String> = _formattedTime.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(300)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isServiceActive = MutableStateFlow(false)
    val isServiceActive: StateFlow<Boolean> = _isServiceActive.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    fun updateState(formattedTime: String, remainingSecs: Int, totalSecs: Int, active: Boolean, paused: Boolean) {
        _formattedTime.value = formattedTime
        _remainingSeconds.value = remainingSecs
        _isServiceActive.value = active
        _isPaused.value = paused
    }

    fun setServiceRunning(running: Boolean) {
        _isServiceActive.value = running
    }

    fun resetState() {
        _formattedTime.value = "05:00"
        _remainingSeconds.value = 300
        _isServiceActive.value = false
        _isPaused.value = false
    }
}
