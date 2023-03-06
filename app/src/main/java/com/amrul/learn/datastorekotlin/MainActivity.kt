package com.amrul.learn.datastorekotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_pref")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveDataOnclick()
        loadDataOnclick()
    }

    private fun saveDataOnclick() {
        val etName = findViewById<EditText>(R.id.et_name)
        val btnSave = findViewById<Button>(R.id.btn_save_text)

        btnSave.setOnClickListener { saveData(valueToSave = etName.text.toString()) }
    }

    private fun saveData(valueToSave: String) = lifecycleScope.launch {
        val prefsKey = stringPreferencesKey("name_key")
        dataStore.edit { preferences ->
            preferences[prefsKey] = valueToSave
        }
    }

    private fun loadDataOnclick() {
        val tvName = findViewById<TextView>(R.id.tv_load_text)
        val btnLoad = findViewById<Button>(R.id.btn_load_text)
        btnLoad.setOnClickListener {
            lifecycleScope.launch {
                tvName.text = loadText().toString()
            }
        }
    }

    private suspend fun loadText(): String? {
        val prefsKey = stringPreferencesKey("name_key")
        val prefs = dataStore.data.first()
        return prefs[prefsKey]
    }
}