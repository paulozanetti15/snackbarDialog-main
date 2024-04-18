package com.example.myrecyclerviewapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecyclerviewapplication.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    // Variável para armazenar o último item excluído
    private var lastDeletedItem: City? = null
    private var lastDeletedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        Singleton.setContext(this)
        setupRecyclerView()

        binding.addButton.setOnClickListener {
            Singleton.citySelected = null
            val intent = Intent(this, CityDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.mainRecyclerView.apply {
            adapter = CityAdapter(object : CityAdapter.OnCityClickListener {
                override fun onCityClick(view: View, position: Int) {
                    Singleton.citySelected = Singleton.cities[position]
                    val intent = Intent(this@MainActivity, CityDetailsActivity::class.java)
                    startActivity(intent)
                }

                override fun onCityLongClick(view: View, position: Int) {
                    lastDeletedItem = Singleton.cities[position]
                    lastDeletedPosition = position
                    Singleton.delete(lastDeletedItem!!)
                    adapter?.notifyItemRemoved(position)
                    showUndoSnackbar()
                }
            })
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun showUndoSnackbar() {
        val snackbar = Snackbar.make(binding.root, "Item deleted", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            lastDeletedItem?.let {
                Singleton.add(lastDeletedPosition, it)
                binding.mainRecyclerView.adapter?.notifyItemInserted(lastDeletedPosition)
            }
            lastDeletedItem = null
        }
        snackbar.show()
    }
}