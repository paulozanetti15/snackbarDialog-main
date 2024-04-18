package com.example.myrecyclerviewapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myrecyclerviewapplication.databinding.ActivityCityDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CityDetailsActivity : AppCompatActivity() {
    private var changesMade = false // Variável para rastrear se houve alterações nos dados da cidade

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCityDetailsBinding>(
            this,
            R.layout.activity_city_details
        )

        Singleton.citySelected?.apply {
            binding.nameEditText.setText(name)
            binding.populationEditText.setText(population.toString())
            binding.capitalCheckBox.isChecked = isCapital
        }

        binding.saveButton.setOnClickListener {
            saveCityData(binding)
            finish()
        }
    }

    override fun onBackPressed() {
        if (!changesMade) {
            showExitDialog() // Exibir o diálogo apenas se houver alterações nos dados da cidade
        } else {
            super.onBackPressed() // Voltar imediatamente se não houver alterações
        }
    }

    private fun saveCityData(binding: ActivityCityDetailsBinding) {
        val name = binding.nameEditText.text.toString()
        val population = binding.populationEditText.text.toString().toInt()
        val isCapital = binding.capitalCheckBox.isChecked
        val citySelected = Singleton.citySelected

        // Verificar se houve alterações nos dados da cidade
        val changesDetected = citySelected == null || // Se não há cidade selecionada, há alterações
                citySelected.name != name ||
                citySelected.population != population ||
                citySelected.isCapital != isCapital

        if (changesDetected) {
            if (citySelected == null) {
                Singleton.add(City(0, name, population, isCapital))
            } else {
                citySelected.apply {
                    this.name = name
                    this.population = population
                    this.isCapital = isCapital
                    Singleton.update(this)
                }
            }
            changesMade = true // Marcar como true apenas se houver alterações
        }
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Exit without saving?")
            .setMessage("Are you sure you want to exit without saving changes?")
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
    }
}
