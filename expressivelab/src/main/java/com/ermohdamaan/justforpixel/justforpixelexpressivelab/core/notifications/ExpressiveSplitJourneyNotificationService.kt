package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.Build
import android.os.IBinder
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
 * Foreground Service for Android 17 Multi-Segment Split Focus & Rest Timer Notifications.
 *
 * Runs a 3-split timer loop (Split 1: Focus Session ➔ Split 2: Short Rest ➔ Split 3: Cooldown/Long Break).
 * Uses [ExpressiveSplitJourneyNotificationManager] for guaranteed immediate notification posting.
 *
 * @author Er. Mohd Amaan
 */
class ExpressiveSplitJourneyNotificationService : Service() {

    private lateinit var manager: ExpressiveSplitJourneyNotificationManager
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var journeyJob: Job? = null
    private var progressValue = 0 // 0 to 1000

    private var stage1ColorInt = Color.GREEN
    private var stage2ColorInt = Color.YELLOW
    private var stage3ColorInt = Color.CYAN

    override fun onCreate() {
        super.onCreate()
        manager = ExpressiveSplitJourneyNotificationManager(this)
        SplitJourneyState.setRunning(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_JOURNEY -> {
                stage1ColorInt = intent.getIntExtra(EXTRA_C1, Color.GREEN)
                stage2ColorInt = intent.getIntExtra(EXTRA_C2, Color.YELLOW)
                stage3ColorInt = intent.getIntExtra(EXTRA_C3, Color.CYAN)
                startJourney()
            }
            ACTION_STOP_JOURNEY -> stopJourney()
        }
        return START_STICKY
    }

    private fun startJourney() {
        progressValue = 50
        val initialNotification = manager.buildSplitJourneyNotification(
            journeyTitle = "Live Split Timer • Focus Session",
            currentStage = "Split 1: Deep Focus (25 Mins)",
            totalProgress = progressValue,
            isRunning = true,
            stage1Color = stage1ColorInt,
            stage2Color = stage2ColorInt,
            stage3Color = stage3ColorInt
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    ExpressiveSplitJourneyNotificationManager.NOTIFICATION_ID,
                    initialNotification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(
                    ExpressiveSplitJourneyNotificationManager.NOTIFICATION_ID,
                    initialNotification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
                )
            }
        } else {
            startForeground(ExpressiveSplitJourneyNotificationManager.NOTIFICATION_ID, initialNotification)
        }

        runJourneyLoop()
    }

    private fun runJourneyLoop() {
        journeyJob?.cancel()
        journeyJob = serviceScope.launch {
            while (isActive && progressValue < 1000) {
                delay(1000)
                progressValue += 25
                if (progressValue > 1000) progressValue = 1000

                val stageText = when {
                    progressValue <= 350 -> "Split 1: Deep Focus Phase (25 Mins)"
                    progressValue <= 700 -> "Split 2: Short Rest Phase (5 Mins)"
                    else -> "Split 3: Cooldown Phase (15 Mins)"
                }

                val titleText = when {
                    progressValue <= 350 -> "Split Focus • ${calculateTimeRemaining(progressValue, 350)} Left"
                    progressValue <= 700 -> "Split Rest • ${calculateTimeRemaining(progressValue, 700)} Left"
                    else -> "Split Cooldown • ${calculateTimeRemaining(progressValue, 1000)} Left"
                }

                SplitJourneyState.updateState(
                    progress = progressValue,
                    stageText = stageText,
                    titleText = titleText,
                    active = true
                )

                manager.updateNotification(
                    titleText,
                    stageText,
                    progressValue,
                    true,
                    stage1ColorInt,
                    stage2ColorInt,
                    stage3ColorInt
                )
            }

            if (progressValue >= 1000) {
                stopJourney()
            }
        }
    }

    private fun calculateTimeRemaining(current: Int, target: Int): String {
        val diff = ((target - current) / 10).coerceAtLeast(0)
        val mins = diff / 60
        val secs = diff % 60
        return String.format("%02d:%02d", mins, secs)
    }

    private fun stopJourney() {
        journeyJob?.cancel()
        manager.cancelNotification()
        SplitJourneyState.reset()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        SplitJourneyState.setRunning(false)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START_JOURNEY = "com.ermohdamaan.justforpixel.ACTION_START_SPLIT_JOURNEY"
        const val ACTION_STOP_JOURNEY = "com.ermohdamaan.justforpixel.ACTION_STOP_SPLIT_JOURNEY"
        const val EXTRA_C1 = "extra_c1"
        const val EXTRA_C2 = "extra_c2"
        const val EXTRA_C3 = "extra_c3"
    }
}

/**
 * State Observer for Android 17 Split Journey Notification UI observing in Compose.
 *
 * @author Er. Mohd Amaan
 */
object SplitJourneyState {
    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    private val _stageText = MutableStateFlow("Split 1: Deep Focus Phase")
    val stageText: StateFlow<String> = _stageText.asStateFlow()

    private val _titleText = MutableStateFlow("Split Focus Timer")
    val titleText: StateFlow<String> = _titleText.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    fun updateState(progress: Int, stageText: String, titleText: String, active: Boolean) {
        _progress.value = progress
        _stageText.value = stageText
        _titleText.value = titleText
        _isActive.value = active
    }

    fun setRunning(running: Boolean) {
        _isActive.value = running
    }

    fun reset() {
        _progress.value = 0
        _stageText.value = "Split 1: Deep Focus Phase"
        _titleText.value = "Split Focus Timer"
        _isActive.value = false
    }
}
