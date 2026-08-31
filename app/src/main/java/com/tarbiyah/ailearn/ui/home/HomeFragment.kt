package com.tarbiyah.ailearn.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tarbiyah.ailearn.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import com.tarbiyah.ailearn.network.RetrofitClient
import com.tarbiyah.ailearn.utils.PrayerTimeUtil
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tarbiyah.ailearn.R
import android.widget.LinearLayout
import android.widget.TextView
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.browser.customtabs.CustomTabsIntent
import android.net.Uri

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
        setupUI()
    }

    private fun setupUI() {
        val userName = "Rizki" // TODO: Load from SharedPreferences or ViewModel
        binding.tvUsername.text = userName

        // Set current date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
        binding.tvDate.text = dateFormat.format(Date())

        // Fetch Ads and Prayer Times
        fetchAdsAndShowPopup()
        fetchPrayerTimes()
        
        // Setup Feature Click Listeners
        binding.btnFeatureQuran.setOnClickListener {
            findNavController().navigate(com.tarbiyah.ailearn.R.id.action_homeFragment_to_quranFragment)
        }
        
        binding.btnFeatureBelajar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_belajarDashboardFragment)
        }

        // Notification button
        binding.btnNotification.setOnClickListener {
            binding.badgeNotification.visibility = View.GONE
            findNavController().navigate(R.id.action_homeFragment_to_notificationFragment)
        }
    }

    private fun fetchAdsAndShowPopup() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.adInstance.getAds()
                if (response.isActive && !response.imageUrl.isNullOrEmpty()) {
                    showAdPopup(response.title, response.imageUrl, response.targetUrl)
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching ads", e)
            }
        }
    }

    private fun showAdPopup(title: String?, imageUrl: String, targetUrl: String?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_ad_popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        // Match parent width with margins
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.window?.setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT)

        val ivImage = dialog.findViewById<ImageView>(R.id.iv_ad_image)
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close_ad)
        val tvTitle = dialog.findViewById<TextView>(R.id.tv_ad_title)
        val btnAction = dialog.findViewById<Button>(R.id.btn_ad_action)

        tvTitle.text = title ?: "Promo!"
        
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.bg_input_modern)
            .into(ivImage)

        btnClose.setOnClickListener { dialog.dismiss() }
        
        if (targetUrl.isNullOrEmpty()) {
            btnAction.visibility = View.GONE
        } else {
            btnAction.visibility = View.VISIBLE
            btnAction.setOnClickListener {
                val customTabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .setUrlBarHidingEnabled(true)
                    .setToolbarColor(android.graphics.Color.parseColor("#1A1A2E"))
                    .build()
                customTabsIntent.launchUrl(requireContext(), Uri.parse(targetUrl))
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun fetchPrayerTimes() {
        binding.tvPrayerName.text = "Memuat..."
        
        lifecycleScope.launch {
            try {
                // Fetching for Jakarta by default
                val response = RetrofitClient.prayerInstance.getPrayerTimes(city = "Jakarta")
                val timings = response.data.timings
                
                // Update prayer list UI
                binding.tvTimeSubuh.text = timings.fajr
                binding.tvTimeDzuhur.text = timings.dhuhr
                binding.tvTimeAshar.text = timings.asr
                binding.tvTimeMaghrib.text = timings.maghrib
                binding.tvTimeIsya.text = timings.isha

                // Calculate next prayer
                val nextPrayer = PrayerTimeUtil.getNextPrayer(timings)
                
                // Update Hero Card
                binding.tvPrayerName.text = nextPrayer.name
                binding.tvPrayerTime.text = nextPrayer.time
                
                // Highlight the correct card
                highlightPrayerCard(nextPrayer.name)

            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching prayer times", e)
                Toast.makeText(context, "Gagal mengambil jadwal sholat", Toast.LENGTH_SHORT).show()
                binding.tvPrayerName.text = "GAGAL"
            }
        }
    }

    private fun highlightPrayerCard(prayerName: String) {
        // Reset all cards
        resetCard(binding.cardSubuh, binding.tvLabelSubuh, binding.tvTimeSubuh)
        resetCard(binding.cardDzuhur, binding.tvLabelDzuhur, binding.tvTimeDzuhur)
        resetCard(binding.cardAshar, binding.tvLabelAshar, binding.tvTimeAshar)
        resetCard(binding.cardMaghrib, binding.tvLabelMaghrib, binding.tvTimeMaghrib)
        resetCard(binding.cardIsya, binding.tvLabelIsya, binding.tvTimeIsya)

        // Highlight active
        when (prayerName) {
            "SUBUH" -> activeCard(binding.cardSubuh, binding.tvLabelSubuh, binding.tvTimeSubuh)
            "DZUHUR" -> activeCard(binding.cardDzuhur, binding.tvLabelDzuhur, binding.tvTimeDzuhur)
            "ASHAR" -> activeCard(binding.cardAshar, binding.tvLabelAshar, binding.tvTimeAshar)
            "MAGHRIB" -> activeCard(binding.cardMaghrib, binding.tvLabelMaghrib, binding.tvTimeMaghrib)
            "ISYA" -> activeCard(binding.cardIsya, binding.tvLabelIsya, binding.tvTimeIsya)
        }
    }

    private fun resetCard(card: LinearLayout, label: TextView, time: TextView) {
        card.setBackgroundResource(R.drawable.bg_input_modern)
        card.backgroundTintList = null
        label.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
        time.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
    }

    private fun activeCard(card: LinearLayout, label: TextView, time: TextView) {
        card.setBackgroundResource(R.drawable.bg_button_primary_modern)
        card.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F2C94C"))
        label.setTextColor(android.graphics.Color.parseColor("#3E2723"))
        time.setTextColor(android.graphics.Color.parseColor("#3E2723"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
