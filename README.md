# Android Currency Tracker

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-7F52FF?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-4285F4?style=for-the-badge&logo=jetpackcompose)
![Android](https://img.shields.io/badge/Android-Native-3DDC84?style=for-the-badge&logo=android)

A clean, modern, and lightweight Android application for tracking the real-time prices of gold, global currencies, and cryptocurrencies. This project serves as a portfolio piece demonstrating proficiency in modern Android development technologies.

---

### Screenshots

A simple, tab-based UI allows users to quickly switch between different asset classes.

| Gold Prices | Currency Prices | Crypto Prices |
| :---: | :---: | :---: |
| <img src="screenshots/screen_gold.jpg" width="220"/> | <img src="screenshots/screen_currency.jpg" width="220"/> | <img src="screenshots/screen_crypto.jpg" width="220"/> |

*(Note: The UI is in Persian, demonstrating the ability to build for a specific locale.)*

---

## 🌟 Key Features

-   **Real-Time Data:** Fetches the latest prices for gold, currencies, and cryptocurrencies from a public API.
-   **Modern UI:** A clean, single-screen interface built with Jetpack Compose and Material 3, following modern design principles.
-   **Tabbed Navigation:** Easily switch between Gold, Currency, and Crypto views.
-   **Efficient Data Fetching:** Data for all tabs is fetched in a single, efficient network call upon startup.
-   **Custom Splash Screen:** A smooth startup experience with both a system and a custom splash screen.

---

## 🛠️ Tech Stack & Architecture

This project showcases a modern Android architecture and best practices.

-   **UI:** Built 100% with **Jetpack Compose** for a declarative and reactive user interface.
-   **Language:** Written entirely in **Kotlin**.
-   **Networking:** Uses **Retrofit 2** for efficient and clean handling of REST API calls.
-   **Asynchronous Programming:** Leverages **Kotlin Coroutines** (via `LaunchedEffect`) for non-blocking network requests.
-   **Architecture:** Follows a clear **Repository Pattern** to separate data sources from the UI logic. The repository is implemented as a **Singleton** for efficient resource management.
-   **Data Parsing:** Uses **Gson** for seamless serialization and deserialization of JSON data.

---

## 🚀 Setup & Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/Android-Currency-Tracker.git
    ```
2.  **Open in Android Studio:**
    -   Open the project in the latest version of Android Studio.
    -   Let Gradle sync and download the required dependencies.
3.  **Build and Run:**
    -   Build the project and run it on an Android emulator or a physical device.

---

### API Source

The data is fetched from the public API provided by `daneshjooyar.com`, which sources its data from `tgju.org`.