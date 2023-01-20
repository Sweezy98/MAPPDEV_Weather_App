package at.fh.mappdev.sweather

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class LocationActivity : AppCompatActivity(), CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        const val LAT = "LAT"
        const val LON = "LON"
        const val LOCATION_NAME = "LOCATION_NAME"
        const val FAVORITE_ID = "FAVORITE_ID"
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // update the list of predictions
            getPlacePrediction(s.toString())
        }
    }

    private val predictionAdapter = LocationPredictionAdapter() {
        // get place details from Places with location id (it)
        val places = Places.createClient(this)
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS)
        places.fetchPlace(FetchPlaceRequest.newInstance(it, placeFields))
            .addOnSuccessListener { response ->
                val place = response.place
                val lat = place.latLng?.latitude
                val lon = place.latLng?.longitude
                val name = place.name + ", " + place.addressComponents.asList()[3].shortName

                // check if location is already in favorites list
                val favId = favouriteAdapter.isFavourite(name, lat!!, lon!!)

                //Set sharedPreferences
                val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                sharedPreferences.edit().putFloat(LAT, place.latLng.latitude.toFloat()).apply()
                sharedPreferences.edit().putFloat(LON, place.latLng.longitude.toFloat()).apply()
                sharedPreferences.edit().putString(FAVORITE_ID, favId).apply()
                sharedPreferences.edit().putString(LOCATION_NAME, name).apply()
                finish()
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("LocationActivity", "Place not found: ${exception.statusCode}")
                }
            }
    }

    private val favouriteAdapter = FavouriteAdapter( {
        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putFloat(LAT, it!!.lat!!.toFloat()).apply()
        sharedPreferences.edit().putFloat(LON, it!!.lon!!.toFloat()).apply()
        sharedPreferences.edit().putString(FAVORITE_ID, it!!._id!!).apply()
        sharedPreferences.edit().putString(LOCATION_NAME, it.name).apply()
        finish()
    },
        {
            val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
            val name: String = sharedPreferences.getString(LOCATION_NAME, null).toString()

            // check if location to delete is the current location
            if (name == it!!.name!!) {
                sharedPreferences.edit().putString(FAVORITE_ID, null).apply()
            }

            removeFavourite(it!!._id!!)
            launch {
                val result = apolloClient(applicationContext).mutation(RemoveFavouriteMutation(it!!._id!!)).execute()
            }
        })

    private fun removeFavourite(id: String) {
        favouriteAdapter.removeFavourite(id)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        Places.initialize(applicationContext, "AIzaSyAVn85NCkPwc9-k06cNj0U2b1-lZDCdkfs")

        findViewById<EditText>(R.id.editLocationName).addTextChangedListener(textWatcher)

        val predictionsRecyclerView = findViewById<RecyclerView>(R.id.predictionsRecyclerView)
        predictionsRecyclerView.layoutManager = LinearLayoutManager(this)
        predictionsRecyclerView.adapter = predictionAdapter

        val favouritesRecyclerView = findViewById<RecyclerView>(R.id.favouritesRecyclerView)
        favouritesRecyclerView.layoutManager = LinearLayoutManager(this)
        favouritesRecyclerView.adapter = favouriteAdapter




        findViewById<EditText>(R.id.editLocationName).setOnFocusChangeListener() { _, hasFocus ->
            if (hasFocus) {
                findViewById<MaterialCardView>(R.id.locationSearchbar).strokeWidth = 4
                findViewById<MaterialCardView>(R.id.locationSearchbar).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
                findViewById<MaterialCardView>(R.id.locationPredictions).strokeWidth = 4
                findViewById<MaterialCardView>(R.id.locationPredictions).outlineAmbientShadowColor = getColorWithAlpha(R.color.blue, 0.5f)
                findViewById<MaterialCardView>(R.id.locationPredictions).visibility = View.VISIBLE
            } else {
                findViewById<MaterialCardView>(R.id.locationSearchbar).strokeWidth = 0
                findViewById<MaterialCardView>(R.id.locationSearchbar).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
                findViewById<MaterialCardView>(R.id.locationPredictions).strokeWidth = 0
                findViewById<MaterialCardView>(R.id.locationPredictions).outlineAmbientShadowColor = ResourcesCompat.getColor(resources, R.color.defaultOutlineAmbientShadowColor, null)
                findViewById<MaterialCardView>(R.id.locationPredictions).visibility = View.GONE
            }
        }

        launch {
            val favouriteResult = apolloClient(applicationContext).query(UserFavouritesQuery()).execute()
            val favourites = favouriteResult?.data?.userFavourites
            if (favourites != null) {
                favouriteAdapter.updateList(favourites)
            }
        }
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

    private fun getColorWithAlpha(color: Int, alpha: Float): Int {
        val baseColor = ResourcesCompat.getColor(resources, color, null)
        val alphaInt = (alpha * 255).toInt()
        return Color.argb(alphaInt, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))
    }
}