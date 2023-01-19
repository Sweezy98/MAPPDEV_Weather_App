package at.fh.mappdev.sweather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class LocationActivity : AppCompatActivity() {
    companion object {
        const val LAT = "LAT"
        const val LON = "LON"
        const val LOCATION_NAME = "LOCATION_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)


        Places.initialize(applicationContext, "AIzaSyAVn85NCkPwc9-k06cNj0U2b1-lZDCdkfs");

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                //Set sharedPreferences
                val locationName = place.name + ", " + place.addressComponents.asList()[3].shortName
                val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                sharedPreferences.edit().putFloat(LAT, place.latLng.latitude.toFloat()).apply()
                sharedPreferences.edit().putFloat(LON, place.latLng.longitude.toFloat()).apply()
                sharedPreferences.edit().putString(LOCATION_NAME, locationName).apply()
                finish()
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(this.toString(), "An error occurred: $status")
            }
        })
    }
}