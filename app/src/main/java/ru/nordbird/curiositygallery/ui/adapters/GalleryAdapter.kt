package ru.nordbird.curiositygallery.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.nordbird.curiositygallery.data.model.PhotoItem
import ru.nordbird.curiositygallery.databinding.ItemPhotoBinding

class GalleryAdapter(val longClickListener: (PhotoItem) -> Boolean) :
    RecyclerView.Adapter<GalleryAdapter.PhotoItemViewHolder>() {

    var items: List<PhotoItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoBinding.inflate(inflater, parent, false)

        return PhotoItemViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        holder.bind(items[position], longClickListener)
    }

    fun updateData(data: List<PhotoItem>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].id == data[newPos].id

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
                items[oldPos].hashCode() == data[newPos].hashCode()

        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    inner class PhotoItemViewHolder(viewBinding: ItemPhotoBinding, private val context: Context) :
        RecyclerView.ViewHolder(viewBinding.root) {
        private var binding: ItemPhotoBinding = viewBinding

        fun bind(item: PhotoItem, longClickListener: (PhotoItem) -> Boolean) {
            if (item.imgSrc.isBlank()) {
                Glide.with(binding.root).clear(binding.ivPhoto)
            } else {
                Glide.with(binding.root).load(item.imgSrc)
                    .into(binding.ivPhoto)
            }

            binding.tvTitle.text = item.earthDate
            itemView.setOnLongClickListener { longClickListener.invoke(item) }
        }
    }
}