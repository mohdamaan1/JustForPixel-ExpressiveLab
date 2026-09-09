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
