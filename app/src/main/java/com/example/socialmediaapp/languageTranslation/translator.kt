package com.example.socialmediaapp.languageTranslation

import java.util.Locale

// Function to dynamically load translated strings based on device's language
fun getTranslatedString(key: String): String {
    val currentLocale: Locale = Locale.getDefault()

    return currentLocale.toString()
}