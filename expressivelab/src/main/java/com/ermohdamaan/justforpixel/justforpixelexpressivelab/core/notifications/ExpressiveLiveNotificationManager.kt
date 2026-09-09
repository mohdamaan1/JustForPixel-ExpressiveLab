package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * Manager class responsible for Android 16 Live Activity Promoted Ongoing Notifications
 * & Status Bar Chip Notifications.
 *
 * Configures notification channel with [NotificationManager.IMPORTANCE_HIGH] and applies
 * Android 16 [NotificationCompat.Builder.setRequestPromotedOngoing] flags and
 * [NotificationCompat.CATEGORY_STOPWATCH] for dynamic status bar chips.
 *
 * @param context Android application context
 * @author Er. Mohd Amaan
 */
class ExpressiveLiveNotificationManager(private val context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    /**
     * Initializes the Live Notification Channel required for Android 8.0+ (API 26+).
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                setShowBadge(true)
                setSound(null, null)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds an ongoing, live-updating Android 16 Promoted Notification for Foreground Service.
     *
     * @param timerString Formatted time readout (e.g. "04:59")
     * @param isRunning Active timer state
     * @param progressPercent Integer progress value (0 to 100)
     * @return Built [Notification] instance
     */
    fun buildLiveNotification(
        timerString: String,
        isRunning: Boolean,
        progressPercent: Int
    ): Notification {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Stop Action Intent
        val stopIntent = Intent(context, ExpressiveLiveNotificationService::class.java).apply {
            action = ExpressiveLiveNotificationService.ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Pause/Resume Toggle Intent
        val toggleIntent = Intent(context, ExpressiveLiveNotificationService::class.java).apply {
            action = if (isRunning) ExpressiveLiveNotificationService.ACTION_PAUSE else ExpressiveLiveNotificationService.ACTION_RESUME
        }
        val togglePendingIntent = PendingIntent.getService(
            context,
            2,
            toggleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val smallIcon = context.applicationInfo.icon

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Expressive Live Activity: $timerString")
            .setContentText(if (isRunning) "Active Session • $progressPercent% Complete" else "Session Paused • Tap to Resume")
            .setSmallIcon(smallIcon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setProgress(100, progressPercent, false)
            .addAction(
                if (isRunning) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (isRunning) "Pause" else "Resume",
                togglePendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Stop",
                stopPendingIntent
            )

        // Android 16 Live Activity Promoted Ongoing Notification Extra Flag
        builder.extras.putBoolean("android.requestPromotedOngoing", true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        }

        return builder.build()
    }

    /**
     * Updates the existing active notification with new progress data.
     */
    fun updateNotification(timerString: String, isRunning: Boolean, progressPercent: Int) {
        val notification = buildLiveNotification(timerString, isRunning, progressPercent)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Cancels the active live notification.
     */
    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    companion object {
        const val CHANNEL_ID = "expressive_live_notifications"
        const val CHANNEL_NAME = "Live Activity Sessions"
        const val CHANNEL_DESCRIPTION = "Android 16 Live status bar chip & ongoing notifications for ExpressiveLab"
        const val NOTIFICATION_ID = 1001
    }
}
