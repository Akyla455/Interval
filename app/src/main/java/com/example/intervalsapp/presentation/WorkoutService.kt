package com.example.intervalsapp.presentation


import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.intervalsapp.MainActivity
import com.example.intervalsapp.R
import com.example.intervalsapp.domain.events.WorkoutObserver
import com.example.intervalsapp.domain.workout.WorkoutManager
import com.example.intervalsapp.domain.model.IntervalTimer
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutService : Service() {

    @Inject lateinit var manager: WorkoutManager
    @Inject lateinit var workoutObserver: WorkoutObserver
    @Inject lateinit var locationClient: FusedLocationProviderClient

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val cb = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach {
                manager.appendLocation(it.latitude, it.longitude)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> {
                val timer = intent.getParcelableExtra<IntervalTimer>("timer")
                    ?: run { stopSelf(); return START_NOT_STICKY }

                createNotificationChannel()

                // 1. Запускаем пустую нотификацию, чтобы сервис не убили
                val initialNotification = buildNotification("Подготовка...", "")
                startForegroundCompat(1, initialNotification)

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startLocation()
                }
                manager.start(timer)
                workoutObserver.observeWorkout()
                    .onEach { update ->
                        val timeStr = formatTime(update.elapsedSec)
                        val intervalName = if (update.currentInterval in timer.intervals.indices) {
                            timer.intervals[update.currentInterval].title
                        } else {
                            "Финиш"
                        }
                        updateNotification(timeStr, intervalName)
                    }
                    .launchIn(serviceScope)
            }
            "STOP" -> {
                stopLocation()
                manager.stop()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            val ch = NotificationChannel("workout_channel", "Workout", NotificationManager.IMPORTANCE_LOW)
            nm.createNotificationChannel(ch)
        }
    }

    private fun startForegroundCompat(id: Int, notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                startForeground(id, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
            } catch (e: Exception) {
                startForeground(id, notification)
            }
        } else {
            startForeground(id, notification)
        }
    }

    private fun buildNotification(title: String, content: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, "workout_channel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_run)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    private fun updateNotification(title: String, content: String) {
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(1, buildNotification(title, content))
    }

    private fun startLocation() {
        val req = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1500L).build()
        try {
            locationClient.requestLocationUpdates(req, cb, Looper.getMainLooper())
        } catch (e: SecurityException) { }
    }

    private fun stopLocation() {
        try { locationClient.removeLocationUpdates(cb) } catch (_: Exception) {}
    }

    private fun formatTime(seconds: Long): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }

    override fun onDestroy() {
        manager.stop()
        stopLocation()
        serviceScope.cancel()
        super.onDestroy()
    }
}
