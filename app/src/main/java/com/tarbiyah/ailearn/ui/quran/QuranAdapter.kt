package com.tarbiyah.ailearn.ui.quran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tarbiyah.ailearn.R
import com.tarbiyah.ailearn.data.model.Surah

class QuranAdapter(
    private var surahList: List<Surah>,
    private val onItemClick: (Surah) -> Unit
) : RecyclerView.Adapter<QuranAdapter.SurahViewHolder>() {

    private var filteredList: List<Surah> = surahList

    inner class SurahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.findViewById(R.id.tv_surah_number)
        val tvNameLatin: TextView = itemView.findViewById(R.id.tv_surah_name_latin)
        val tvInfo: TextView = itemView.findViewById(R.id.tv_surah_info)
        val tvArabic: TextView = itemView.findViewById(R.id.tv_surah_arabic)

        fun bind(surah: Surah) {
            tvNumber.text = surah.nomor.toString()
            tvNameLatin.text = surah.namaLatin
            tvInfo.text = "${surah.tempatTurun} • ${surah.jumlahAyat} Ayat"
            tvArabic.text = surah.nama
            
            itemView.setOnClickListener {
                onItemClick(surah)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_surah, parent, false)
        return SurahViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun updateData(newList: List<Surah>) {
        surahList = newList
        filteredList = newList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            surahList
        } else {
            surahList.filter {
                it.namaLatin.contains(query, ignoreCase = true) ||
                it.arti.contains(query, ignoreCase = true) ||
                it.nomor.toString() == query
            }
        }
        notifyDataSetChanged()
    }
}
