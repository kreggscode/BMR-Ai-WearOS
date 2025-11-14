# 🎯 BMI Calculator - AI Powered for Wear OS

<div align="center">

![Wear OS](https://img.shields.io/badge/Wear%20OS-3.0+-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![AI Powered](https://img.shields.io/badge/AI-Powered-FF6B6B?style=for-the-badge)

**A beautiful, modern BMI calculator app for Wear OS with AI-powered health insights, diet plans, and meal suggestions.**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Screenshots](#-screenshots) • [Installation](#-installation) • [Usage](#-usage) • [Contributing](#-contributing)

</div>

---

## ✨ Features

### 🧮 Core Functionality
- **BMI Calculation**: Quick and accurate BMI calculation with support for both metric (cm/kg) and imperial (feet/inches, lbs) units
- **Real-time Updates**: Smooth slider-based input with instant calculations
- **Visual Progress Indicator**: Beautiful animated circular progress indicator showing BMI value and category

### 🤖 AI-Powered Insights
- **Health Analysis**: AI-generated personalized health analysis based on your BMI
- **Diet Plans**: Category-based diet plans (Weight Loss, Weight Gain, Muscle Gain, Maintenance)
- **Meal Plans**: Personalized meal suggestions for different goals
- **AI Chat**: Interactive chat with AI health assistant for personalized advice

### 📊 Tracking & History
- **BMI History**: Track your BMI over time with beautiful parabolic trend graphs
- **Statistics**: View your BMI statistics and progress
- **Data Persistence**: Save and review your BMI records

### 🎨 Beautiful UI/UX
- **Dark & Light Mode**: Seamless theme switching with beautiful color schemes
- **Glowing Animations**: Stunning gradient buttons with pulsing glow effects
- **Watch-Optimized**: Fully optimized for both circular and square Wear OS watches
- **Full-Screen Experience**: Immersive full-screen design with no status bars
- **Smooth Animations**: Fluid transitions and animations throughout the app

### 🔧 Additional Features
- **Standalone App**: Works independently without requiring a phone connection
- **Unit System Toggle**: Easy switching between metric and imperial units
- **Beautiful Loading Indicators**: Animated loading states for AI operations
- **Splash Screen**: Elegant animated splash screen on app launch

---

## 🛠️ Tech Stack

### Core Technologies
- **Language**: [Kotlin](https://kotlinlang.org/) - Modern, concise, and safe programming language
- **Platform**: [Wear OS](https://wearos.google.com/) - Google's smartwatch platform
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit
- **Architecture**: MVVM (Model-View-ViewModel) - Clean architecture pattern

### Libraries & Dependencies

#### Wear OS Compose
```kotlin
implementation("androidx.wear.compose:compose-material:1.3.0")
implementation("androidx.wear.compose:compose-foundation:1.3.0")
implementation("androidx.wear.compose:compose-navigation:1.3.0")
```

#### Jetpack Compose
```kotlin
implementation("androidx.compose.ui:ui:1.6.0")
implementation("androidx.compose.material3:material3:1.2.0")
implementation("androidx.compose.animation:animation:1.6.0")
```

#### Architecture Components
```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
```

#### Networking
```kotlin
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
```

#### Coroutines
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

### AI Integration
- **AI Service**: [Pollinations.AI](https://pollinations.ai/) - For generating health insights, diet plans, and chat responses
- **API Method**: RESTful API with JSON POST requests
- **Fallback System**: Intelligent placeholder data when AI service is unavailable

### Design Patterns
- **MVVM Architecture**: Separation of concerns with ViewModel managing business logic
- **Composition**: Reusable composable components
- **State Management**: Reactive state management with Compose State
- **Dependency Injection**: Manual dependency injection (can be upgraded to Hilt/Dagger)

---

## 📱 Screenshots

### Main Features
- **Calculator Screen**: Input height and weight with beautiful sliders
- **Results Screen**: Animated circular progress indicator with BMI category
- **AI Analysis**: Personalized health insights and recommendations
- **Diet Plans**: Category-based diet recommendations
- **Meal Plans**: Personalized meal suggestions
- **History Tracker**: Beautiful trend graphs showing BMI progress over time

### UI Highlights
- ✨ Glowing gradient buttons with smooth animations
- 🎨 Dark and light mode support
- 📊 Beautiful data visualizations
- 🔄 Smooth transitions and animations
- 📱 Optimized for all Wear OS watch shapes

---

## 🚀 Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Wear OS emulator or physical device (API 30+)
- Android SDK with Wear OS support

### Build Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/kreggscode/BMR-Ai-WearOS.git
   cd BMR-Ai-WearOS
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Run on Device/Emulator**
   - Connect a Wear OS device or start an emulator
   - Click "Run" or press `Shift + F10`
   - Select your target device

### Building Release APK/AAB

```bash
# Build release AAB (recommended for Play Store)
.\gradlew.bat bundleRelease

# Build release APK
.\gradlew.bat assembleRelease
```

Output location: `app/build/outputs/bundle/release/` or `app/build/outputs/apk/release/`

---

## 💻 Usage

### Basic Workflow

1. **Launch App**: Open the app on your Wear OS watch
2. **Input Data**: 
   - Use sliders to set height and weight
   - Toggle between metric/imperial units
   - Switch between dark/light mode
3. **Calculate BMI**: Tap "CALCULATE BMI" button
4. **View Results**: See your BMI with animated circular progress indicator
5. **Explore Features**:
   - Get AI-powered health analysis
   - View personalized diet plans
   - Check meal suggestions
   - Track your BMI history
   - Chat with AI health assistant

### Unit Systems

**Metric System:**
- Height: Centimeters (cm)
- Weight: Kilograms (kg)

**Imperial System:**
- Height: Feet and Inches (e.g., 5'8")
- Weight: Pounds (lbs)

### AI Features

- **Health Analysis**: Tap "🤖 AI ANALYSIS" to get personalized insights
- **Diet Plans**: Select a category (Weight Loss, Gain, etc.) and view recommendations
- **Meal Plans**: Choose a meal category and get personalized meal suggestions
- **AI Chat**: Use quick question buttons to interact with AI health assistant

---

## 📁 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/kreggscode/wearosbmi/
│   │   │   ├── MainActivity.kt          # Main activity with navigation
│   │   │   ├── SplashActivity.kt        # Animated splash screen
│   │   │   ├── model/                   # Data models
│   │   │   │   ├── BMIData.kt
│   │   │   │   ├── BMICategory.kt
│   │   │   │   └── BMIRecord.kt
│   │   │   ├── viewmodel/               # ViewModels
│   │   │   │   └── BMIViewModel.kt
│   │   │   ├── ui/
│   │   │   │   ├── screens/             # Screen composables
│   │   │   │   │   ├── CalculatorScreen.kt
│   │   │   │   │   ├── ResultsScreen.kt
│   │   │   │   │   ├── AnalysisScreen.kt
│   │   │   │   │   ├── DietPlansScreen.kt
│   │   │   │   │   ├── MealPlansScreen.kt
│   │   │   │   │   ├── ChatWithAIScreen.kt
│   │   │   │   │   └── TrackerScreen.kt
│   │   │   │   ├── components/          # Reusable components
│   │   │   │   │   ├── BMISlider.kt
│   │   │   │   │   ├── CircularProgress.kt
│   │   │   │   │   ├── GradientButton.kt
│   │   │   │   │   ├── UnitSystemToggle.kt
│   │   │   │   │   ├── BeautifulLoadingIndicator.kt
│   │   │   │   │   └── ThemeToggle.kt
│   │   │   │   └── theme/               # Theme and colors
│   │   │   │       ├── Color.kt
│   │   │   │       └── Theme.kt
│   │   │   ├── network/                 # Network layer
│   │   │   │   └── AIService.kt
│   │   │   └── utils/                  # Utility classes
│   │   │       ├── BMICalculator.kt
│   │   │       └── AnalysisGenerator.kt
│   │   ├── res/                         # Resources
│   │   │   ├── drawable/               # Drawables
│   │   │   ├── mipmap-*/               # App icons
│   │   │   └── values/                 # Strings, styles, colors
│   │   └── AndroidManifest.xml
│   └── test/                            # Unit tests
└── build.gradle.kts                     # App-level build config
```

---

## 🎨 Design Philosophy

### Visual Design
- **Modern & Clean**: Minimalist design with focus on content
- **Glowing Effects**: Beautiful gradient buttons with pulsing animations
- **Smooth Animations**: Fluid transitions and micro-interactions
- **Watch-Optimized**: Designed specifically for small circular/square screens

### Color Scheme
- **Dark Mode**: Deep blues and purples with vibrant accent colors
- **Light Mode**: High contrast whites and blacks for maximum visibility
- **Accent Colors**: Dynamic colors based on BMI category (blue, green, yellow, red)

### User Experience
- **Intuitive Navigation**: Swipe-based navigation native to Wear OS
- **Quick Actions**: Large, easy-to-tap buttons optimized for watch screens
- **Visual Feedback**: Clear animations and transitions for user actions
- **Accessibility**: High contrast colors and readable fonts

---

## 🔧 Configuration

### Package Name
```
com.kreggscode.wearosbmi
```

### Minimum SDK
- **Min SDK**: 30 (Android 11)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

### Standalone App
The app is configured as a standalone Wear OS app, meaning it works independently without requiring a phone connection.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 🙏 Acknowledgments

- **Pollinations.AI** - For providing AI-powered text generation
- **Google Wear OS Team** - For excellent documentation and tools
- **Jetpack Compose Team** - For the amazing UI framework
- **Kotlin Team** - For the beautiful programming language

---

## 📞 Contact & Support

- **GitHub**: [kreggscode](https://github.com/kreggscode)
- **Repository**: [BMR-Ai-WearOS](https://github.com/kreggscode/BMR-Ai-WearOS)

---

## 🚀 Future Enhancements

- [ ] Data persistence with Room database
- [ ] Cloud sync for BMI history
- [ ] Additional health metrics (BMR, TDEE, etc.)
- [ ] Widget support for quick BMI access
- [ ] Wear OS complications
- [ ] Multi-language support
- [ ] Advanced analytics and insights
- [ ] Integration with Google Fit

---

<div align="center">

**Made with ❤️ for Wear OS**

⭐ Star this repo if you find it helpful!

</div>
