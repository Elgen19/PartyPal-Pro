package com.pepito.partypalpro

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pepito.partypalpro.databinding.ActivityMainBinding
import com.pepito.partypalpro.recycleradapter.GuestAdapter
import com.pepito.partypalpro.storage.AppDatabase
import com.pepito.partypalpro.storage.GuestDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var guestDao: GuestDao
    private lateinit var guestAdapter: GuestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Room database
        val database = AppDatabase.getInstance(this)
        guestDao = database.guestDao()

        // Set up RecyclerView
        guestAdapter = GuestAdapter(this, emptyList())

        binding.guestRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.guestRecyclerView.adapter = guestAdapter

        // Set up BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.guest -> {
                    // Handle the guest item click (if needed)
                    true
                }
                R.id.task -> {
                    // Redirect to ToDoListActivity when the "Task" item is clicked
                    startActivity(Intent(this, ToDoListActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Load data from the Room database using Coroutines
        loadDataFromDatabase()
    }

    override fun onResume() {
        super.onResume()
        loadDataFromDatabase()
    }

    private fun loadDataFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val guests = guestDao.getAllGuests()

            // Update the adapter with the new data
            guestAdapter.setData(guests)

            // Update the visibility of textViewNoGuest based on the data size
            binding.textViewNoGuest.visibility = if (guests.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItemAddGuest -> {
                startActivity(Intent(this, AddAGuestActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
