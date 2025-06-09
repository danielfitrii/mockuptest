package com.example.mockuptest

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.content.Context

class QuoteFragment : Fragment() {

    private lateinit var frameAnimation: AnimationDrawable
    private var currentQuote: String? = null
    private var currentFontSize: Int = 16

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = view.findViewById<ImageView>(R.id.quoteAnimationView)
        imageView.setBackgroundResource(R.drawable.animation_list)
        frameAnimation = imageView.background as AnimationDrawable

        imageView.post { frameAnimation.start() }

        // Restore from savedInstanceState if available
        if (savedInstanceState != null) {
            currentQuote = savedInstanceState.getString("quote", "Your quote will appear here")
            currentFontSize = savedInstanceState.getInt("fontSize", 16)
        } else {
            // Load from SharedPreferences
            val prefs = requireContext().getSharedPreferences("QuotePrefs", Context.MODE_PRIVATE)
            currentQuote = prefs.getString("quote", "Your quote will appear here")
            currentFontSize = prefs.getInt("fontSize", 16)
        }
        updateQuote(currentQuote ?: "Your quote will appear here", currentFontSize)
    }

    override fun onResume() {
        super.onResume()
        if (::frameAnimation.isInitialized) {
            frameAnimation.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::frameAnimation.isInitialized) {
            frameAnimation.stop()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("quote", currentQuote)
        outState.putInt("fontSize", currentFontSize)
    }

    fun updateQuote(text: String, size: Int) {
        currentQuote = text
        currentFontSize = size

        // Save to SharedPreferences
        val prefs = requireContext().getSharedPreferences("QuotePrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("quote", text)
            putInt("fontSize", size)
            apply()
        }

        val quoteView = view?.findViewById<TextView>(R.id.quoteTextView)
        quoteView?.text = text
        quoteView?.textSize = size.toFloat()
    }
}