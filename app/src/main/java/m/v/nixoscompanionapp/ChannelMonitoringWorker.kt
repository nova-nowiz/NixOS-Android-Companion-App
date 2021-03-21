package m.v.nixoscompanionapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class ChannelMonitoringWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    val notification_id = 1

    override fun doWork(): Result {
        val notificationEnabled = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("notifications", false)

        if (notificationEnabled) {
            val channel_id = inputData.getString("channel_id") ?: return Result.failure()
            makeVolleyRequest(channel_id)
        }

        return Result.success()
    }

    private fun makeVolleyRequest(channel_id: String) {
        VolleySingleton
            .getInstance(applicationContext)
            .addToRequestQueue(
                JsonObjectRequest(
                    "https://monitoring.nixos.org/prometheus/api/v1/query?query=channel_revision",
                    null,
                    { response: JSONObject ->
                        val result: JSONArray = response
                            .getJSONObject("data")
                            .getJSONArray("result")

                        val channels: MutableList<Pair<String, String>> = ArrayList()

                        for (i in 0 until result.length()) {
                            val channel = result
                                .getJSONObject(i)
                                .getJSONObject("metric")
                            val channelName = channel.getString("channel")
                            val channelRevision = channel.getString("revision")
                            channels += Pair(channelName, channelRevision)
                        }

                        val notificationText: String =
                            channels
                                .joinToString(
                                    separator = "\n",
                                    transform = {
                                        it.first + ": " + it.second.subSequence(0, 12)
                                    }
                                )

                        val notification = NotificationCompat
                            .Builder(applicationContext, channel_id)
                            .setSmallIcon(R.drawable.nixos_logo)
                            .setContentTitle("NixOS channel revisions")
                            .setContentText(notificationText)
                            .setStyle(
                                NotificationCompat
                                    .BigTextStyle()
                                    .bigText(notificationText)
                            )
                            .setPriority(NotificationCompat.PRIORITY_LOW)
                            .setContentIntent(
                                PendingIntent
                                    .getActivity(
                                        applicationContext,
                                        0,
                                        Intent(
                                            applicationContext,
                                            MainActivity::class.java
                                        ),
                                        0
                                    )
                            )
                            .setAutoCancel(true)
                            .build()

                        NotificationManagerCompat
                            .from(applicationContext)
                            .notify(notification_id, notification)
                    },
                    null
                )
            )
    }
}