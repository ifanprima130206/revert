package com.example.revert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView

class AyahAdapter(private val listAyah: List<Ayah>) : RecyclerView.Adapter<AyahAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAyahNumber: TextView = view.findViewById(R.id.tv_ayah_number)
        val tvArabic: TextView = view.findViewById(R.id.tv_ayah_arabic)
        val tvLatin: TextView = view.findViewById(R.id.tv_ayah_latin)
        val tvTranslation: TextView = view.findViewById(R.id.tv_ayah_translation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayah, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ayah = listAyah[position]
        holder.tvAyahNumber.text = ayah.ayahNumber.toString()
        holder.tvArabic.text = ayah.arab
        holder.tvTranslation.text = HtmlCompat.fromHtml(ayah.translation, HtmlCompat.FROM_HTML_MODE_COMPACT)

        // Latin transliterasi
        if (ayah.latin.isNotEmpty()) {
            holder.tvLatin.text = HtmlCompat.fromHtml(ayah.latin, HtmlCompat.FROM_HTML_MODE_COMPACT)
            holder.tvLatin.visibility = View.VISIBLE
        } else {
            holder.tvLatin.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = listAyah.size
}
