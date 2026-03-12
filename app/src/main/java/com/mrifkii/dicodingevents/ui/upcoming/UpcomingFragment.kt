package com.mrifkii.dicodingevents.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mrifkii.dicodingevents.R
import com.mrifkii.dicodingevents.databinding.FragmentUpcomingBinding
import com.mrifkii.dicodingevents.ui.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel by viewModels<UpcomingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val finishedAdapter = EventAdapter()
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcomingEvents.adapter = finishedAdapter
    }

    private fun observeViewModel() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            (binding.rvUpcomingEvents.adapter as? EventAdapter)?.submitList(events)
            updateEmptyState()
        }

        upcomingViewModel.snackbarText.observe(viewLifecycleOwner) {
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

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun updateEmptyState() {
        val finishedList = upcomingViewModel.upcomingEvents.value
        binding.apply {
            if (finishedList.isNullOrEmpty() && upcomingViewModel.isLoading.value == false) {
                emptyStateSection.visibility = View.VISIBLE
                rvUpcomingEvents.visibility = View.GONE
            } else {
                emptyStateSection.visibility = View.GONE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                shimmerViewContainer.visibility = View.VISIBLE
                shimmerViewContainer.startShimmer()
                rvUpcomingEvents.visibility = View.GONE
                emptyStateSection.visibility = View.GONE
            } else {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                updateEmptyState()

                if (!emptyStateSection.isShown) {
                    rvUpcomingEvents.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}