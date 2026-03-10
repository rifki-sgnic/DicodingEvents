package com.mrifkii.dicodingevents.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mrifkii.dicodingevents.R
import com.mrifkii.dicodingevents.data.response.Event
import com.mrifkii.dicodingevents.databinding.ActivityDetailBinding
import com.mrifkii.dicodingevents.utils.DateFormatter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventId = intent.getIntExtra(EXTRA_ID, -1)

        detailViewModel.event.observe(this) { event ->
            if (event != null) {
                setEventData(event)
                binding.contentScroll.visibility = View.VISIBLE
                binding.emptyStateDetail.visibility = View.GONE
            }
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    binding.root,
                    snackBarText,
                    Snackbar.LENGTH_LONG
                ).apply {
                    anchorView = binding.btnRegister
                    show()
                }
                
                if (detailViewModel.event.value == null) {
                    binding.contentScroll.visibility = View.GONE
                    binding.emptyStateDetail.visibility = View.VISIBLE
                    binding.btnRegister.isEnabled = false
                }
            }
        }

        binding.btnRetry.setOnClickListener {
            if (eventId != -1) {
                detailViewModel.getDetailEvent(eventId.toString())
            }
        }

        if (eventId != -1) {
            detailViewModel.getDetailEvent(eventId.toString())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setEventData(event: Event) {
        val remainingQuota = event.quota - event.registrants
        val isPast = DateFormatter.isEventPast(event.beginTime)

        supportActionBar?.title = event.name
        binding.tvDetailName.text = event.name
        binding.tvDetailOwner.text = event.ownerName
        binding.tvDetailTime.text = DateFormatter.formatEventDate(event.beginTime)
        binding.tvDetailQuota.text = resources.getQuantityString(
            R.plurals.slots_remaining,
            remainingQuota,
            remainingQuota,
        )
        binding.tvDetailDescription.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivDetailImage)

        val canRegister = remainingQuota > 0 && !isPast

        binding.btnRegister.isEnabled = canRegister
        binding.btnRegister.text = when {
            isPast -> getString(R.string.event_ended)
            remainingQuota <= 0 -> getString(R.string.sold_out)
            else -> getString(R.string.register_for_event)
        }
        binding.btnRegister.setOnClickListener {
            if (binding.btnRegister.isEnabled) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = event.link.toUri()
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.contentScroll.visibility = View.GONE
            binding.emptyStateDetail.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}