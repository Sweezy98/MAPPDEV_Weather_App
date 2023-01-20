package at.fh.mappdev.sweather

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse


class LocationActivity : AppCompatActivity() {
    companion object {
        const val LAT = "LAT"
        const val LON = "LON"
        const val LOCATION_NAME = "LOCATION_NAME"
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // update the list of predictions
            getPlacePrediction(s.toString())
            //for (prediction in predictions) {
            //    Log.d("LocationActivity", prediction.name)
            //}
        }
    }

    val predictionAdapter = LocationPredictionAdapter() {
        // get place details from Places with location id (it)
        val places = Places.createClient(this)
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS)
        places.fetchPlace(FetchPlaceRequest.newInstance(it, placeFields))
            .addOnSuccessListener { response ->
                val place = response.place
                val lat = place.latLng?.latitude
                val lon = place.latLng?.longitude
                val name = place.name + ", " + place.addressComponents.asList()[3].shortName
                //Set sharedPreferences
                val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                sharedPreferences.edit().putFloat(LAT, place.latLng.latitude.toFloat()).apply()
                sharedPreferences.edit().putFloat(LON, place.latLng.longitude.toFloat()).apply()
                sharedPreferences.edit().putString(LOCATION_NAME, name).apply()
                finish()
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("LocationActivity", "Place not found: ${exception.statusCode}")
                }
            }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        Places.initialize(applicationContext, "AIzaSyAVn85NCkPwc9-k06cNj0U2b1-lZDCdkfs")

        findViewById<EditText>(R.id.editLocationName).addTextChangedListener(textWatcher)

        val recyclerView = findViewById<RecyclerView>(R.id.predictionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = predictionAdapter

        /*Places.initialize(applicationContext, "AIzaSyAVn85NCkPwc9-k06cNj0U2b1-lZDCdkfs")

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
        })*/

    }

    // prediction object with name, country and id
    data class Prediction(val name: String, val country: String, val id: String)

    private fun getPlacePrediction(query: String) {

        // Create a new token for the autocomplete session.
        val token = AutocompleteSessionToken.newInstance()

        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.CITIES)
                .setSessionToken(token)
                .setQuery(query)
                .build()

        val placesClient = Places.createClient(this)

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                // map predictions to list of prediction objects
                // primary text as name
                // secondary text as country
                // place id as id
                val predictionsList = response.autocompletePredictions.map {
                    val name = it.getPrimaryText(null).toString()
                    val country = it.getSecondaryText(null).toString()
                    val id = it.placeId
                    Prediction(name, country, id)
                }
                predictionAdapter.updateList(predictionsList)
            }
            .addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e("LocationActivity", "Place not found: " + exception.statusCode)
                }
            }

    }
}