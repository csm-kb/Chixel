package com.chixel.chixelapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chixel.chixelapp.canvas.PixelCanvasView
import com.chixel.chixelapp.canvas.ToolEnum
import com.chixel.chixelapp.database.CanvasBitmapData
import java.io.ByteArrayOutputStream
import java.util.*

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

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        bindViews()
        var newBitmap : Bitmap? = null
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getInt("Current_color")
            drawView.setColor(value)
            val newByteArray : ByteArray? = extras.getByteArray("returnSavedBitmap")
            if (newByteArray != null) {
                newBitmap = BitmapFactory.decodeByteArray(newByteArray, 0, newByteArray.size)
            }
            if (newBitmap != null) {
                drawView.setBitmap(newBitmap)
            }

            //The key argument here must match that used in the other activity
        }

        c1Button.setOnLongClickListener {
            val intent = Intent(this, ColorPickerPopup::class.java)
            intent.putExtra("whichColorPicker", "one")
            var stream : ByteArrayOutputStream = ByteArrayOutputStream()
            var bitmap : Bitmap = drawView.getBitmap()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var byteArray : ByteArray = stream.toByteArray()
            intent.putExtra("saved_bitmap", byteArray)
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
            val factory = MainActivityViewModelFactory(this)
            mainActivityViewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
            Toast.makeText(this, "Retrieve button pressed", Toast.LENGTH_SHORT).show()
            var savedBitmapArray : String = ""
            savedBitmapArray = mainActivityViewModel.savedBitmapDB

            var newByteArray : ByteArray = Base64.decode(savedBitmapArray, Base64.DEFAULT)

            var newSavedBitmap : Bitmap? = null
            Toast.makeText(this, "ByteArray data not null", Toast.LENGTH_SHORT).show()
            newSavedBitmap = BitmapFactory.decodeByteArray(newByteArray, 0, newByteArray.size)
            if (newSavedBitmap != null) {
                Toast.makeText(this, "Bitmap data not null", Toast.LENGTH_SHORT).show()
                drawView.clearCanvas()
                drawView.setBitmap(newSavedBitmap)
            }
        }


        toolSaveBtn.setOnClickListener {
            val factory = MainActivityViewModelFactory(this)
            mainActivityViewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
            Toast.makeText(this, "Save button pressed", Toast.LENGTH_SHORT).show()
            var stream : ByteArrayOutputStream = ByteArrayOutputStream()
            var bitmap : Bitmap = drawView.getBitmap()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var byteArray : ByteArray = stream.toByteArray()
            var byteString : String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            var tempCanvasBitmapData = CanvasBitmapData()
            tempCanvasBitmapData.bitmapData = byteString
            mainActivityViewModel.addBitmapToDB(tempCanvasBitmapData)
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
