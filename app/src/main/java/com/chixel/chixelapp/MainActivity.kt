package com.chixel.chixelapp

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.*

class MainActivity : AppCompatActivity() {

    private lateinit var c1Button: ImageButton
    private lateinit var c2Button: ImageButton
    private lateinit var toolOptionsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        bindViews()

        c1Button.setOnLongClickListener {
            val intent = Intent(this, ColorPickerPopup::class.java)
            intent.putExtra("whichColorPicker", "one")
            startActivity(intent)
            true
        }
        c2Button.setOnLongClickListener {
            val intent = Intent(this, ColorPickerPopup::class.java)
            intent.putExtra("whichColorPicker", "two")
            startActivity(intent)
            true
        }
        toolOptionsBtn.setOnClickListener {
            val intent = Intent(this, ToolOptionsPopup::class.java)
            startActivity(intent)
        }
    }

    private fun bindViews() {
        c1Button = findViewById<ImageButton>(R.id.color_one_select_button)
        c2Button = findViewById<ImageButton>(R.id.color_two_select_button)
        toolOptionsBtn = findViewById<Button>(R.id.tool_options_open_button)
    }
}
