package com.pepito.partypalpro

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pepito.partypalpro.databinding.ActivityCateringBinding
import com.pepito.partypalpro.models.Caterer
import com.pepito.partypalpro.recycleradapter.MenuAdapter

class CateringActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCateringBinding
    val caterers = listOf(
        Caterer(
            "Delicious Delights Catering",
            listOf(
                "Grilled Salmon with Lemon Butter Sauce",
                "Filet Mignon with Mushroom Sauce",
                "Mediterranean Quinoa Salad",
                "Garlic Roasted Potatoes",
                "Chocolate Lava Cake"
            ),
            listOf(25.99, 32.99, 15.99, 10.99, 8.99)
        ),
        Caterer(
            "Gourmet Gatherings",
            listOf(
                "Caprese Salad with Balsamic Glaze",
                "Lemon Herb Roasted Chicken",
                "Vegetable Paella",
                "Garlic Breadsticks",
                "Tiramisu"
            ),
            listOf(12.49, 18.99, 20.99, 6.99, 9.99)
        ),
        Caterer(
            "Savor & Serve Catering Co.",
            listOf(
                "Shrimp Cocktail",
                "Beef Wellington",
                "Greek Salad with Feta Cheese",
                "Twice-Baked Potatoes",
                "New York Cheesecake"
            ),
            listOf(30.99, 40.99, 14.99, 11.49, 7.99)
        ),
        // Add more caterers with detailed menus and prices as needed
    )


    private lateinit var selectedCaterer: Caterer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCateringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        val menuAdapter = MenuAdapter(emptyList(), emptyList()) // Initialize with empty lists
        binding.menuRecyclerView.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(this@CateringActivity)
        }

        // Populate spinner with caterer names
        // Populate spinner with caterer names
        val catererNames = caterers.map { it.name }
        val catererAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, catererNames)
        binding.catererSpinner.adapter = catererAdapter

// Spinner item selected listener
        binding.catererSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCaterer = caterers[position]
                menuAdapter.updateMenu(selectedCaterer.menuItems, selectedCaterer.prices)
                updateOrderSummary()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        // Confirm order button click listener
        binding.confirmOrderButton.setOnClickListener {
            val totalPrice = menuAdapter.getTotalPrice()
            if (totalPrice > 0) {
                // Update order summary with total price
                binding.orderSummaryTextView.text = "Total: $$totalPrice"
            } else {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateOrderSummary() {
        // Reset order summary text
        binding.orderSummaryTextView.text = getString(R.string.order_summary_placeholder)
    }
}
