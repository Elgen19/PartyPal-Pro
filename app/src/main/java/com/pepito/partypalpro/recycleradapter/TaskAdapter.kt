package com.pepito.partypalpro.recycleradapter

// TaskAdapter.kt

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pepito.partypalpro.R
import com.pepito.partypalpro.databinding.ItemTaskBinding
import com.pepito.partypalpro.storage.Task
import com.pepito.partypalpro.storage.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskAdapter(
    private var taskList: List<Task>,
    private val onItemClick: (Task) -> Unit,
    private val taskDao: TaskDao // Pass the TaskDao as a parameter
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemTaskBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]

        holder.binding.textViewTaskName.text = currentTask.taskName
        holder.binding.checkBoxTask.isChecked = currentTask.taskStatus == "Completed"

        // Update badgeTaskStatus TextView based on taskStatus
        holder.binding.badgeTaskStatus.text = currentTask.taskStatus
        holder.binding.badgeTaskStatus.setBackgroundResource(
            if (currentTask.taskStatus == "Completed") R.color.colorBadgeCompleted
            else R.color.colorBadgeOngoing
        )

        holder.binding.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Completed" else "On going"

            // Only update if the status has changed
            if (currentTask.taskStatus != status) {
                holder.binding.badgeTaskStatus.text = status
                holder.binding.badgeTaskStatus.setBackgroundResource(
                    if (isChecked) R.color.colorBadgeCompleted else R.color.colorBadgeOngoing
                )

                // Update the status in the Task entity and call the DAO method to persist the change
                currentTask.taskStatus = status
                CoroutineScope(Dispatchers.IO).launch {
                    taskDao.updateTask(currentTask)
                }
            }
        }

        // Set click listener for the item view
        holder.itemView.setOnClickListener {
            onItemClick(currentTask)
        }
    }




    override fun getItemCount() = taskList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Task>) {
        taskList = newList
        notifyDataSetChanged()
    }


}

