# 🏰 Mysore Explorer (Mysuru)

> **Discover the Royal Soul of Mysore** — A modern, interactive travel companion and tourism app for visitors to the City of Palaces (Karnataka, India).

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-purple.svg)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![Room Database](https://img.shields.io/badge/Storage-Room%20DB%20(Offline)-orange.svg)](https://developer.android.com/training/data-storage/room)

---

## ✨ Features

- **🏛️ 3D Royal Palace Hero Section**:
  - Procedural 3D perspective tilt canvas with Mysore Palace silhouette, golden kalasa, minarets, and Chamundi Hill backdrop.
  - Interactive lighting modes: *Dawn Glow*, *Golden Sunset*, and the signature *Sunday Palace Illumination* with 100,000 glowing bulbs and golden particle sparkles.
  - Real-time "Today in Mysore" weather badge & season tips.
- **🗺️ Interactive 2.5D Mysore Map**:
  - Stylized isometric canvas map with Chamundi Hill elevation contours, Cauvery River, and Outer Ring Road.
  - Category filters: Attractions, Restaurants, Hotels, Shopping, and Experiences.
  - Tap-to-inspect glass popup cards with GPS coordinates and routing.
- **✨ AI Trip Planner ("Build Your Perfect Mysore Trip")**:
  - 1 to 4+ day itinerary generator.
  - Interest customizer: Heritage, Food, Nature, Shopping, Photography, Family, Adventure.
  - Time-slotted schedules (Morning, Afternoon, Evening, Night) with insider local tips.
- **🍛 "Taste Mysore" Food Guide**:
  - Authentic Mysore Masala Dosa (Mylari), Royal Mysore Pak (origin story at Guru Sweet Mart), Filter Coffee, Maddur Vada, Mallige Idli, and Churumuri.
  - Veg badges, price estimates, and iconic eatery recommendations.
- **📍 Attractions & Audio Experience**:
  - Mysore Palace, Chamundi Hill, Brindavan Gardens, St. Philomena’s Cathedral, Devaraja Market, Karanji Lake, Mysore Zoo, Srirangapatna.
  - Timings, entry fees, best visiting hours, and a simulated royal audio narration player with animated waveforms.
- **💾 Offline Persistence with Room Database**:
  - Bookmark favorite landmarks and save custom generated trip plans for offline access during travel.

---

## 🏗️ Tech Stack

- **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
- **UI Framework:** Jetpack Compose with Material Design 3 (M3)
- **State Management:** Kotlin Coroutines & `StateFlow`
- **Local Persistence:** Android Room Database + KSP
- **Image Loading:** Coil Compose
- **Design System:** Custom Royal Mysore palette (`#090D1A` Midnight, `#FFD700` Gold, `#6B21A8` Royal Purple, `#FDF8EE` Warm Ivory) with glassmorphism cards

---

## 🚀 Getting Started

### Prerequisites
- [Android Studio Hedgehog / Ladybug or newer](https://developer.android.com/studio)
- JDK 17 or higher
- Android SDK with API 36 / 35 (Minimum SDK: 24)

### Clone & Run

```bash
git clone https://github.com/YOUR_USERNAME/mysore-explorer.git
cd mysore-explorer
```

1. Open **Android Studio**.
2. Select **Open** and choose the `mysore-explorer` directory.
3. Allow Gradle to sync dependencies.
4. Select an emulator or connected physical Android device.
5. Click **Run (`Shift + F10`)**.

---

## 📁 Project Structure

```
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/
│       │   │   ├── MainActivity.kt
│       │   │   ├── data/
│       │   │   │   ├── local/
│       │   │   │   │   └── LocalDatabase.kt          # Room Database & DAOs
│       │   │   │   ├── model/
│       │   │   │   │   └── Models.kt                 # Data Models & Initial Seeds
│       │   │   │   └── repository/
│       │   │   │       └── MysoreRepository.kt       # Data Source & Flow
│       │   │   └── ui/
│       │   │       ├── components/
│       │   │       │   ├── GlassComponents.kt        # Glassmorphic UI components
│       │   │       │   ├── InteractiveMysoreMap.kt   # 2.5D Isometric Canvas Map
│       │   │       │   └── Palace3DHeroView.kt       # Interactive 3D Palace Hero
│       │   │       ├── screens/
│       │   │       │   ├── AttractionDetailScreen.kt # Landmark details & audio guide
│       │   │       │   ├── ExploreScreen.kt          # All places & categories
│       │   │       │   ├── HomeScreen.kt             # Main discovery feed & cards
│       │   │       │   ├── MapScreen.kt              # Fullscreen interactive map
│       │   │       │   ├── SavedTripsScreen.kt       # Saved itineraries & bookmarks
│       │   │       │   ├── TasteMysoreScreen.kt      # Food guide & iconic spots
│       │   │       │   └── TripPlannerScreen.kt      # Interactive AI itinerary builder
│       │   │       ├── theme/
│       │   │       │   ├── Color.kt                  # Royal Mysore color palette
│       │   │       │   ├── Theme.kt                  # M3 Dark/Light themes
│       │   │       │   └── Type.kt                   # Typography styles
│       │   │       └── viewmodel/
│       │   │           └── MysoreViewModel.kt        # StateFlow & business logic
│       │   └── res/
│       │       ├── drawable/                         # Custom adaptive icons & graphics
│       │       └── values/strings.xml
│       └── test/                                     # Unit & Robolectric tests
├── gradle/
│   └── libs.versions.toml                            # Version Catalog
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 📄 License
This project is open source and available under the [MIT License](LICENSE).
