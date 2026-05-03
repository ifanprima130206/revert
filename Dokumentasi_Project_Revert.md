# 📖 Dokumentasi Proyek: Revert (Aplikasi Al-Quran & Jadwal Sholat)

Aplikasi **Revert** dibangun menggunakan bahasa pemrograman **Kotlin** dengan pendekatan arsitektur modern Android (*Separation of Concerns*). Aplikasi ini didesain agar sangat modular, mudah dibaca, dan menggunakan *best practices* dalam hal pemanggilan API dan *rendering* UI.

---

## 🛠️ 1. Library & Dependencies Tambahan

Untuk mendukung fitur-fitur di dalam aplikasi tanpa harus melakukan *coding* dari nol untuk tugas-tugas rumit, proyek ini menggunakan *libraries* berikut yang disematkan di dalam `build.gradle.kts` / `libs.versions.toml`:

### A. Jaringan & API (Networking)
*   **Retrofit2 (`com.squareup.retrofit2:retrofit`)**
    *Kegunaan:* Library HTTP Client paling populer dan handal untuk Android. Memudahkan kita melakukan *request* ke server API (MyQuran dan EQuran) hanya dengan menggunakan anotasi antarmuka (*interface*).
*   **Gson Converter (`com.squareup.retrofit2:converter-gson`)**
    *Kegunaan:* Bertugas men-*translate* (parsing) data mentah berbentuk JSON yang didapat dari server API agar otomatis dikonversi menjadi *Object Data Class* di dalam Kotlin tanpa harus di-*parsing* secara manual satu per satu.

### B. UI & Komponen Antarmuka
*   **Material Design (`com.google.android.material:material`)**
    *Kegunaan:* Komponen desain antarmuka resmi dari Google. Digunakan dalam proyek ini untuk membangun `BottomNavigationView` (navigasi di bawah layar) serta efek `CardView` yang melengkung dan memiliki bayangan (*elevation*).
*   **HtmlCompat (`androidx.core.text.HtmlCompat`)** *(Bawaan AndroidX Core)*
    *Kegunaan:* Digunakan saat me-*render* teks terjemahan atau teks latin yang terkadang membawa *tag* HTML dari API (seperti `<i>` atau `<strong>`), sehingga yang muncul di layar adalah huruf miring/tebal yang rapi, bukan *code* HTML mentahnya.

---

## 📂 2. Struktur File Kotlin & Kegunaannya

Seluruh kode utama *logic* program berada di dalam *package* `com.example.revert`. File-file tersebut dibagi berdasarkan fungsinya masing-masing.

### 🟡 A. Models (Penyimpan Kerangka Data)
File-file ini **tidak memiliki fungsi aksi**, ia murni hanya sebagai cetakan/templat (Data Class) agar Gson tahu bagaimana menyusun JSON ke dalam variabel Kotlin.

1.  **`Surah.kt`**
    *   **Deskripsi:** Model untuk menampung satu Surah.
    *   **Isi Ringkas:** Memiliki variabel nomor, nama surah, nama latin, tempat turun (mekah/madinah), jumlah ayat, dan `List<Ayah>` (daftar ayat) yang akan terisi jika memanggil API detail.

2.  **`Ayah.kt`**
    *   **Deskripsi:** Model untuk menampung barisan teks satu ayat tunggal.
    *   **Isi Ringkas:** Berisi variabel untuk teks Arab, teks transliterasi Latin, dan teks terjemahan bahasa Indonesia.

3.  **`MyQuranModels.kt`**
    *   **Deskripsi:** Model data khusus untuk API Jadwal Sholat dan Kalender Hijriah (API MyQuran v3).
    *   **Isi Ringkas:** Menampung `SholatResponse` (karena jadwal dari API berupa struktur Map Dinamis berdasar tanggal) dan `CalResponse` untuk memisahkan tanggal Masehi dan Hijriah.

---

### 🔵 B. API Services (Penghubung Internet)
File berupa `interface` ini mendaftarkan rute (URL Endpoint) apa saja yang akan dipanggil aplikasinya.

