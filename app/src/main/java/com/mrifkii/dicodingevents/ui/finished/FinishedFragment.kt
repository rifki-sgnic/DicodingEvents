package com.mrifkii.dicodingevents.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mrifkii.dicodingevents.R
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

        setupRecyclerView()
        observeViewModel()

//        if (savedInstanceState == null) {
//            finishedViewModel.getFinishedEvents()
//        }
    }

    private fun setupRecyclerView() {
        val finishedAdapter = EventAdapter()
        binding.rvFinishedEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvents.adapter = finishedAdapter
    }

    private fun observeViewModel() {
        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            (binding.rvFinishedEvents.adapter as? EventAdapter)?.submitList(events)
            updateEmptyState()
        }

        finishedViewModel.snackbarText.observe(viewLifecycleOwner) {
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

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun updateEmptyState() {
        val finishedList = finishedViewModel.finishedEvents.value
        binding.apply {
            if (finishedList.isNullOrEmpty() && finishedViewModel.isLoading.value == false) {
                emptyStateSection.visibility = View.VISIBLE
                rvFinishedEvents.visibility = View.GONE
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
                rvFinishedEvents.visibility = View.GONE
                emptyStateSection.visibility = View.GONE
            } else {
                shimmerViewContainer.stopShimmer()
                shimmerViewContainer.visibility = View.GONE
                updateEmptyState()

                if (!emptyStateSection.isShown) {
                    rvFinishedEvents.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}