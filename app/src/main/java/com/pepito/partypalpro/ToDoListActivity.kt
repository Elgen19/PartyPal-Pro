package com.pepito.partypalpro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pepito.partypalpro.databinding.ActivityToDoListBinding
import com.pepito.partypalpro.recycleradapter.TaskAdapter
import com.pepito.partypalpro.storage.AppDatabase
import com.pepito.partypalpro.storage.Task
import com.pepito.partypalpro.storage.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityToDoListBinding
    private lateinit var taskDao: TaskDao
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Room database
        val database = AppDatabase.getInstance(this)
        taskDao = database.taskDao()

        // Set up RecyclerView.
        taskAdapter = TaskAdapter(emptyList(), this::openTaskDetailActivity, taskDao)

        binding.guestRecyclerView.adapter = taskAdapter
        binding.guestRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up click listener for the "Add Task" button
        binding.buttonAddTask.setOnClickListener {
            addTask()
        }

        // Set up BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.guest -> {
                    // Redirect to MainActivity when the "Guest" item is clicked
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.task -> {
                    // Handle necessary actions

                    true
                }
                else -> false
            }
        }

        // Explicitly set the selected item to "Task"
        binding.bottomNavigationView.selectedItemId = R.id.task

        // Load existing tasks from the Room database
        loadTasks()
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        CoroutineScope(Dispatchers.Main).launch {
            // Retrieve tasks directly without observing changes
            val tasks = taskDao.getAllTasksDirect()

            // Log the task statuses
            tasks.forEach { task ->
                Log.d("TaskAdapter", "Task status from database: ${task.taskStatus}")
            }

            // Update the adapter with the retrieved tasks
            taskAdapter.setData(tasks)

            // Update the visibility of textViewEmptyList based on the data size
            binding.textViewEmptyList.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }
    }




    private fun addTask() {
        val taskName = binding.editTextTaskName.text.toString()
        val taskDescription = binding.editTextTask.text.toString()

        if (taskName.isNotBlank()) {
            val newTask = Task(taskName = taskName, taskDescription = taskDescription, taskStatus = "On going")
            CoroutineScope(Dispatchers.IO).launch {
                // Add the new task to the Room database
                taskDao.addTask(newTask)

                // Fetch tasks again from the database after adding a new task
                loadTasks()
            }

            // Clear the input fields after adding a task
            binding.editTextTaskName.text.clear()
            binding.editTextTask.text.clear()
        }
    }

    private fun openTaskDetailActivity(task: Task) {
        // Start TaskDetailActivity with the selected task's details
        val intent = Intent(this, TaskDetailActivity::class.java)
        intent.putExtra("taskId", task.id) // Pass the task's ID to TaskDetailActivity
        startActivity(intent)
    }

}
