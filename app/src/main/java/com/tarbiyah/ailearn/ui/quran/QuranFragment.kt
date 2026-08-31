package com.tarbiyah.ailearn.ui.quran

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tarbiyah.ailearn.R
import com.tarbiyah.ailearn.network.RetrofitClient
import kotlinx.coroutines.launch

class QuranFragment : Fragment() {

    private lateinit var rvSurah: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var etSearch: EditText
    private lateinit var quranAdapter: QuranAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quran, container, false)

        rvSurah = view.findViewById(R.id.rv_surah)
        progressBar = view.findViewById(R.id.progress_bar)
        tvError = view.findViewById(R.id.tv_error)
        etSearch = view.findViewById(R.id.et_search)

        rvSurah.layoutManager = LinearLayoutManager(requireContext())
        quranAdapter = QuranAdapter(emptyList()) { surah ->
            // Navigate to detail fragment
            val bundle = Bundle().apply {
                putInt("nomorSurah", surah.nomor)
                putString("namaSurah", surah.namaLatin)
            }
            findNavController().navigate(R.id.action_quranFragment_to_quranReadFragment, bundle)
        }
        rvSurah.adapter = quranAdapter

        setupSearch()
        loadData()

        return view
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                quranAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        tvError.visibility = View.GONE
        rvSurah.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getSurahList()
                if (response.code == 200) {
                    quranAdapter.updateData(response.data)
                    rvSurah.visibility = View.VISIBLE
                } else {
                    tvError.text = "Gagal memuat data: ${response.message}"
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
