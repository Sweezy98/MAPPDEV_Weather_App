package at.fh.mappdev.sweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlin.random.Random

private val imageMap = mapOf(
    R.drawable.image1 to "image1",
    R.drawable.image2 to "image2",
    R.drawable.image3 to "image3",
    R.drawable.image4 to "image4",
    R.drawable.image5 to "image5",
    R.drawable.image6 to "image6",
    R.drawable.image7 to "image7",
    R.drawable.image8 to "image8"
)

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

    // Use a LinearLayout to hold the CardViews
    val container = findViewById<LinearLayout>(R.id.container)

    // Create a list of the resource IDs of the 4 randomly selected images
    val selectedImages = selectRandomImages()

    // Iterate through the selected images and create a CardView for each one
    for (resourceId in selectedImages) {
        val cardView = layoutInflater.inflate(R.layout.cardview, container, false) as CardView
        val imageView = cardView.findViewById<ImageView>(R.id.imageView)
        val textView = cardView.findViewById<TextView>(R.id.textView)

        // Set the image and text on the CardView
        imageView.setImageResource(resourceId)
        textView.text = imageMap[resourceId]

        // Add the CardView to the container
        container.addView(cardView)
    }
}

private fun selectRandomImages(): List<Int> {
    // Create a list of the resource IDs of all images
    val allImages = imageMap.keys.toList()

    // Use a random number generator to select 4 images
    val random = Random(System.currentTimeMillis())
    val selectedImages = mutableListOf<Int>()
    for (i in 0..3) {
        val index = random.nextInt(allImages.size)
        selectedImages.add(allImages[index])
    }
    return selectedImages
    }
}

