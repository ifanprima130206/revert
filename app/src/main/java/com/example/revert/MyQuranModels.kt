package com.example.revert

import com.google.gson.annotations.SerializedName

data class SholatResponse(
    @SerializedName("data") val data: SholatData
)

data class SholatData(
    @SerializedName("kabko") val kabko: String,
    @SerializedName("prov") val prov: String,
    @SerializedName("jadwal") val jadwal: Map<String, JadwalDetail>
)

data class JadwalDetail(
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("imsak") val imsak: String,
    @SerializedName("subuh") val subuh: String,
    @SerializedName("terbit") val terbit: String,
    @SerializedName("dhuha") val dhuha: String,
    @SerializedName("dzuhur") val dzuhur: String,
    @SerializedName("ashar") val ashar: String,
    @SerializedName("maghrib") val maghrib: String,
    @SerializedName("isya") val isya: String
)

data class CalResponse(
    @SerializedName("data") val data: CalData
)

data class CalData(
    @SerializedName("hijr") val hijr: HijrData
)

data class HijrData(
    @SerializedName("today") val today: String
)
