package m.v.nixoscompanionapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.work.*
import m.v.nixoscompanionapp.ui.preferences.PreferencesActivity
import m.v.nixoscompanionapp.ui.preferences.PreferencesFragment
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val channel_id = "1AvFtsBX4mTq8vnP6yLeH93qO7B4zVvyx3lwI1f6lvHVUpKRAccNfaJvTvY3FOOj"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_package_search, R.id.nav_option_search), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        createNotificationChannel()
        launchChannelMonitoringWorker()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            //startActivity(Intent(applicationContext, PreferencesActivity::class.java))
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.nav_preferences)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channel_id, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun launchChannelMonitoringWorker() {
        val channelMonitoringWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<ChannelMonitoringWorker>(2, TimeUnit.HOURS)
                .setInputData(workDataOf(
                    "channel_id" to channel_id
                ))
                .setConstraints(
                    Constraints
                        .Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueue(channelMonitoringWorkRequest)
    }
}