4.  **`QuranApiService.kt`**
    *   **Deskripsi:** Pengelola API dari `equran.id/api/v2/`.
    *   **Isi Ringkas:** 
        *   `@GET("surat")` -> Meminta daftar 114 Surah.
        *   `@GET("surat/{surahNumber}")` -> Meminta 1 Surah secara detail (termasuk seluruh ayatnya sekaligus, tanpa *pagination*).

5.  **`MyQuranApiService.kt`**
    *   **Deskripsi:** Pengelola API dari `api.myquran.com/v3/`.
    *   **Isi Ringkas:** 
        *   `@GET("sholat/jadwal/{id}/today")` -> Meminta data sholat harian berdasarkan ID kota Purwakarta.
        *   `@GET("cal/today")` -> Meminta penanggalan Hijriah hari ini.

---

### 🟢 C. Adapters (Perakit Daftar ke UI)
Adapter merupakan "tukang" yang bertugas menggabungkan data dari *Models* ke *layout* tampilan XML (*RecyclerView*).

6.  **`QuranAdapter.kt`**
    *   **Deskripsi:** Digunakan oleh `MainActivity` untuk menampilkan 114 surah.
    *   **Isi Ringkas:** Menghubungkan variabel nama Surah dan detail lainnya ke dalam *layout* `item_surah.xml`. Jika satu baris diklik (*setOnClickListener*), ia akan men-_trigger_ untuk pindah ke halaman baca.

7.  **`AyahAdapter.kt`**
    *   **Deskripsi:** Digunakan oleh `AyahActivity` untuk menyusun ratusan ayat bacaan agar bisa di-*scroll*.
    *   **Isi Ringkas:** Menghubungkan teks Arab dan latin ke `item_ayah.xml`. Memiliki *logic* `HtmlCompat` khusus untuk membersihkan *tags* HTML dari API agar teks tetap bersih.

---

### 🔴 D. Activities (Layar atau Halaman Aplikasi)
File-file utama yang mengendalikan apa yang dilihat user saat membuka halaman tertentu.

8.  **`MainActivity.kt`**
    *   **Deskripsi:** Halaman *Home* Aplikasi (Daftar Surah).
    *   **Isi Ringkas:** Saat *onCreate*, halaman ini langsung memanggil internet (`loadSurahList`). Ketika merespons sukses, hasil daftarnya dioper ke `QuranAdapter`. Menghandle pula navigasi menu bawah (*Bottom Navigation*).

9.  **`AyahActivity.kt`**
    *   **Deskripsi:** Halaman Baca Ayat Al-Quran secara utuh.
    *   **Isi Ringkas:** Menerima data (nomor surah) dari MainActivity, lalu halaman ini akan memutar efek animasi *Skeleton Loader* abu-abu. Saat respon API berhasil men-*download* seluruh ratusan ayatnya, Skeleton dihilangkan (`visibility = GONE`), lalu *RecyclerView* bacaannya ditampilkan.

10. **`JadwalActivity.kt`**
    *   **Deskripsi:** Halaman Waktu Sholat dan Penanggalan.
    *   **Isi Ringkas:** Melakukan *request* asinkron melalui Retrofit ke Jadwal dan Hijriah secara bersamaan. Di dalamnya terdapat fungsi `bindWaktuSholat()` yang sangat efisien untuk menghindari duplikasi kode saat mencetak 8 jadwal (Imsak hingga Isya) ke layar.

11. **`ProfileActivity.kt`**
    *   **Deskripsi:** Halaman statis Identitas Kelompok.
    *   **Isi Ringkas:** Tidak ada logika API di sini, murni menampilkan UI dari `activity_profile.xml` beserta menangani logika jika user nge-klik *Bottom Navigation* untuk pindah layar.

---

**Selesai!** Struktur di atas sangat menjunjung tinggi pola kode yang bersih. Model digunakan murni untuk data, Adapter untuk menghubungkan daftar, Service untuk internet, dan Activity hanya fokus pada status Layar Utama. 🚀
