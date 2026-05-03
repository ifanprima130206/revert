package com.example.revert

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AyahActivity : AppCompatActivity() {

    private lateinit var rvAyah: RecyclerView
    private lateinit var skeletonContainer: LinearLayout
    private lateinit var tvHeader: TextView
    private lateinit var tvSubHeader: TextView
    private lateinit var tvAyahCount: TextView
    private lateinit var btnBack: ImageView
    private val apiService = QuranApiService.create()
    private var skeletonAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayah)

        val surahNumber   = intent.getIntExtra("SURAH_NUMBER", 1)
        val surahName     = intent.getStringExtra("SURAH_NAME") ?: "Al-Quran"
        val surahTrans    = intent.getStringExtra("SURAH_TRANSLATION") ?: ""
        val surahRevel    = intent.getStringExtra("SURAH_REVELATION") ?: ""
        val totalAyahs    = intent.getIntExtra("SURAH_AYAH_COUNT", 0)

        rvAyah            = findViewById(R.id.rv_ayah)
        skeletonContainer = findViewById(R.id.skeleton_container)
        tvHeader          = findViewById(R.id.tv_surah_header)
        tvSubHeader       = findViewById(R.id.tv_surah_sub_header)
        tvAyahCount       = findViewById(R.id.tv_ayah_count)
        btnBack           = findViewById(R.id.btn_back)

        // Init animation
        skeletonAnimator = AnimatorInflater.loadAnimator(this, R.animator.skeleton_pulse) as ObjectAnimator
        skeletonAnimator?.target = skeletonContainer

        // Header: "../Al-Fatihah" style persis seperti website
        tvHeader.text    = "../$surahName"
        tvSubHeader.text = if (surahTrans.isNotEmpty() && surahRevel.isNotEmpty())
            "$surahTrans - $surahRevel"
        else surahTrans
        tvAyahCount.text = if (totalAyahs > 0) "$totalAyahs Ayat" else ""

        btnBack.setOnClickListener { finish() }

        rvAyah.layoutManager = LinearLayoutManager(this)
        loadAyahs(surahNumber)
    }

    private fun loadAyahs(surahNumber: Int) {
        skeletonContainer.visibility = View.VISIBLE
        skeletonAnimator?.start()
        rvAyah.visibility = View.GONE

        apiService.getSurahDetail(surahNumber).enqueue(object : Callback<SurahDetailResponse> {
            override fun onResponse(
                call: Call<SurahDetailResponse>,
                response: Response<SurahDetailResponse>
            ) {
                skeletonAnimator?.cancel()
                skeletonContainer.visibility = View.GONE
                rvAyah.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val detail = response.body()?.data
                    val ayahs  = detail?.ayahs ?: emptyList()

                    // Update ayah count dari API jika belum di-set
                    if (tvAyahCount.text.isEmpty() && detail != null) {
                        tvAyahCount.text = "${detail.numberOfAyahs} Ayat"
                    }

                    rvAyah.adapter = AyahAdapter(ayahs)
                } else {
                    Toast.makeText(
                        this@AyahActivity,
                        "Gagal memuat ayat: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SurahDetailResponse>, t: Throwable) {
                skeletonAnimator?.cancel()
                skeletonContainer.visibility = View.GONE
                Log.e("AyahActivity", "Error: ${t.message}", t)
                Toast.makeText(
                    this@AyahActivity,
                    "Tidak dapat terhubung ke server",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
