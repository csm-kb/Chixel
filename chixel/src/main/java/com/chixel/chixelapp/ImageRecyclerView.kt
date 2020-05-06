package com.chixel.chixelapp

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chixel.chixelapp.database.ImageData

class ImageRecyclerView : Fragment() {

    interface ImageRecyclerViewCallback {
        fun callCanvasFragment(pictureName: String?)
    }

    private lateinit var colorPickerPopupViewModel : ColorPickerPopupViewModel
    private lateinit var imageListRecyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private var callback : ImageRecyclerViewCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ImageRecyclerViewCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ColorPickerPopupModelFactory(requireContext())
        colorPickerPopupViewModel = ViewModelProvider(this, factory).get(ColorPickerPopupViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycler_saved_files, container, false)
        imageListRecyclerView = view.findViewById(R.id.recycler_saved_recycler_view)
        imageListRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorPickerPopupViewModel.imageListLiveData.observe(
            viewLifecycleOwner,
            Observer { images ->
                images?.let {
                    updateUI(images)
                }
            }
        )
    }

    fun updateUI(images: List<ImageData>) {
        adapter = ImageAdapter(images) { image: ImageData ->
           callback?.callCanvasFragment(image.pictureName)
        }

        imageListRecyclerView.adapter = adapter
    }

}