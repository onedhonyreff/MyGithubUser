package com.fikri.submissiongithubuserbfaa.activity

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.databinding.ActivitySettingsBinding
import com.fikri.submissiongithubuserbfaa.other_class.SettingPreferences
import com.fikri.submissiongithubuserbfaa.view_model.SettingsFactory
import com.fikri.submissiongithubuserbfaa.view_model.SettingsViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.settings_actionbar_title)

        val pref = SettingPreferences.getInstance(dataStore)

        val viewModel = ViewModelProvider(this, SettingsFactory(pref)).get(
            SettingsViewModel::class.java
        )

        viewModel.getThemeSettings().observe(this, { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        })

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }
}