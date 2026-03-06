package com.mrifkii.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrifkii.dicodingevents.databinding.FragmentHomeBinding
import com.mrifkii.dicodingevents.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

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

        val upcomingAdapter = EventUpcomingAdapter()
        binding.rvUpcomingEvents.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.adapter = upcomingAdapter

        val finishedAdapter = EventAdapter()
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.adapter = finishedAdapter

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        if (savedInstanceState == null) {
            homeViewModel.getUpcomingEvents()
            homeViewModel.getFinishedEvents()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.shimmerViewContainer.visibility = View.VISIBLE
            binding.shimmerViewContainer.startShimmer()
            binding.rvUpcomingEvents.visibility = View.INVISIBLE
            binding.rvFinishedEvents.visibility = View.INVISIBLE
        } else {
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            binding.rvUpcomingEvents.visibility = View.VISIBLE
            binding.rvFinishedEvents.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}