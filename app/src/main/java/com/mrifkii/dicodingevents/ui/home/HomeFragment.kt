package com.mrifkii.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mrifkii.dicodingevents.R
import com.mrifkii.dicodingevents.databinding.FragmentHomeBinding
import com.mrifkii.dicodingevents.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // Upcoming Events
        val upcomingAdapter = EventUpcomingAdapter()
        binding.rvUpcomingEvents.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.adapter = upcomingAdapter

        // Finished Events
        val finishedAdapter = EventAdapter()
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinishedEvents.adapter = finishedAdapter

        // Search Results
        val searchAdapter = EventAdapter()
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSearchResults.adapter = searchAdapter
    }

    private fun setupSearchView() {
        binding.edSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val query = textView.text.toString()
                if (query.isNotEmpty()) {
                    setSearchMode(true)
                    homeViewModel.searchEvents(query)
                } else {
                    setSearchMode(false)
                }
                true
            } else {
                false
            }
        }

        binding.searchLayout.setEndIconOnClickListener {
            binding.edSearch.text?.clear()
            setSearchMode(false)
        }
    }

    private fun setSearchMode(searching: Boolean) {
        isSearching = searching
        binding.apply {
            if (isSearching) {
                homeContentGroup.visibility = View.GONE
                searchContentGroup.visibility = View.VISIBLE
            } else {
                homeContentGroup.visibility = View.VISIBLE
                searchContentGroup.visibility = View.GONE
                emptyStateSection.visibility = View.GONE
            }
        }
    }

    private fun observeViewModel() {
        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            (binding.rvUpcomingEvents.adapter as? EventUpcomingAdapter)?.submitList(events)
            updateEmptyState()
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            (binding.rvFinishedEvents.adapter as? EventAdapter)?.submitList(events)
            updateEmptyState()
        }

        homeViewModel.searchResults.observe(viewLifecycleOwner) { events ->
            (binding.rvSearchResults.adapter as? EventAdapter)?.submitList(events)
            updateEmptyState()
        }

        homeViewModel.snackbarText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).apply {
                    anchorView = requireActivity().findViewById(R.id.nav_view)
                    show()
                }
                updateEmptyState()
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun updateEmptyState() {
        val upcomingList = homeViewModel.upcomingEvents.value
        val finishedList = homeViewModel.finishedEvents.value
        val searchList = homeViewModel.searchResults.value

        binding.apply {
            if (isSearching) {
                if (searchList.isNullOrEmpty() && homeViewModel.isLoading.value == false) {
                    emptyStateSection.visibility = View.VISIBLE
                    searchContentGroup.visibility = View.GONE
                } else {
                    emptyStateSection.visibility = View.GONE
                }
            } else {
                if (upcomingList.isNullOrEmpty() && finishedList.isNullOrEmpty() && homeViewModel.isLoading.value == false) {
                    emptyStateSection.visibility = View.VISIBLE
                    homeContentGroup.visibility = View.GONE
                } else {
                    emptyStateSection.visibility = View.GONE
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                shimmerViewContainer.visibility = View.VISIBLE
                shimmerViewContainer.startShimmer()
                
                if (isSearching) {
                    searchShimmerSection.visibility = View.VISIBLE
                    homeShimmerSection.visibility = View.GONE
                } else {
                    searchShimmerSection.visibility = View.GONE
                    homeShimmerSection.visibility = View.VISIBLE
                }

                homeContentGroup.visibility = View.GONE
                searchContentGroup.visibility = View.GONE
                emptyStateSection.visibility = View.GONE
            } else {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                updateEmptyState()
                
                if (!binding.emptyStateSection.isShown) {
                    if (isSearching) {
                        searchContentGroup.visibility = View.VISIBLE
                        homeContentGroup.visibility = View.GONE
                    } else {
                        homeContentGroup.visibility = View.VISIBLE
                        searchContentGroup.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}