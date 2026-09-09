package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * Android 16 / 17 Multi-Segment Split Timer & Journey Notification Manager.
 *
 * =========================================================================================================
 * 🛠️ HOW LIVE NOTIFICATION SUCCESS WAS ACHIEVED & STEPS FOLLOWED:
 * =========================================================================================================
 * 1. MANIFEST REGISTRATION:
 *    - Service `ExpressiveSplitJourneyNotificationService` is declared in `AndroidManifest.xml` under `<application>` tag
 *      with `android:foregroundServiceType="specialUse"` and property `android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE`.
 *
 * 2. HIGH IMPORTANCE CHANNEL SETUP:
 *    - `NotificationChannel` registered with `NotificationManager.IMPORTANCE_HIGH` (API 26+) for status bar chip promotion.
 *
 * 3. CATEGORY_STOPWATCH & PROMOTED EXTRA FLAGS:
 *    - `setCategory(Notification.CATEGORY_STOPWATCH)` instructs Android System UI to immediately promote this notification as a status bar chip.
 *    - `extras.putBoolean("android.requestPromotedOngoing", true)` requests Android 16+ status bar chip promotion.
 *    - `setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)` bypasses the 10-second notification display delay.
 *
 * 4. NATIVE `Notification.ProgressStyle` REFLECTION (API 35+):
 *    - Instead of setting `mStyle` on `NotificationCompat.Builder` (which gets overridden in `build()`), we instantiate
 *      native `android.app.Notification.Builder` and invoke `setStyle(progressStyleObj)` directly on API 35+.
 *
 * =========================================================================================================
 * ⚠️ CRITICAL PITFALLS TO AVOID:
 * =========================================================================================================
 * ❌ PITFALL 1: Forgetting to register `<service>` in `AndroidManifest.xml`.
 * ❌ PITFALL 2: Applying reflection on `NotificationCompat.Builder`'s private `mStyle` field.
 * ❌ PITFALL 3: Missing `foregroundServiceType` attribute on Android 14+ (API 34+).
 * ❌ PITFALL 4: Missing `POST_NOTIFICATIONS` runtime permission on Android 13+ (API 33+).
 * =========================================================================================================
 *
 * @param context Android application context
 * @author Er. Mohd Amaan
 */
