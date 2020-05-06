package com.chixel.chixelapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chixel.chixelapp.database.ImageData
import com.chixel.chixelapp.database.ImageDataRepository
import com.rarepebble.colorpicker.ColorPickerView
import kotlinx.android.synthetic.main.color_gradient_picker.*
import kotlinx.android.synthetic.main.popup_tool_options.*
import java.util.*


private const val logTag = "ColorPickerPopup"
class ColorPickerPopup : AppCompatActivity() {
    private var darkStatusBar = false

    private lateinit var confirmBtn: Button
    private lateinit var colorsBtn: Button
    private lateinit var colorPicker : ColorPickerView
    private var color : Int = 0
    private var hexString : String = ""
    private lateinit var colorPickerPopupViewModel: ColorPickerPopupViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ColorPickerPopupModelFactory(this)
        colorPickerPopupViewModel = ViewModelProvider(this, factory).get(ColorPickerPopupViewModel::class.java)
        overridePendingTransition(0, 0)
        setContentView(R.layout.color_gradient_picker)
        supportActionBar?.hide()

        val whichPicker = intent.getStringArrayExtra("whichColorPicker")

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

        confirmBtn = findViewById<Button>(R.id.color_gradient_picker_confirm_button)
        colorsBtn = findViewById<Button>(R.id.color_gradient_picker_colors_button)
        colorPicker = findViewById<ColorPickerView>(R.id.color_picker_view)
        colorPicker.setColor(-65536)

        confirmBtn.setOnClickListener {
            color = colorPicker.color
            //Toast.makeText(this, "ColorInt code is: " + color, Toast.LENGTH_SHORT).show()
            //hexString = String.format("#%08X", 0xFFFFFF, color)
            hexString = Integer.toHexString(color)
            val tempColorPicker = ImageData()
            tempColorPicker.colorOne = hexString
            tempColorPicker.date = Date()
            colorPickerPopupViewModel.addSingleImageData(tempColorPicker)

            colorPickerPopupViewModel.allColorLiveData.observe(
                this,
                Observer { images ->
                    images?.let {
                        Log.d(logTag, "Got images ${images.size}")
                        //Toast.makeText(this, "Got images ${images.size}", Toast.LENGTH_SHORT).show()
                    }
                }
            )

//            colorPickerPopupViewModel.allColorOneData.observe(
//                this,
//                Observer { colors ->
//                    colors?.let {
//                        Log.d(logTag, "Got colors ${colors}")
//                        Toast.makeText(this, "Got images ${colors}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            )

            //Toast.makeText(this, "Hex code is: " + hexString, Toast.LENGTH_SHORT).show()
            //colorPicker.setColor(color)
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