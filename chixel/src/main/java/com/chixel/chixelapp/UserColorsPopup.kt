package com.chixel.chixelapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rarepebble.colorpicker.ColorPickerView
import kotlinx.android.synthetic.main.user_colors_popup.*

class UserColorsPopup : AppCompatActivity() {
    private var darkStatusBar = false

    private lateinit var confirmBtn: Button
    private lateinit var colorPicker : ColorPickerView
    private var color : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.user_colors_popup)
        supportActionBar?.hide()

        // Set the Status bar appearance for different API levels
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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

        confirmBtn = findViewById<Button>(R.id.user_colors_confirm_button)


        user_colors_background_layout.alpha = 0f
        user_colors_background_layout.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(
                DecelerateInterpolator()
            ).start()

        colorPicker = findViewById<ColorPickerView>(R.id.color_picker_view)
        colorPicker.setColor(-65536)


        confirmBtn.setOnClickListener {
            color = colorPicker.color
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        user_colors_background_layout.animate()
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