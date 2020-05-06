package com.chixel.chixelapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chixel.chixelapp.canvas.PixelCanvasView
import com.chixel.chixelapp.canvas.ToolEnum
import com.chixel.chixelapp.database.ImageData
import java.util.*

private const val ARG_IMAGE_NAME = "pictureName"


class CanvasFragment: Fragment() {

    interface CanvasCallback {
        fun toolSavedImagesCallback()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CanvasCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private lateinit var imageViewModel : ColorPickerPopupViewModel
    private var imageName : String? = ""
    private var callbacks: CanvasCallback? = null


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

    companion object {
        fun newInstance(pictureName: String?): CanvasFragment {
            val args = Bundle().apply {
                putSerializable(ARG_IMAGE_NAME, pictureName)
            }
            return CanvasFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ColorPickerPopupModelFactory(requireContext())
        imageViewModel = ViewModelProvider(this, factory).get(ColorPickerPopupViewModel::class.java)
        imageName = arguments?.getSerializable(ARG_IMAGE_NAME) as String?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycler_saved_files, container, false)
        c1Button = view.findViewById(R.id.color_one_select_button)
        c2Button = view.findViewById(R.id.color_two_select_button)
        toolOptionsBtn = view.findViewById(R.id.tool_options_open_button)
        drawView = view.findViewById(R.id.drawView)
        toolBrushBtn = view.findViewById(R.id.toolBrushButton)
        toolEraseBtn = view.findViewById(R.id.toolEraseButton)
        toolUndoBtn = view.findViewById(R.id.toolUndoButton)
        toolRedoBtn = view.findViewById(R.id.toolRedoButton)
        toolSavedImagesBtn = view.findViewById(R.id.toolSavedImagesButton)
        toolSaveBtn = view.findViewById(R.id.toolSaveButton)
        c1Button.setOnLongClickListener {
            val intent = Intent(requireContext(), ColorPickerPopup::class.java)
            intent.putExtra("whichColorPicker", "one")
            startActivity(intent)
            true
        }
        c2Button.setOnLongClickListener {
            val intent = Intent(requireContext(), ColorPickerPopup::class.java)
            intent.putExtra("whichColorPicker", "two")
            startActivity(intent)
            true
        }
        toolOptionsBtn.setOnClickListener {
            val intent = Intent(requireContext(), ToolOptionsPopup::class.java)
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
            callbacks?.toolSavedImagesCallback()
        }
        updateUI(emptyList())
        return view
    }

    fun updateUI(images: List<ImageData>) {

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
}