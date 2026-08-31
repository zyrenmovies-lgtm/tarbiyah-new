package com.tarbiyah.ailearn.ui.quran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarbiyah.ailearn.R
import com.tarbiyah.ailearn.network.RetrofitClient
import kotlinx.coroutines.launch

class QuranReadFragment : Fragment() {

    private lateinit var rvAyah: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var tvToolbarTitle: TextView
    private lateinit var quranReadAdapter: QuranReadAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quran_read, container, false)

        rvAyah = view.findViewById(R.id.rv_ayah)
        progressBar = view.findViewById(R.id.progress_bar)
        tvError = view.findViewById(R.id.tv_error)
        toolbar = view.findViewById(R.id.toolbar)
        tvToolbarTitle = view.findViewById(R.id.tv_toolbar_title)

        // Setup Toolbar
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Get args
        val nomorSurah = arguments?.getInt("nomorSurah") ?: 1
        val namaSurah = arguments?.getString("namaSurah") ?: "Al-Fatihah"
        tvToolbarTitle.text = namaSurah

        // Setup RecyclerView
        rvAyah.layoutManager = LinearLayoutManager(requireContext())
        quranReadAdapter = QuranReadAdapter(emptyList())
        rvAyah.adapter = quranReadAdapter

        loadData(nomorSurah)

        return view
    }

    private fun loadData(nomorSurah: Int) {
        progressBar.visibility = View.VISIBLE
        tvError.visibility = View.GONE
        rvAyah.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getSurahDetail(nomorSurah)
                if (response.code == 200) {
                    quranReadAdapter.updateData(response.data.ayat)
                    rvAyah.visibility = View.VISIBLE
                } else {
                    tvError.text = "Gagal memuat ayat: ${response.message}"
                    tvError.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                tvError.text = "Terjadi kesalahan koneksi"
                tvError.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}
