package dk.fclante.buddytracker.services.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat

open class LocationService(private val context: Context?) {
    private val locationManager: LocationManager? =
        context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager


    private var coarseLocationListener: CoarseLocationListener? = null
    private var fineLocationListener: FineLocationListener? = null

    class CoarseLocationListener(private val callback: (Location) -> Unit) :
        LocationListener {
        override fun onLocationChanged(location: Location) {
            callback(location)
        }

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }

    class FineLocationListener(private val callback: (Location) -> Unit) :
        LocationListener {
        override fun onLocationChanged(location: Location) {
            callback(location)
        }

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }


    open fun startCoarseLocationUpdates(callback: (Location) -> Unit) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            this.coarseLocationListener = CoarseLocationListener(callback)
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                this.coarseLocationListener!!
            )
        } else {
            // Permission is not granted, request the permission
            // This is just a placeholder, you should handle this part according to your app's flow
            println("Permission not granted")
        }
    }

    open fun startFineLocationUpdates(callback: (Location) -> Unit) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            this.fineLocationListener = FineLocationListener(callback)
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                this.fineLocationListener!!
            )
        } else {
            // Permission is not granted, request the permission
            // This is just a placeholder, you should handle this part according to your app's flow
            println("Permission not granted")
        }
    }

    open fun stopLocationUpdates() {
        coarseLocationListener?.let { locationManager?.removeUpdates(it) }
        fineLocationListener?.let { locationManager?.removeUpdates(it) }
    }
}
