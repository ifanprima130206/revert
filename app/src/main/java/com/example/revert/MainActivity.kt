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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var rvQuran: RecyclerView
    private val apiService = QuranApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        rvQuran = findViewById(R.id.rv_quran)
        rvQuran.layoutManager = LinearLayoutManager(this)

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

    private fun loadSurahList() {
        apiService.getSurahList().enqueue(object : Callback<QuranResponse> {
            override fun onResponse(call: Call<QuranResponse>, response: Response<QuranResponse>) {
                if (response.isSuccessful) {
                    val surahList = response.body()?.data ?: emptyList()
                    rvQuran.adapter = QuranAdapter(surahList) { surah ->
                        val intent = Intent(this@MainActivity, AyahActivity::class.java)
                        intent.putExtra("SURAH_NUMBER", surah.number)
                        intent.putExtra("SURAH_NAME", surah.nameLatin)
                        intent.putExtra("SURAH_TRANSLATION", surah.translation)
                        intent.putExtra("SURAH_REVELATION", surah.revelation)
                        intent.putExtra("SURAH_AYAH_COUNT", surah.numberOfAyahs)
                        startActivity(intent)
                    }
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