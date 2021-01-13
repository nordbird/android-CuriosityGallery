package ru.nordbird.curiositygallery.ui.gallery

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.nordbird.curiositygallery.viewmodel.GalleryViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(layoutInflater, container, false)

        galleryAdapter = GalleryAdapter {
            Snackbar.make(binding.rvGallery, "Click on ${it.id}", Snackbar.LENGTH_LONG).show()
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
            galleryAdapter.updateData(it)
            setRecyclerViewScrollListener(staggeredLayoutManager)
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
                if (totalItemCount <= lastVisibleItemPosition[0] + 2) {
                    binding.rvGallery.removeOnScrollListener(scrollListener)
                    viewModel.nextPage()
                }
            }
        }
        binding.rvGallery.addOnScrollListener(scrollListener)
    }

}