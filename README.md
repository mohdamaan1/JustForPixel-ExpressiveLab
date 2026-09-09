# 🚀 JustForPixel ExpressiveLab M3
### Publishable Open-Source Android Material 3 Expressive Component & Motion Library

<p align="center">
  <a href="https://jitpack.io/#com.github.ermohdamaan/expressivelab"><img src="https://jitpack.io/v/com.github.ermohdamaan/expressivelab.svg" alt="JitPack Release"></a>
  <img src="https://img.shields.io/badge/Kotlin-2.2.10-blue.svg?logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3_Expressive-purple.svg?logo=android" alt="Material 3 Expressive">
  <img src="https://img.shields.io/badge/Min_SDK-24-green.svg" alt="Min SDK 24">
  <img src="https://img.shields.io/badge/Compile_SDK-37-blue.svg" alt="Compile SDK 37">
  <img src="https://img.shields.io/badge/License-MIT-orange.svg" alt="License">
</p>
 📸 Components Showcase
 
 ---

<p align="center">
  <img src="https://github.com/user-attachments/assets/75946453-fcca-47e6-bfd9-46e81c82ec01" width="220" alt="Showcase 01"/>
  <img src="https://github.com/user-attachments/assets/5be3aad1-12b0-4d20-ae35-9035b154d352" width="220" alt="Showcase 02"/>
  <img src="https://github.com/user-attachments/assets/9286e8a2-e2a8-44f3-b20e-1885cf86ada8" width="220" alt="Showcase 03"/>
  <img src="https://github.com/user-attachments/assets/a4154b26-6534-48fe-b6c8-8722a76a65e8" width="220" alt="Showcase 04"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/37743e35-1d6f-4b6a-83c1-a4e3938f2bf1" width="220" alt="Showcase 05"/>
  <img src="https://github.com/user-attachments/assets/b7505157-9d0b-4f78-a600-9fef3d1cd55b" width="220" alt="Showcase 06"/>
  <img src="https://github.com/user-attachments/assets/a86d0f09-1309-476b-a6a6-697a8503ffb6" width="220" alt="Showcase 07"/>
  <img src="https://github.com/user-attachments/assets/781246ef-4412-4781-82a4-074491afcb21" width="220" alt="Showcase 08"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/a943686c-4da7-4c11-a9b3-649b44b0fb9e" width="220" alt="Showcase 09"/>
  <img src="https://github.com/user-attachments/assets/f13f31f6-6f71-43e8-a480-1df9d9da648d" width="220" alt="Showcase 10"/>
  <img src="https://github.com/user-attachments/assets/80777290-933c-4f27-950a-f03e923170e9" width="220" alt="Showcase 11"/>
  <img src="https://github.com/user-attachments/assets/28821e71-e4de-4f8e-95cf-97b9e3cc4545" width="220" alt="Showcase 12"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/91f24ee6-fd00-4b2f-a2e1-bd0d05ead328" width="220" alt="Showcase 13"/>
  <img src="https://github.com/user-attachments/assets/47f3bf32-1231-4190-b8cf-6fe9984a30e0" width="220" alt="Showcase 14"/>
  <img src="https://github.com/user-attachments/assets/13c08872-e94f-4e18-a2c7-f4df2a9edd75" width="220" alt="Showcase 15"/>
  <img src="https://github.com/user-attachments/assets/3c8486b9-1072-4a7b-bef1-7c4662525351" width="220" alt="Showcase 16"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/88fd5797-5825-490b-886a-514efb8bc0fb" width="220" alt="Showcase 17"/>
  <img src="https://github.com/user-attachments/assets/ffca3474-b620-415a-8e32-c76cb90ea889" width="220" alt="Showcase 18"/>
  <img src="https://github.com/user-attachments/assets/f0b08912-0d04-4e45-81ed-259d18c65e20" width="220" alt="Showcase 19"/>
</p>

---

## 📌 Executive Project Vision & Overview
**JustForPixel ExpressiveLab** is a high-performance, open-source **Material 3 Expressive UI & Motion Component Library** for Jetpack Compose, architected and maintained by **Er. Mohd Amaan** (`justforpixel`).

It provides a rich set of tactile, bouncy spring-animated UI components, organic squircle shape geometries (`AbsoluteSmoothCornerShape`), oscillating wavy sliders, expanding floating navigation bars, Google Material 3 Expressive loading spinners, and audio visualizers ready for production Android apps.

---

## 🛠️ Project Architecture (2-Module Structure)

```
JustForPixel ExpressiveLab Root
 ├── 📦 :expressivelab  ---> Android Library Module (com.github.ermohdamaan:expressivelab:1.0.0)
 │    ├── 🧩 components/  (Push Buttons, Morphing Icons, Wavy Sliders, Floating Bars, Progress, Rings, Equalizers, Cards)
 │    └── 🎨 theme/       (TactileMotionTokens, AbsoluteSmoothCornerShape, ExpressiveColorScheme)
 └── 📱 :app            ---> Flagship Interactive Showcase Application (com.ermohdamaan.justforpixel.justforpixelexpressivelab.app)
      ├── 🎬 Splash      (Shape-Morphing Progress & Intro)
      ├── 📑 Catalog     (Interactive Grid with 10 Component Playground Categories)
      └── ⚙️ Settings    (Theme Mode, Pure OLED Black #000000, Copy Dependency, Haptics)
```

