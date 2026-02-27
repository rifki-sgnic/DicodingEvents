package com.mrifkii.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrifkii.dicodingevents.databinding.FragmentHomeBinding
import com.mrifkii.dicodingevents.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        // Setup Upcoming Events (Horizontal)
        val upcomingAdapter = EventAdapter() // You might want a different adapter for different layout if needed
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingEvents.adapter = upcomingAdapter

        // Setup Finished Events (Vertical)
        val finishedAdapter = EventAdapter()
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.adapter = finishedAdapter
        
        // Example: upcomingAdapter.submitList(upcomingEvents)
        // Example: finishedAdapter.submitList(finishedEvents)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}