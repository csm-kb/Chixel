package com.chixel.chixelapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.color_gradient_picker.*
import kotlinx.android.synthetic.main.popup_tool_options.*

class ColorPickerPopup : AppCompatActivity() {
    private var darkStatusBar = false

    private lateinit var confirmBtn: Button
    private lateinit var colorsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.color_gradient_picker)
        supportActionBar?.hide()

        val whichPicker = intent.getStringArrayExtra("whichColorPicker")

        // Set the Status bar appearance for different API levels
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If you want dark status bar, set darkStatusBar to true
                if (darkStatusBar) {
                    this.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }
        }

        confirmBtn = findViewById<Button>(R.id.color_gradient_picker_confirm_button)
        colorsBtn = findViewById<Button>(R.id.color_gradient_picker_colors_button)

        confirmBtn.setOnClickListener {
            onBackPressed()
        }

        colorsBtn.setOnClickListener {
            val intent = Intent(this, UserColorsPopup::class.java)
            startActivity(intent)
        }

        color_picker_background_layout.alpha = 0f
        color_picker_background_layout.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(
                DecelerateInterpolator()
            ).start()
    }

    override fun onBackPressed() {
        color_picker_background_layout.animate()
            .alpha(0f)
            .setDuration(500)
            .setInterpolator(
                DecelerateInterpolator()
            ).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    finish()
                    overridePendingTransition(0, 0)
                }
            }).start()
    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }
}