package com.mobileapp.battleship

import android.content.Context
import androidx.lifecycle.ViewModel
import java.io.File

class StatsViewModel : ViewModel() {
    private val fileName = "stats.txt"

    // get the contents from the file as a string, other wise if it doesnt exist, return empty string
    fun readFromFile(context: Context): String {
        val file = File(context.filesDir, fileName)
        if(!file.exists()) {
            return ""
        }

        return context.openFileInput(fileName).bufferedReader().use {
            it.readText()
        }
    }
}