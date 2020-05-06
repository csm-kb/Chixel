package com.chixel.chixelapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.*
import com.chixel.chixelapp.canvas.PixelCanvasView
import com.chixel.chixelapp.canvas.ToolEnum
import com.chixel.chixelapp.database.ImageData

class MainActivity : AppCompatActivity(), CanvasFragment.CanvasCallback, ImageRecyclerView.ImageRecyclerViewCallback {

    private lateinit var c1Button: ImageButton
    private lateinit var c2Button: ImageButton
    private lateinit var toolOptionsBtn: Button
    private lateinit var drawView: PixelCanvasView

    // tools
    private lateinit var toolBrushBtn: ImageButton
    private lateinit var toolEraseBtn: ImageButton
    // other buttons
    private lateinit var toolUndoBtn: ImageButton
    private lateinit var toolRedoBtn: ImageButton
    private lateinit var toolSavedImagesBtn : ImageButton
    private lateinit var toolSaveBtn: ImageButton

    private var currentTool: ToolEnum = ToolEnum.BRUSH

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

        guiCurrentToolSelected()

        toolBrushBtn.setOnClickListener {
            if (currentTool == ToolEnum.BRUSH) return@setOnClickListener
            currentTool = ToolEnum.BRUSH
            // TODO: set brush tool in draw view
            if (drawView.isEraserOn) drawView.toggleEraser()
            guiCurrentToolSelected()
        }
        toolEraseBtn.setOnClickListener {
            if (currentTool == ToolEnum.ERASE) return@setOnClickListener
            currentTool = ToolEnum.ERASE
            // TODO: set erase tool in draw view
            if (!drawView.isEraserOn) drawView.toggleEraser()
            guiCurrentToolSelected()
        }
        toolUndoBtn.setOnClickListener {
            drawView.undo()
        }
        toolRedoBtn.setOnClickListener {
            drawView.redo()
        }
        toolSavedImagesBtn.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.recycler_container)
            if (currentFragment == null) {
                // val fragment = createFragment()
                val fragment = ImageRecyclerView()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.screen_container, fragment)
                    .commit()

            }
        }
    }

    private val toolSelColor: Int = Color.parseColor("#ff00aae0")
    private fun guiCurrentToolSelected() {
        when(currentTool) {
            ToolEnum.BRUSH -> {
                toolBrushBtn.setBackgroundColor(toolSelColor)
                toolEraseBtn.setBackgroundColor(Color.TRANSPARENT)
            }
            ToolEnum.ERASE -> {
                toolBrushBtn.setBackgroundColor(Color.TRANSPARENT)
                toolEraseBtn.setBackgroundColor(toolSelColor)
            }
        }
    }

    private fun bindViews() {
        c1Button = findViewById(R.id.color_one_select_button)
        c2Button = findViewById(R.id.color_two_select_button)
        toolOptionsBtn = findViewById(R.id.tool_options_open_button)
        drawView = findViewById(R.id.drawView)
        toolBrushBtn = findViewById(R.id.toolBrushButton)
        toolEraseBtn = findViewById(R.id.toolEraseButton)
        toolUndoBtn = findViewById(R.id.toolUndoButton)
        toolRedoBtn = findViewById(R.id.toolRedoButton)
        toolSavedImagesBtn = findViewById(R.id.toolSavedImagesButton)
        toolSaveBtn = findViewById(R.id.toolSaveButton)
    }

    override fun toolSavedImagesCallback() {
            // val fragment = createFragment()
            val fragment = ImageRecyclerView()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.screen_container, fragment)
                .commit()
    }

    override fun callCanvasFragment(pictureName: String?) {
        val fragment = CanvasFragment.newInstance(pictureName)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.screen_container, fragment)
            .commit()
    }
}
