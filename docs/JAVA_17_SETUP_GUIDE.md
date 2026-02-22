# ☕ Java 17 Installation & Setup Guide

Panduan lengkap untuk menginstall dan mengkonfigurasi Java 17 (JDK 17) di Windows dan macOS, serta setup di IntelliJ IDEA.

## 📋 Daftar Isi
- [Prerequisites](#-prerequisites)
- [Windows Installation](#-windows-installation)
- [macOS Installation](#-macos-installation)
- [Verifikasi Instalasi](#-verifikasi-instalasi)
- [Setup IntelliJ IDEA](#-setup-intellij-idea)
- [Troubleshooting](#-troubleshooting)

---

## 📦 Prerequisites

Sebelum memulai, pastikan:
- Koneksi internet stabil
- Hak akses Administrator (Windows) atau sudo (macOS)
- IntelliJ IDEA sudah terinstall (Community atau Ultimate Edition)

---

## 🪟 Windows Installation

### Option 1: Menggunakan winget (Recommended)

**winget** adalah package manager bawaan Windows 10/11.

#### Step 1: Buka Command Prompt sebagai Administrator

1. Tekan `Win + X`
2. Pilih **Terminal (Admin)** atau **Command Prompt (Admin)**

#### Step 2: Install JDK 17

```cmd
winget install EclipseAdoptium.Temurin.17.JDK
```

#### Step 3: Restart Terminal

Tutup dan buka kembali Command Prompt.

---

### Option 2: Menggunakan Chocolatey

#### Step 1: Install Chocolatey (jika belum ada)

Buka PowerShell sebagai Administrator dan jalankan:

```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

#### Step 2: Install JDK 17

```cmd
choco install temurin17 -y
```

---

### Option 3: Manual Download

#### Step 1: Download JDK 17

1. Buka browser dan kunjungi: https://adoptium.net/temurin/releases/
2. Pilih:
   - **Operating System**: Windows
   - **Architecture**: x64
   - **Package Type**: JDK
   - **Version**: 17 - LTS
3. Klik **Download** (.msi installer)

#### Step 2: Install JDK 17

1. Double-click file `.msi` yang sudah didownload
2. Klik **Next** pada setiap langkah
3. ✅ Centang **Set JAVA_HOME variable**
4. ✅ Centang **Add to PATH**
5. Klik **Install**
6. Tunggu proses selesai, lalu klik **Finish**

---

### Setup JAVA_HOME (Windows)

Jika `JAVA_HOME` tidak otomatis ter-set:

#### Step 1: Buka Environment Variables

1. Tekan `Win + R`
2. Ketik `sysdm.cpl` dan tekan Enter
3. Klik tab **Advanced**
4. Klik **Environment Variables**

#### Step 2: Tambah JAVA_HOME

1. Di bagian **System variables**, klik **New**
2. Isi:
   - **Variable name**: `JAVA_HOME`
   - **Variable value**: `C:\Program Files\Eclipse Adoptium\jdk-17.0.12.7-hotspot` (sesuaikan dengan versi Anda)
3. Klik **OK**

#### Step 3: Update PATH

1. Di **System variables**, cari dan pilih `Path`
2. Klik **Edit**
3. Klik **New**
4. Tambahkan: `%JAVA_HOME%\bin`
5. Klik **OK** di semua dialog

#### Step 4: Restart Terminal

Tutup semua Command Prompt/PowerShell dan buka kembali.

---

## 🍎 macOS Installation

### Option 1: Menggunakan Homebrew (Recommended)

#### Step 1: Install Homebrew (jika belum ada)

Buka Terminal dan jalankan:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

#### Step 2: Install JDK 17

```bash
brew install openjdk@17
```

#### Step 3: Create Symlink

```bash
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

> **Note**: Untuk Mac Intel, gunakan path:
> ```bash
> sudo ln -sfn /usr/local/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
> ```

#### Step 4: Set JAVA_HOME

Tambahkan ke `~/.zshrc`:

```bash
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

---

### Option 2: Menggunakan SDKMAN

#### Step 1: Install SDKMAN

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

#### Step 2: Install JDK 17

```bash
sdk install java 17.0.11-tem
```

#### Step 3: Set as Default

```bash
sdk default java 17.0.11-tem
```

---

### Option 3: Manual Download

#### Step 1: Download JDK 17

1. Buka browser dan kunjungi: https://adoptium.net/temurin/releases/
2. Pilih:
   - **Operating System**: macOS
   - **Architecture**: aarch64 (Apple Silicon) atau x64 (Intel)
   - **Package Type**: JDK
   - **Version**: 17 - LTS
3. Download file `.pkg`

#### Step 2: Install JDK 17

1. Double-click file `.pkg`
2. Ikuti wizard instalasi
3. Masukkan password jika diminta

#### Step 3: Set JAVA_HOME

```bash
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc
```

---

## ✅ Verifikasi Instalasi

### Check Java Version

**Windows (CMD):**
```cmd
java -version
```

**macOS (Terminal):**
```bash
java -version
```

**Output yang diharapkan:**
```
openjdk version "17.0.12" 2024-07-16 LTS
OpenJDK Runtime Environment Temurin-17.0.12+7 (build 17.0.12+7-LTS)
OpenJDK 64-Bit Server VM Temurin-17.0.12+7 (build 17.0.12+7-LTS, mixed mode)
```

### Check JAVA_HOME

**Windows:**
```cmd
echo %JAVA_HOME%
```

**macOS:**
```bash
echo $JAVA_HOME
```

### Check javac (Compiler)

```bash
javac -version
```

**Output:**
```
javac 17.0.12
```

---

## 🖥 Setup IntelliJ IDEA

### Step 1: Buka Project Settings

1. Buka IntelliJ IDEA
2. Buka project Anda atau buat project baru
3. Klik **File** → **Project Structure** (atau tekan `Ctrl+Alt+Shift+S` / `Cmd+;`)

### Step 2: Configure Project SDK

1. Di panel kiri, pilih **Project**
2. Di bagian **SDK**, klik dropdown
3. Pilih **Add SDK** → **JDK...**

![Project SDK](https://resources.jetbrains.com/help/img/idea/2024.1/add_sdk.png)

### Step 3: Select JDK 17 Path

**Windows:**
```
C:\Program Files\Eclipse Adoptium\jdk-17.0.12.7-hotspot
```

**macOS (Homebrew):**
```
/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
```

**macOS (SDKMAN):**
```
~/.sdkman/candidates/java/17.0.11-tem
```

### Step 4: Set Language Level

1. Di **Project** settings, set **Language level** ke **17 - Sealed types, always-strict floating-point semantics**
2. Klik **Apply**

### Step 5: Configure Module SDK

1. Di panel kiri, pilih **Modules**
2. Pilih module project Anda
3. Di tab **Dependencies**, set **Module SDK** ke **Project SDK (17)**

### Step 6: Configure Gradle JVM (untuk Gradle projects)

1. Klik **File** → **Settings** (atau `Ctrl+Alt+S` / `Cmd+,`)
2. Navigate ke **Build, Execution, Deployment** → **Build Tools** → **Gradle**
3. Di bagian **Gradle JVM**, pilih **Project SDK (17)** atau path JDK 17

![Gradle JVM](https://resources.jetbrains.com/help/img/idea/2024.1/gradle_jvm.png)

### Step 7: Apply dan OK

1. Klik **Apply**
2. Klik **OK**

---

## 🔄 Switching Between Java Versions

### Windows

Ubah `JAVA_HOME` environment variable ke path JDK yang diinginkan.

### macOS

**Menggunakan alias di ~/.zshrc:**

```bash
# Add to ~/.zshrc
alias java17='export JAVA_HOME=$(/usr/libexec/java_home -v 17)'
alias java11='export JAVA_HOME=$(/usr/libexec/java_home -v 11)'
alias java8='export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)'
```

Lalu reload:
```bash
source ~/.zshrc
```

Untuk switch:
```bash
java17  # Switch to Java 17
java11  # Switch to Java 11
```

**Menggunakan SDKMAN:**
```bash
sdk use java 17.0.11-tem
sdk use java 11.0.21-tem
```

---

## 🐛 Troubleshooting

### Problem 1: `java -version` masih menunjukkan versi lama

**Solusi Windows:**
1. Pastikan `JAVA_HOME` sudah di-set dengan benar
2. Pastikan `%JAVA_HOME%\bin` ada di PATH dan di posisi paling atas
3. Restart terminal/Command Prompt
4. Jika masih gagal, restart komputer

**Solusi macOS:**
```bash
# Check all installed Java versions
/usr/libexec/java_home -V

# Force set to Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
source ~/.zshrc
```

### Problem 2: IntelliJ tidak mendeteksi JDK 17

**Solusi:**
1. Buka **File** → **Project Structure**
2. Pilih **SDKs** di panel kiri
3. Klik **+** → **Add JDK**
4. Browse ke folder instalasi JDK 17
5. Klik **OK**

### Problem 3: Gradle menggunakan Java versi lama

**Solusi:**
1. Edit `gradle.properties` di project root:
   ```properties
   org.gradle.java.home=C:/Program Files/Eclipse Adoptium/jdk-17.0.12.7-hotspot
   ```
   
   Atau untuk macOS:
   ```properties
   org.gradle.java.home=/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
   ```

2. Atau set di IntelliJ: **Settings** → **Build Tools** → **Gradle** → **Gradle JVM**

### Problem 4: Error "switch expressions" atau "case ->"

**Penyebab:** Project belum dikonfigurasi untuk Java 14+

**Solusi:** Tambahkan di `build.gradle.kts`:
```kotlin
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

Atau di `build.gradle` (Groovy):
```groovy
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

### Problem 5: PowerShell error saat menjalankan Gradle

**Error:**
```
Task '.xml' not found in root project
```

**Solusi:** Gunakan salah satu cara berikut:

```powershell
# Option 1: Use --project-prop
./gradlew clean test --project-prop suite=smoke.xml --project-prop env=staging

# Option 2: Use stop-parsing token
./gradlew --% clean test -Psuite=smoke.xml -Penv=staging

# Option 3: Use CMD instead of PowerShell
cmd
gradlew clean test -Psuite=smoke.xml -Penv=staging
```

---

## 📚 Useful Commands Summary

| Command | Windows | macOS |
|---------|---------|-------|
| Check Java version | `java -version` | `java -version` |
| Check JAVA_HOME | `echo %JAVA_HOME%` | `echo $JAVA_HOME` |
| List installed JDKs | - | `/usr/libexec/java_home -V` |
| Install via package manager | `winget install EclipseAdoptium.Temurin.17.JDK` | `brew install openjdk@17` |

---

## 🔗 Useful Links

- [Eclipse Temurin Downloads](https://adoptium.net/temurin/releases/)
- [Oracle JDK Downloads](https://www.oracle.com/java/technologies/downloads/)
- [SDKMAN](https://sdkman.io/)
- [Homebrew](https://brew.sh/)
- [IntelliJ IDEA Documentation](https://www.jetbrains.com/help/idea/sdk.html)

---

**Last Updated**: February 22, 2026  
**Version**: 1.0.0