---

## 💻 Installation & Gradle Setup

> [!NOTE]  
> The `:expressivelab` library is published via **JitPack**. You can integrate it into any Android Jetpack Compose project targeting `minSdk = 24` or higher.

### Step 1: Add JitPack Repository in `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add Dependency in Module `build.gradle.kts`

```kotlin
dependencies {
    // JustForPixel ExpressiveLab Material 3 Expressive Component Library
    implementation("com.github.ermohdamaan:expressivelab:1.0.0")

    // Compose Material 3 Expressive BOM
    implementation(platform("androidx.compose:compose-bom:2026.02.01"))
    implementation("androidx.compose.material3:material3")
}
```

---

## 🌟 Included Material 3 Expressive Components & Code Usage

### 1. Elastic Push Controls (`ExpressivePushButtonRow`)
Elastic layout weight expansion (`1.35f` expansion vs `0.55f` compression) with neighbor push (*dhakka*) physics on tap.

```kotlin
var isPlaying by remember { mutableStateOf(false) }

ExpressivePushButtonRow(
    isPlaying = isPlaying,
    onPrevious = { /* Previous track */ },
    onPlayPause = { isPlaying = !isPlaying },
    onNext = { /* Next track */ },
    modifier = Modifier.fillMaxWidth()
)
```

---

### 2. Standalone Morphing Icon Button (`MorphingIconButton`)
Bouncy press physics (`scale = 0.88f`) with corner morphing between Circle, Squircle, and Pill shapes.

```kotlin
var favActive by remember { mutableStateOf(true) }

MorphingIconButton(
    onClick = { favActive = !favActive },
    icon = Icons.Rounded.Favorite,
    contentDescription = "Favorite",
    active = favActive
)
```

---

### 3. Centered Floating Navigation Bar (`ExpressiveFloatingBottomBar`)
Material 3 Expressive floating pill navigation bar with active label expansion (Icon + Text for active tab, Icon-only for inactive tabs) and zero shadow artifacts.

```kotlin
var selectedTab by remember { mutableIntStateOf(0) }

ExpressiveFloatingBottomBar(
    selectedTab = selectedBottomTab,
    onTabSelected = { selectedBottomTab = it }
)
```

---

### 4. Expressive Wavy Slider (`ExpressiveWavySlider`) & Arc Slider (`ExpressiveArcSlider`)
Oscillating wave animation with dynamic gap modulation, thumb line expansion, and 180° curved arc seeking.

```kotlin
var sliderValue by remember { mutableFloatStateOf(0.5f) }

ExpressiveWavySlider(
    value = { sliderValue },
    onValueChange = { sliderValue = it },
    isPlaying = true,
    modifier = Modifier.fillMaxWidth()
)
```

---

### 5. Shape-Morph Loading Indicators (`ExpressiveShapeMorphLoadingIndicator`)
Continuous shape interpolation (Circle ⇄ Squircle ⇄ Pill) combined with 360° spin rotation for expressive loading states.

```kotlin
ExpressiveShapeMorphLoadingIndicator(
    modifier = Modifier.size(64.dp),
    color = MaterialTheme.colorScheme.primary
)
```

---

### 6. Audio Frequency Equalizer Bar (`PlayingEqIcon`)
Jumping frequency bar icon that smoothly interpolates between animated audio bars and static paused dots.

```kotlin
PlayingEqIcon(
    isPlaying = true,
    modifier = Modifier.size(28.dp),
    color = MaterialTheme.colorScheme.primary
)
```

---

### 7. Bouncy Squircle Card (`ExpressiveCard`)
Tactile press elevation physics with continuous curvature squircle geometry (`AbsoluteSmoothCornerShape`).

```kotlin
ExpressiveCard(
    onClick = { /* Card tap action */ },
    modifier = Modifier.fillMaxWidth()
) {
    Text(text = "Tactile Squircle Card")
}
```

---

## 🎨 UI/UX Theme & OLED Pitch Black Support

The application includes an **OLED Pitch Black (#000000)** theme mode, dynamic Material You color extraction, and edge-to-edge system bar integration:

> [!TIP]  
> In Dark Mode, enabling **OLED Pitch Black** sets `background` and `surface` colors to pure `#000000` black for maximum energy savings on AMOLED displays.

---

## ⚖️ Open Source License & Credit Policy

> [!IMPORTANT]  
> **Mandatory Attribution Policy:**  
> This library is 100% open-source under the **MIT License**. You are free to use it in open-source and commercial Android applications provided that you retain credit to **Er. Mohd Amaan (JustForPixel)** in your project README or credits section.

**Author & Maintainer:** Er. Mohd Amaan  
**Developer Account:** `justforpixel` / `ermohdamaan`  
**Contact & Business Inquiries:** `business.amaan0@gmail.com`
