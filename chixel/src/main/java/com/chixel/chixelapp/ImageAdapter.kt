package com.chixel.chixelapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chixel.chixelapp.database.ImageData


class ImageAdapter(private val images: List<ImageData>, private val clickListener: (ImageData) -> Unit) : RecyclerView.Adapter<ImageHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_saved_files_list, parent, false)
        return ImageHolder(view)
    }

    override fun getItemCount(): Int {
        return  images.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val image = images[position]
        holder.bind(image, clickListener)
        holder.apply {
            imageNameTextView.text = image.pictureName
            imageDate.text = image.date.toString()
        }
    }
}