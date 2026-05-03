package com.example.revert

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalActivity : AppCompatActivity() {

    private lateinit var tvLokasi: TextView
    private lateinit var tvTanggalMasehi: TextView
    private lateinit var tvTanggalHijriah: TextView
    private lateinit var progressJadwal: ProgressBar
    private lateinit var containerJadwal: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        tvLokasi = findViewById(R.id.tv_lokasi)
        tvTanggalMasehi = findViewById(R.id.tv_tanggal_masehi)
        tvTanggalHijriah = findViewById(R.id.tv_tanggal_hijriah)
        progressJadwal = findViewById(R.id.progress_jadwal)
        containerJadwal = findViewById(R.id.container_jadwal)

        setupBottomNavigation()
        fetchData()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_jadwal
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_quran -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_jadwal -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchData() {
        val apiService = MyQuranApiService.create()

        // Fetch Hijri Date
        apiService.getHijriDate().enqueue(object : Callback<CalResponse> {
            override fun onResponse(call: Call<CalResponse>, response: Response<CalResponse>) {
                if (response.isSuccessful) {
                    val calData = response.body()?.data
                    if (calData != null) {
                        tvTanggalHijriah.text = calData.hijr.today
                    }
                }
            }
            override fun onFailure(call: Call<CalResponse>, t: Throwable) {
                // Ignore or handle
            }
        })

        // Fetch Jadwal Sholat
        apiService.getJadwalSholat().enqueue(object : Callback<SholatResponse> {
            override fun onResponse(call: Call<SholatResponse>, response: Response<SholatResponse>) {
                progressJadwal.visibility = View.GONE
                if (response.isSuccessful) {
                    val sholatData = response.body()?.data
                    if (sholatData != null) {
                        tvLokasi.text = "${sholatData.kabko}, ${sholatData.prov}"
                        
                        val jadwalDetail = sholatData.jadwal.values.firstOrNull()
                        if (jadwalDetail != null) {
                            tvTanggalMasehi.text = jadwalDetail.tanggal
                            
                            containerJadwal.visibility = View.VISIBLE
                            bindWaktuSholat(R.id.item_imsak, "Imsak", jadwalDetail.imsak)
                            bindWaktuSholat(R.id.item_subuh, "Subuh", jadwalDetail.subuh)
                            bindWaktuSholat(R.id.item_terbit, "Terbit", jadwalDetail.terbit)
                            bindWaktuSholat(R.id.item_dhuha, "Dhuha", jadwalDetail.dhuha)
                            bindWaktuSholat(R.id.item_dzuhur, "Dzuhur", jadwalDetail.dzuhur)
                            bindWaktuSholat(R.id.item_ashar, "Ashar", jadwalDetail.ashar)
                            bindWaktuSholat(R.id.item_maghrib, "Maghrib", jadwalDetail.maghrib)
                            bindWaktuSholat(R.id.item_isya, "Isya", jadwalDetail.isya)
                        }
                    }
                } else {
                    Toast.makeText(this@JadwalActivity, "Gagal memuat jadwal sholat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SholatResponse>, t: Throwable) {
                progressJadwal.visibility = View.GONE
                Toast.makeText(this@JadwalActivity, "Koneksi gagal", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bindWaktuSholat(viewId: Int, nama: String, jam: String) {
        val view = findViewById<View>(viewId)
        view.findViewById<TextView>(R.id.tv_nama_waktu).text = nama
        view.findViewById<TextView>(R.id.tv_jam_waktu).text = jam
    }
}
