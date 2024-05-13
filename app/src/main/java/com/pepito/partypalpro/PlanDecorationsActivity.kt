package com.pepito.partypalpro

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PlanDecorationsActivity : AppCompatActivity() {

    private lateinit var spinnerThemes: Spinner
    private lateinit var textViewThemeDescription: TextView
    private lateinit var textViewMaterialsNeeded: TextView
    private lateinit var imageViewTheme: ImageView
    private lateinit var layoutThemeDetails: LinearLayout

    // Define the materials needed for each theme
    private val materialsNeeded = arrayOf(
        "Balloons, Banners, Party Hats, Streamers, Confetti",
        "Flowers, Table Linens, Centerpieces, Candles, Vases",
        "Banners, Diapers, Baby Clothes, Balloons, Stuffed Toys",
        "Balloons, Streamers, Graduation Caps, Photo Booth Props, Cake Toppers",
        "Pumpkins, Fake Spider Webs, Halloween Decor, Costumes, Fog Machine",
        "Ornaments, Lights, Christmas Tree, Wreaths, Stockings",
        "Party Hats, Confetti, Champagne Glasses, Streamers, Photo Booth Props",
        "Candles, Flowers, Dinnerware, Tablecloths, Wine Glasses",
        "Corporate Logo Banners, Name Tags, Presentation Slides, Projector, Stage Setup"
        // Add more materials as needed
    )

    private val themeDescriptions = arrayOf(
        "Celebrate your special day with a vibrant array of balloons, personalized banners, and festive party hats. Create unforgettable memories with friends and family amidst colorful decorations and joyful laughter.",
        "Create a romantic atmosphere with a breathtaking display of fresh flowers, elegant table linens, and flickering candles. Indulge in a night of romance and enchantment as you celebrate your love under the soft glow of candlelight.",
        "Welcome the newest member of your family with an adorable baby shower filled with charming decorations, cute diapers, and cozy baby clothes. Shower the parents-to-be with love and best wishes as they embark on this exciting journey.",
        "Commemorate the milestone of graduation with a lively celebration featuring bold balloons, festive streamers, and spirited photo booth props. Capture the essence of achievement and excitement as you honor the graduate's accomplishments.",
        "Haunt your house with spine-chilling decorations, eerie fake spider webs, and ghoulish costumes for a Halloween bash that will send shivers down your spine. Dare to enter a world of fright and fun as you celebrate the spookiest night of the year.",
        "Deck the halls with twinkling lights, shimmering ornaments, and fragrant evergreen boughs to create a magical Christmas wonderland. Embrace the spirit of the season as you gather with loved ones to share in the joy and warmth of the holidays.",
        "Ring in the new year with a glamorous party filled with sparkling decorations, chic party hats, and bubbly champagne. Toast to new beginnings and endless possibilities as you countdown to midnight in style.",
        "Set the mood for an elegant affair with sophisticated decorations, exquisite dinnerware, and fragrant flowers. Create an atmosphere of refinement and grace as you host an unforgettable evening of fine dining and conversation.",
        "Impress clients and colleagues with a polished corporate event featuring professional setup, branded banners, and sleek presentation slides. Showcase your company's professionalism and expertise as you network and engage with industry leaders."
        // Add more detailed theme descriptions as needed
    )


    private val themeImageUrls = arrayOf(
        "https://deowgxgt4vwfe.cloudfront.net/uploads/81584010288_.jpg",
        "https://th.bing.com/th/id/OIP.xOg_bXAuFtfd-EUhOgDEiQHaE7?w=800&h=533&rs=1&pid=ImgDetMain",
        "https://i.pinimg.com/originals/9c/ff/04/9cff048fd993c63b728ac2e47577737f.jpg",
        "https://i.pinimg.com/originals/ed/8d/d5/ed8dd53b7ef5b75adbe9dfef6e6620c2.jpg",
        "https://th.bing.com/th/id/OIP.L_TlJW6frTPScyN2YohICwHaJ4?w=202&h=269&c=7&r=0&o=5&dpr=1.3&pid=1.7",
        "https://th.bing.com/th/id/R.08f9016f2ec093913947e9587551800b?rik=el9M7HksC1Z62g&riu=http%3a%2f%2fmagment.com%2fwp-content%2fuploads%2f2015%2f10%2fChristmas-Party-Decoration-1.jpg&ehk=lL9x%2bQFAMaOg%2fBdjANmNzgylRoXi3%2bftfZvyagvGMgY%3d&risl=&pid=ImgRaw&r=0",
        "https://th.bing.com/th/id/OIP.-XhYABL0cQS-8c9KyWdKEwAAAA?rs=1&pid=ImgDetMain",
        "https://th.bing.com/th/id/OIP.91J3xngriCdBON30qQZL9QHaE7?rs=1&pid=ImgDetMain",
        "https://i.pinimg.com/736x/13/b3/24/13b32474b79279e1d08b4426012deaa9--corporate-event-design-event-decor.jpg"
        // Add more image URLs as needed
    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_decorations)

        spinnerThemes = findViewById(R.id.spinnerThemes)
        textViewThemeDescription = findViewById(R.id.textViewThemeDescription)
        textViewMaterialsNeeded = findViewById(R.id.textViewMaterialsNeeded)
        imageViewTheme = findViewById(R.id.imageViewTheme)
        layoutThemeDetails = findViewById(R.id.layoutThemeDetails)

        // Populate spinner with theme options
        ArrayAdapter.createFromResource(
            this,
            R.array.themes_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerThemes.adapter = adapter
        }

        // Handle spinner item selection
        spinnerThemes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Show theme details and materials needed
                val selectedTheme = parent?.getItemAtPosition(position).toString()
                textViewThemeDescription.text = themeDescriptions[position] // Populate theme description
                textViewMaterialsNeeded.text = String.format("Materials Needed: \n%s", materialsNeeded[position]) // Populate materials needed based on position

                // Set image based on selected theme
                Glide.with(this@PlanDecorationsActivity)
                    .load(themeImageUrls[position])
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .error(R.drawable.error_image) // Image to display in case of error
                    .into(imageViewTheme)

                layoutThemeDetails.visibility = View.VISIBLE // Show theme details container
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

}
