package com.example.madlevel2example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel2example.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val reminders = arrayListOf<Reminder>()
    // Items into the adapter and get used to create a view
    private val reminderAdapter = ReminderAdapter(reminders)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.btnAddReminder.setOnClickListener {
            val reminder = binding.etReminder.text.toString()
            // Add text value to the list
            addReminder(reminder)
        }

        // Adds LinearLayoutManager to recyclerview
        // Keep in mind Views are hierarchical
        binding.rvReminders.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        binding.rvReminders.adapter = reminderAdapter

        // ?
        binding.rvReminders.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        // Adds touchhelper to the recyclerview
        createItemTouchHelper().attachToRecyclerView(rvReminders)
    }

    private fun addReminder(reminder: String) {
        // When its not empty
        if (reminder.isNotBlank()) {
            // Add a reminder item.
            reminders.add(Reminder(reminder))
            // Notify Adapter that the data model has changed so it has to update its view.
            reminderAdapter.notifyDataSetChanged()
            // Reset the input text.
            binding.etReminder.text?.clear()
        } else {
            Snackbar.make(etReminder, "You must fill in the input field!", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     *
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            // Dit is een tegenintuïtieve structuur
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // These are overridden functions it will fill in the recyclerview.
            // Then from the adapterposition the data is removed from the list.
            // Notifies the adapter that the data has been changed and updates accordingly.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // When swipe is complete remove
                reminders.removeAt(position)
                reminderAdapter.notifyDataSetChanged()
            }
        }

        return ItemTouchHelper((callback))
    }
}