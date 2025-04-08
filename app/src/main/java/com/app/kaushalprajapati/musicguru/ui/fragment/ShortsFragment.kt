package com.app.kaushalprajapati.musicguru.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.kaushalprajapati.musicguru.api.network.NetworkClient
import com.app.kaushalprajapati.musicguru.databinding.FragmentShortsBinding
import com.app.kaushalprajapati.musicguru.repository.VideoRepository
import com.app.kaushalprajapati.musicguru.ui.activity.MainActivity
import com.app.kaushalprajapati.musicguru.ui.adapter.ShortAdapter
import com.app.kaushalprajapati.musicguru.ui.adapter.VideoAdapter
import com.app.kaushalprajapati.musicguru.ui.fragment.auth.LoginFragment
import com.app.kaushalprajapati.musicguru.ui.utils.Resource
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import com.app.kaushalprajapati.musicguru.viewmodels.HomeViewModel
import com.app.kaushalprajapati.musicguru.viewmodels.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.exoplayer2.util.Log

class ShortsFragment : Fragment() {

	private lateinit var binding: FragmentShortsBinding
	private lateinit var videoAdapter: ShortAdapter
	private lateinit var homeViewModel: HomeViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = FragmentShortsBinding.inflate(inflater, container, false)

		val repository = VideoRepository(NetworkClient.apiService)
		homeViewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]

		videoAdapter = ShortAdapter()
		binding.recyclerShort.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerShort.adapter = videoAdapter

		setupObserver()
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (prefsHelper.isLoggedIn(requireContext())) {
			homeViewModel.getShortVideoData()
		} else{
			Toast.makeText(requireContext(), "you are not logged in?", Toast.LENGTH_SHORT).show()
			(activity as MainActivity).loadFragment(LoginFragment())
		}
	}

	private fun setupObserver() {
		homeViewModel.shortVideos.observe(viewLifecycleOwner) { resource ->
			when (resource) {
				is Resource.Loading -> showLoading(true)
				is Resource.Success -> {
					showLoading(false)
					val videoList = resource.data.orEmpty()
					Log.d("ShortsFragment", "Received ${videoList.size} videos")
					if (videoList.isNotEmpty()) {
						videoAdapter.submitList(videoList)
					} else {
						showError("No data found")
					}
				}
				is Resource.Error -> {
					showLoading(false)
					showError(resource.message)
					Log.e("ShortsFragment", "Error fetching videos: ${resource.message}")

				}
			}
		}
	}

	private fun showLoading(loading: Boolean) {
		binding.loadingIndicator.root.visibility = if (loading) View.VISIBLE else View.GONE
	}

	private fun showError(message: String?) {
		Snackbar.make(binding.root, message ?: "error occurred", Snackbar.LENGTH_LONG).show()
	}
}
