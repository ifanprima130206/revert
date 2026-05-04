package com.example.revert

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var rvQuran: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var quranAdapter: QuranAdapter
    private var allSurahList: List<Surah> = emptyList()
    private val apiService = QuranApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        rvQuran = findViewById(R.id.rv_quran)
        etSearch = findViewById(R.id.et_search)
        rvQuran.layoutManager = LinearLayoutManager(this)

        quranAdapter = QuranAdapter(emptyList()) { surah ->
            val intent = Intent(this@MainActivity, AyahActivity::class.java)
            intent.putExtra("SURAH_NUMBER", surah.number)
            intent.putExtra("SURAH_NAME", surah.nameLatin)
            intent.putExtra("SURAH_TRANSLATION", surah.translation)
            intent.putExtra("SURAH_REVELATION", surah.revelation)
            intent.putExtra("SURAH_AYAH_COUNT", surah.numberOfAyahs)
            startActivity(intent)
        }
        rvQuran.adapter = quranAdapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_quran -> {
                    rvQuran.visibility = View.VISIBLE
                    loadSurahList()
                    true
                }
                R.id.nav_jadwal -> {
                    startActivity(Intent(this, JadwalActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }

        // Tampilkan daftar surah saat pertama buka
        bottomNav.selectedItemId = R.id.nav_quran
    }

    private fun filter(text: String) {
        val filteredList = mutableListOf<Surah>()
        for (surah in allSurahList) {
            if (surah.nameLatin.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault())) ||
                surah.translation.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredList.add(surah)
            }
        }
        quranAdapter.updateData(filteredList)
    }

    private fun loadSurahList() {
        apiService.getSurahList().enqueue(object : Callback<QuranResponse> {
            override fun onResponse(call: Call<QuranResponse>, response: Response<QuranResponse>) {
                if (response.isSuccessful) {
                    allSurahList = response.body()?.data ?: emptyList()
                    quranAdapter.updateData(allSurahList)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Gagal memuat data: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<QuranResponse>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}", t)
                Toast.makeText(
                    this@MainActivity,
                    "Tidak dapat terhubung ke server",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}