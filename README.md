# 🎌 Sistem Pemilihan Genre Anime

Aplikasi desktop berbasis **Java Swing** untuk mengelola data anime. Aplikasi ini dirancang dengan sistem **Role-Based Access Control (RBAC)**, memisahkan hak akses antara **Admin** (Pengelola Data) dan **User** (Pengunjung).

## 📸 Tampilan Aplikasi
<img width="383" height="292" alt="image" src="https://github.com/user-attachments/assets/85b48ce4-db13-44f6-b659-4b777224e60c" />


## 🚀 Fitur Utama

Sistem ini memiliki dua peran pengguna, dengan fokus fungsionalitas penuh pada **Admin**.

### 👑 Admin (Full Access)
Admin memiliki kendali penuh atas database anime.
- **Login Keamanan:** Akses khusus administrator.
- **Manajemen CRUD:**
  - ✅ **Create:** Menambahkan data anime baru.
  - ✅ **Read:** Melihat daftar lengkap anime.
  - ✅ **Update:** Mengedit data anime (dengan proteksi ID).
  - ✅ **Delete:** Menghapus data anime.
- **Validasi Data:**
  - Mencegah input ID ganda (Duplikasi).
  - Memastikan kolom ID hanya menerima angka.
- **Laporan & Statistik:**
  - Melihat ringkasan total data.
  - Statistik jumlah anime berdasarkan Genre.
- **Navigasi Cepat:** Dashboard dengan shortcut ke menu kelola.

### 👤 User (Read Only)
User hanya dapat mencari referensi anime tanpa mengubah data.
- **Pencarian Cerdas:** Mencari anime berdasarkan Judul.
- **Filter Kategori:** Menyaring anime berdasarkan Genre (Action, Romance, Horror, dll).
- **Sorting:** Mengurutkan daftar anime berdasarkan Judul atau ID.
- **Info Statistik:** Melihat info ringkas jumlah anime yang tersedia.

## 🛠️ Teknologi yang Digunakan
- **Bahasa Pemrograman:** Java (JDK 8+)
- **GUI Library:** Java Swing & AWT
- **Penyimpanan Data:** File CSV (`anime.csv`) - *Tanpa database SQL eksternal, mudah dijalankan.*
- **IDE:** IntelliJ IDEA

## 📂 Struktur Project
Aplikasi ini terdiri dari 4 layar utama:
1.  **Login Screen:** Gerbang masuk untuk membedakan Admin/User.
2.  **Dashboard:** Menu utama navigasi.
3.  **List Data:** Tabel data dengan fitur Search & Sort.
4.  **Input Form:** Halaman kelola data (Khusus Admin).

## 🔑 Akun Demo
Gunakan akun berikut untuk mencoba aplikasi:

| Role      | Username | Password   | Akses                     |
| :---      | :---     | :---       | :---                      |
| **Admin** | `admin`  | `admin123` | **Full (CRUD + Laporan)** |
| **User**  | `user`   | `user123`  | **View & Search Only**    |

## 📦 Cara Menjalankan
1. Clone repository ini:
   ```bash
   git clone [https://github.com/syamsul-99/sistem-pemilihan-genre-manga-anime.git](https://github.com/syamsul-99/sistem-pemilihan-genre-manga-anime.git)
