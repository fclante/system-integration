package dk.fclante.buddytracker

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dk.fclante.buddytracker.services.azure.EventhubService
import dk.fclante.buddytracker.services.azure.ReadEventhubService
import dk.fclante.buddytracker.services.location.LocationService
import dk.fclante.buddytracker.ui.layouts.MainLayout
import dk.fclante.buddytracker.ui.theme.BuddytrackerTheme

class MainActivity : ComponentActivity() {

    private lateinit var locationService: LocationService
    private lateinit var eventhubService: EventhubService
    private lateinit var readEventhubService: ReadEventhubService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationService = LocationService(this)
        eventhubService = EventhubService()
        readEventhubService = ReadEventhubService()

        setContent {
            BuddytrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainLayout(
                        modifier = Modifier.padding(innerPadding),
                        locationService = locationService,
                        eventhubService = eventhubService,
                        readEventhubService = readEventhubService
                    )
                }
            }
        }

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    Log.d("Permission", "Permission granted")
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Log.d("Permission", "Permission denied")
                }
            }

        val requestMultiplePermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.forEach { (permission, isGranted) ->
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your app.
                        Log.d("Permission", "$permission granted")
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        Log.d("Permission", "$permission denied")
                        requestPermissionLauncher.launch(permission)
                    }
                }
            }


        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE
        )
        requestMultiplePermissionsLauncher.launch(permissions)
    }

}

