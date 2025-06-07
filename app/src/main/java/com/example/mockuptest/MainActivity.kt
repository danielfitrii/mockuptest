package com.example.mockuptest

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit

class MainActivity : AppCompatActivity(), InputFragment.InputListener {

    private var currentQuote: String = ""
    private var currentFontSize: Int = 16

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentQuote = savedInstanceState.getString("quote", "")
            currentFontSize = savedInstanceState.getInt("fontSize", 16)

            val fragment = supportFragmentManager
                .findFragmentById(R.id.quote_fragment) as? QuoteFragment
            fragment?.updateQuote(currentQuote, currentFontSize)
        }

        val prefs = getSharedPreferences("appPrefs", MODE_PRIVATE)
        currentQuote = prefs.getString("quote", "") ?: ""
        currentFontSize = prefs.getInt("fontSize", 16)
    }

    override fun onQuoteSubmit(quoteText: String, fontSize: Int) {
        currentQuote = quoteText
        currentFontSize = fontSize

        val fragment = supportFragmentManager
            .findFragmentById(R.id.quote_fragment) as? QuoteFragment
        fragment?.updateQuote(quoteText, fontSize)

        val prefs = getSharedPreferences("appPrefs", MODE_PRIVATE)
        prefs.edit() {
            putString("quote", quoteText)
                .putInt("fontSize", fontSize)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("quote", currentQuote)
        outState.putInt("fontSize", currentFontSize)
    }

}