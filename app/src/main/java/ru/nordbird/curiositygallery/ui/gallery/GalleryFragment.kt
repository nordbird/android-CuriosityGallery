package ru.nordbird.curiositygallery.ui.gallery

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.nordbird.curiositygallery.App
import ru.nordbird.curiositygallery.ui.viewmodel.GalleryViewModel
import ru.nordbird.curiositygallery.databinding.FragmentGalleryBinding
import ru.nordbird.curiositygallery.ui.adapters.GalleryAdapter

class GalleryFragment : Fragment() {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var viewModel: GalleryViewModel

    private lateinit var scrollListener: RecyclerView.OnScrollListener
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(layoutInflater, container, false)

        galleryAdapter = GalleryAdapter {
            val photoId = it.id

            viewModel.hidePhoto(photoId)
            Snackbar.make(binding.rvGallery, "Photo ${it.id} is hidden", Snackbar.LENGTH_LONG)
                .setAction("Cancel") {
                    viewModel.restorePhoto(photoId)
                }.show()
            return@GalleryAdapter true
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)

        val staggeredLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        with(binding.rvGallery) {
            layoutManager = staggeredLayoutManager
            adapter = galleryAdapter
        }


        viewModel.getPhotoData().observe(viewLifecycleOwner, Observer {
            if (!it.message.isNullOrEmpty()) Toast.makeText(
                App.applicationContext(),
                it.message,
                Toast.LENGTH_LONG
            ).show()

            galleryAdapter.updateData(it.data)
            setRecyclerViewScrollListener(staggeredLayoutManager)
            loading = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerViewScrollListener(staggeredLayoutManager: StaggeredGridLayoutManager) {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = staggeredLayoutManager.itemCount
                val lastVisibleItemPosition =
                    staggeredLayoutManager.findLastVisibleItemPositions(null)
                if (totalItemCount <= lastVisibleItemPosition[0] + 5 && !loading) {
                    loading = true
                    binding.rvGallery.removeOnScrollListener(scrollListener)
                    Snackbar.make(binding.rvGallery, "Next page", Snackbar.LENGTH_SHORT).show()
                    viewModel.nextPage()
                }
            }
        }
        binding.rvGallery.addOnScrollListener(scrollListener)
    }

}