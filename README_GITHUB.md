# Volume Screen Control ASUS — GitHub Actions Build

Project ini dibuat agar **tidak perlu Android Studio** untuk membuat APK.

## Cara paling gampang

1. Buat repository baru di GitHub, misalnya:
   `VolumeScreenControl-ASUS`
2. Upload **semua isi ZIP ini** ke repository.
3. Pastikan file `.github/workflows/build-apk.yml` ikut ter-upload.
4. Buka tab **Actions** di repository.
5. Pilih **Build APK**.
6. Klik **Run workflow**.
7. Tunggu sampai selesai.
8. Buka hasil workflow → bagian **Artifacts**.
9. Download:
   `VolumeScreenControl-ASUS-debug`
10. Extract ZIP artifact tersebut. Di dalamnya ada file `.apk`.
11. Install APK di HP ASUS.

Workflow juga otomatis berjalan setiap kali ada push ke branch `main` atau `master`.

## Fungsi aplikasi

- Tombol aplikasi sederhana: AKTIFKAN / MATIKAN.
- Indikator: AKTIF / NONAKTIF.
- Saat AKTIF:
  - Volume UP → layar ON/wake.
  - Volume DOWN → layar OFF/lock.
- Accessibility Service.
- Device Admin.
- Battery Optimization exclusion.
- Boot receiver untuk mendeteksi reboot.

## Catatan ASUS / Android lama

Android tidak mengizinkan aplikasi mengaktifkan Accessibility Service secara diam-diam setelah reboot.
Pada beberapa perangkat ASUS, min juga mungkin perlu mengaktifkan:
- Auto-start aplikasi.
- Izinkan berjalan di background.
- Jangan batasi baterai aplikasi.
- Jangan Force Stop aplikasi.

## Catatan build

- minSdk 26 (Android 8.0)
- targetSdk 28 (Android 9)
- compileSdk 35
- Gradle 8.9
- JDK 17

APK yang dihasilkan adalah **debug APK**, cocok untuk penggunaan pribadi/testing.


## GitHub Actions

Workflow berada di `.github/workflows/build-apk.yml` dan menggunakan branch `utama`. Workflow dapat dijalankan manual dari GitHub Actions. Hasil APK debug diunggah sebagai artifact `VolumeScreenControl-ASUS-debug`.
