package com.example.mockuptest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar

class InputFragment : Fragment() {

    private var listener: InputListener? = null
    private var currentQuote: String = ""
    private var currentFontSize: Int = 16

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    interface InputListener {
        fun onQuoteSubmit(quoteText: String, fontSize: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as InputListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.javaClass.simpleName} must implement InputListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editText = view.findViewById<EditText>(R.id.editQuote)
        val seekBar = view.findViewById<SeekBar>(R.id.fontSizeSeekbar)
        val button = view.findViewById<Button>(R.id.applyButton)

        // Load saved values from SharedPreferences
        val prefs = requireContext().getSharedPreferences("QuotePrefs", Context.MODE_PRIVATE)
        currentQuote = prefs.getString("quote", "") ?: ""
        currentFontSize = prefs.getInt("fontSize", 16)
        editText.setText(currentQuote)
        seekBar.progress = currentFontSize

        button.setOnClickListener {
            val quote = editText.text.toString()
            val size = seekBar.progress
            currentQuote = quote
            currentFontSize = size
            Log.d("InputFragment", "Button clicked - Quote: $quote, Size: $size")
            listener?.onQuoteSubmit(quote, size)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("quote", currentQuote)
        outState.putInt("fontSize", currentFontSize)
    }
}