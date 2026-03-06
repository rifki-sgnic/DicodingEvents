package com.mrifkii.dicodingevents.ui.finished

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrifkii.dicodingevents.databinding.FragmentFinishedBinding
import com.mrifkii.dicodingevents.ui.EventAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val finishedViewModel by viewModels<FinishedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishedAdapter = EventAdapter()
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.adapter = finishedAdapter

        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        if (savedInstanceState == null) {
            finishedViewModel.getFinishedEvents()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            Log.d("FinishedFragment", "Loading...")
        } else {
            Log.d("FinishedFragment", "Not Loading")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}