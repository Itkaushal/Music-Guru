package com.app.kaushalprajapati.musicguru.ui.fragment

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kaushalprajapati.musicguru.R
import com.app.kaushalprajapati.musicguru.api.network.NetworkClient
import com.app.kaushalprajapati.musicguru.databinding.FragmentHomeBinding
import com.app.kaushalprajapati.musicguru.repository.VideoRepository
import com.app.kaushalprajapati.musicguru.ui.activity.MainActivity
import com.app.kaushalprajapati.musicguru.ui.adapter.VideoAdapter
import com.app.kaushalprajapati.musicguru.ui.fragment.auth.LoginFragment
import com.app.kaushalprajapati.musicguru.ui.utils.Resource
import com.app.kaushalprajapati.musicguru.ui.utils.sharedprefrences.prefsHelper
import com.app.kaushalprajapati.musicguru.viewmodels.HomeViewModel
import com.app.kaushalprajapati.musicguru.viewmodels.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import okhttp3.internal.notify
import retrofit2.http.Query

class HomeFragment : Fragment() {

	private lateinit var binding: FragmentHomeBinding
	private lateinit var homeViewModel: HomeViewModel
	private lateinit var videoAdapter: VideoAdapter
	private lateinit var recyclerView: RecyclerView
	private lateinit var tabLayout: TabLayout
	private lateinit var sharedPreferences: SharedPreferences

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentHomeBinding.inflate(inflater, container, false)
		sharedPreferences = requireContext().getSharedPreferences("MusicGuruPrefs", Context.MODE_PRIVATE)
		return binding.root
	}

	private fun setupTabs() {
		val tabs = listOf("All", "Trends", "Movies", "News", "Tech")
		for (tab in tabs) {
			val tabItem = tabLayout.newTab().setText(tab)
			tabLayout.addTab(tabItem)
			tabItem.view.setBackgroundResource(R.drawable.tab_background)

			val params = tabItem.view.layoutParams as LinearLayout.LayoutParams
			params.leftMargin = 10
			params.rightMargin = 10
			tabItem.view.layoutParams = params
		}

		tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
			override fun onTabSelected(tab: TabLayout.Tab) {
				tab.view.setBackgroundResource(R.drawable.tab_background2)
				showLoading(true)
				loadVideos(tab.text.toString())
			}
			override fun onTabUnselected(tab: TabLayout.Tab) {
				tab.view.setBackgroundResource(R.drawable.tab_background)
				showLoading(false)
				videoAdapter.submitList(emptyList())
			}
			override fun onTabReselected(tab: TabLayout.Tab) {
				tab.view.setBackgroundResource(R.drawable.tab_background2)
				showLoading(true)
				loadVideos(tab.text.toString())
			}
		})
		tabLayout.elevation = 10.0f
		tabLayout.getTabAt(0)?.select()
	}

	private fun loadVideos(category: String) {
		val liveData = when (category) {
			"All" -> homeViewModel.allVideos.also { homeViewModel.loadAllVideos() }
			"Trends" -> homeViewModel.trendingVideos.also { homeViewModel.loadTrendingVideos() }
			"Movies" -> homeViewModel.movie.also { homeViewModel.fetchMovies() }
			"News" -> homeViewModel.news.also { homeViewModel.fetchNews() }
			"Tech" -> homeViewModel.techVideos.also { homeViewModel.fetchTechVideos() }
			else -> null
		}

		liveData?.observe(viewLifecycleOwner, Observer { resource ->
			when (resource) {
				is Resource.Loading -> showLoading(true)
				is Resource.Success -> {
					showLoading(false)
					val videoList = resource.data.orEmpty()
					if (videoList.isNotEmpty()) {
						videoAdapter.submitList(videoList)
						Log.d("HomeFragment", "Received ${videoList.size} $category videos")
					} else {
						Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
						showError("No data found")
					}
				}
				is Resource.Error -> {
					showLoading(false)
					showError(resource.message)
					Log.e("HomeFragment", "Error fetching $category videos: ${resource.message}")
				}
			}
		})
	}

	private fun setupRefresh() {
		binding.swipeRefresh.setOnRefreshListener {
			getCurrentTabIndex()
			binding.swipeRefresh.isRefreshing = false
		}
	}
	private fun getCurrentTabIndex() {
		val selectedTab = tabLayout.getTabAt(tabLayout.selectedTabPosition)
		val category = selectedTab?.text?.toString() ?: "All"
		loadVideos(category)
	}
	private fun showLoading(loading: Boolean) {
		binding.loadingIndicator.root.visibility = if (loading) View.VISIBLE else View.GONE
	}
	private fun showError(message: String?) {
		Snackbar.make(binding.root, message ?: "An error occurred", Snackbar.LENGTH_LONG).show()
	}

	private fun setupSearch() {
		val searchView = binding.searchView
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
			androidx.appcompat.widget.SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				query?.let {
					Log.d("HomeFragment", "Search query submitted: $it")
					homeViewModel.searchVideos(it)
					saveSearchQuery(it) // save history
					searchView.clearFocus()
				}
				return true
			}
			override fun onQueryTextChange(newText: String?): Boolean {
				if (!newText.isNullOrEmpty()) {
					Log.d("HomeFragment", "Search text changed: $newText")
				}
				return false
			}
		})

		searchView.setOnCloseListener {
			Log.d("HomeFragment", "Search closed")
			searchView.setQuery("", false)
			searchView.clearFocus()
			getCurrentTabIndex()
			false
		}

		homeViewModel.videos.observe(viewLifecycleOwner, Observer {
			when (it) {
				is Resource.Loading -> showLoading(true)
				is Resource.Success -> {
					showLoading(false)
					val videoList = it.data.orEmpty()
					if (videoList.isNotEmpty()) {
						videoAdapter.submitList(videoList)
						Log.d("HomeFragment", "Received ${videoList.size} search results")
					} else {
						Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
						showError("No results found")
					}
				}
				is Resource.Error -> {
					showLoading(false)
					showError(it.message)
					Log.e("HomeFragment", "Error searching videos: ${it.message}")
				}

				else -> {}
			}

		})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val repository = VideoRepository(NetworkClient.apiService)
		homeViewModel = ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]
		tabLayout = binding.tabLayout

		if (prefsHelper.isLoggedIn(requireContext())) {
			setupTabs()
			recyclerView = binding.rvVideos
			videoAdapter = VideoAdapter()
			recyclerView.layoutManager = LinearLayoutManager(requireContext())
			recyclerView.adapter = videoAdapter
			setupRefresh()
			setupSearch()

		} else {
			Toast.makeText(requireContext(), "you are not logged in?", Toast.LENGTH_SHORT).show()
			(activity as MainActivity).loadFragment(LoginFragment())
		}
	}

	fun saveSearchQuery(query: String) {
		val historyKey = "search_history"
		val maxHistorySize = 10
		val searchHistory = sharedPreferences.getStringSet(historyKey, mutableSetOf()) ?: mutableSetOf()
		searchHistory.add(query)
		val updatedHistory = searchHistory.take(maxHistorySize).toMutableSet()
		sharedPreferences.edit()
			.putStringSet(historyKey, updatedHistory)
			.apply()

		Log.d("HomeFragment", "Search history updated: $updatedHistory")
	}


}