class ExpressiveSplitJourneyNotificationManager(private val context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannel()
    }

    /**
     * Initializes the High Importance Notification Channel required for Status Bar Chips.
     * Must be created before posting any notifications.
     */
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Android 17 Multi-Segment Split Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Multi-segment colorized progress bar notifications for ExpressiveLab"
                setShowBadge(true)
                setSound(null, null)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds an Android 16/17 Multi-Segment Split Notification with guaranteed [NotificationCompat.CATEGORY_STOPWATCH] category
     * for immediate posting and status bar chip promotion.
     *
     * @param journeyTitle Title of the split session (e.g. "Focus Split Session • 24:50")
     * @param currentStage Subtitle status (e.g. "Split 1: Deep Focus • 80% Left")
     * @param totalProgress Integer progress value out of 1000
     * @param isRunning Active state
     * @param stage1Color Color integer for Stage 1 segment (Focus)
     * @param stage2Color Color integer for Stage 2 segment (Rest)
     * @param stage3Color Color integer for Stage 3 segment (Cooldown)
     * @return Built [Notification]
     */
    fun buildSplitJourneyNotification(
        journeyTitle: String,
        currentStage: String,
        totalProgress: Int,
        isRunning: Boolean = true,
        stage1Color: Int = Color.GREEN,
        stage2Color: Int = Color.YELLOW,
        stage3Color: Int = Color.CYAN
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

        val stopIntent = Intent(context, ExpressiveSplitJourneyNotificationService::class.java).apply {
            action = ExpressiveSplitJourneyNotificationService.ACTION_STOP_JOURNEY
        }
        val stopPendingIntent = PendingIntent.getService(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val smallIcon = context.applicationInfo.icon

        // Apply Android 16 ProgressStyle API if supported on device (API 35+)
        if (Build.VERSION.SDK_INT >= 35) {
            try {
                val progressStyleClass = Class.forName("android.app.Notification\$ProgressStyle")
                val segmentClass = Class.forName("android.app.Notification\$ProgressStyle\$Segment")

                val progressStyleObj = progressStyleClass.getConstructor().newInstance()

                val setTrackerIconMethod = progressStyleClass.getMethod("setProgressTrackerIcon", Icon::class.java)
                setTrackerIconMethod.invoke(progressStyleObj, Icon.createWithResource(context, smallIcon))

                val setProgressMethod = progressStyleClass.getMethod("setProgress", Int::class.javaPrimitiveType)
                setProgressMethod.invoke(progressStyleObj, totalProgress)

                // Define 3 Split Colored Segments (Focus, Rest, Cooldown)
                val segment1Constructor = segmentClass.getConstructor(Int::class.javaPrimitiveType)
                val seg1 = segment1Constructor.newInstance(350)
                val setSegColorMethod = segmentClass.getMethod("setColor", Int::class.javaPrimitiveType)
                setSegColorMethod.invoke(seg1, stage1Color)

                val seg2 = segment1Constructor.newInstance(350)
                setSegColorMethod.invoke(seg2, stage2Color)

                val seg3 = segment1Constructor.newInstance(300)
                setSegColorMethod.invoke(seg3, stage3Color)

                val setSegmentsMethod = progressStyleClass.getMethod("setProgressSegments", List::class.java)
                setSegmentsMethod.invoke(progressStyleObj, listOf(seg1, seg2, seg3))

                // Build with Native Notification.Builder on API 35+ to properly invoke setStyle(progressStyleObj)
                val nativeBuilder = Notification.Builder(context, CHANNEL_ID)
                    .setContentTitle(journeyTitle)
                    .setContentText(currentStage)
                    .setSubText("Android 16 Live Activity")
                    .setSmallIcon(smallIcon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setCategory(Notification.CATEGORY_STOPWATCH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .addAction(
                        Notification.Action.Builder(
                            Icon.createWithResource(context, R.drawable.ic_menu_close_clear_cancel),
                            "End Split Session",
                            stopPendingIntent
                        ).build()
                    )

                val setStyleMethod = Notification.Builder::class.java.getMethod("setStyle", Class.forName("android.app.Notification\$Style"))
                setStyleMethod.invoke(nativeBuilder, progressStyleObj)

                nativeBuilder.extras.putBoolean("android.requestPromotedOngoing", true)
                nativeBuilder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)

                return nativeBuilder.build()
            } catch (_: Exception) {
                // Fallback gracefully to NotificationCompat.Builder if reflection fails
            }
        }

        // Standard NotificationCompat Fallback for API < 35
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(journeyTitle)
            .setContentText(currentStage)
            .setSubText("Android 16 Live Activity")
            .setSmallIcon(smallIcon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            // Use CATEGORY_STOPWATCH so Android immediately recognizes and promotes it as a status bar chip!
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setProgress(1000, totalProgress, false)
            .addAction(
                R.drawable.ic_menu_close_clear_cancel,
                "End Split Session",
                stopPendingIntent
            )

        // Request Android 16 / 17 Promoted Live Status Bar Chip Extra
        builder.extras.putBoolean("android.requestPromotedOngoing", true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        }

        return builder.build()
    }

    fun updateNotification(title: String, stage: String, progress: Int, isRunning: Boolean, c1: Int, c2: Int, c3: Int) {
        val notification = buildSplitJourneyNotification(title, stage, progress, isRunning, c1, c2, c3)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    companion object {
        const val CHANNEL_ID = "expressive_split_journey_channel"
        const val NOTIFICATION_ID = 1002
    }
}
