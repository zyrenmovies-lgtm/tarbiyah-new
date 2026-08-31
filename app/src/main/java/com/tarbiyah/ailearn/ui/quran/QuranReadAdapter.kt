package com.tarbiyah.ailearn.ui.quran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tarbiyah.ailearn.R
import com.tarbiyah.ailearn.data.model.Ayah

class QuranReadAdapter(
    private var ayahList: List<Ayah>
) : RecyclerView.Adapter<QuranReadAdapter.AyahViewHolder>() {

    inner class AyahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAyahNumber: TextView = itemView.findViewById(R.id.tv_ayah_number)
        val tvArabic: TextView = itemView.findViewById(R.id.tv_ayah_arabic)
        val tvLatin: TextView = itemView.findViewById(R.id.tv_ayah_latin)
        val tvTranslation: TextView = itemView.findViewById(R.id.tv_ayah_translation)

        fun bind(ayah: Ayah) {
            tvAyahNumber.text = ayah.nomorAyat.toString()
            tvArabic.text = ayah.teksArab
            tvLatin.text = ayah.teksLatin
            tvTranslation.text = ayah.teksIndonesia
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayah, parent, false)
        return AyahViewHolder(view)
    }

    override fun onBindViewHolder(holder: AyahViewHolder, position: Int) {
        holder.bind(ayahList[position])
    }

    override fun getItemCount(): Int = ayahList.size

    fun updateData(newList: List<Ayah>) {
        ayahList = newList
        notifyDataSetChanged()
    }
}
