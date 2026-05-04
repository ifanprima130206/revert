package com.example.revert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuranAdapter(
    private var listSurah: List<Surah>,
    private val onItemClick: (Surah) -> Unit
) : RecyclerView.Adapter<QuranAdapter.ViewHolder>() {

    fun updateData(newList: List<Surah>) {
        listSurah = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumber: TextView = view.findViewById(R.id.tv_number)
        val tvName: TextView = view.findViewById(R.id.tv_surah_name)
        val tvTranslation: TextView = view.findViewById(R.id.tv_translation)
        val tvArabic: TextView = view.findViewById(R.id.tv_arabic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_surah, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val surah = listSurah[position]
        holder.tvNumber.text = surah.number.toString()
        holder.tvName.text = surah.nameLatin
        holder.tvTranslation.text = "${surah.translation} • ${surah.numberOfAyahs} ayat"
        holder.tvArabic.text = surah.name
        holder.itemView.setOnClickListener { onItemClick(surah) }
    }

    override fun getItemCount(): Int = listSurah.size
}