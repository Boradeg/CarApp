# 🚗 Tummoc Car Management App

A robust, modern Android demo application for vehicle  management built with **Jetpack Compose** and **Clean Architecture**. This project demonstrates industry-standard practices including dependency injection with **Hilt**, local persistence with **Room**, and reactive state management.

---

## 📱 Screenshots 

### Core Dashboard & Forms
| Dashboard Screen | Filter Sidebar | Add Vehicle Form |
| :---: | :---: | :---: |
| ![Dashboard](https://github.com/user-attachments/assets/5448685d-7141-4e50-a233-01cd65b5f7a5) | ![Filters](https://github.com/user-attachments/assets/01a5bb3e-86c0-4151-a836-0a2adc2d9f76) | ![Add Vehicle](https://github.com/user-attachments/assets/0716d81d-2266-4f62-b4b4-f06de7f343b1) |

### Secondary Features
| Model Selection | Brand Selection | Form Validation |
| :---: | :---: | :---: |
| ![Brand](https://github.com/user-attachments/assets/97c100b8-6f75-49a5-8179-069ecc17bb99) | ![Fuel](https://github.com/user-attachments/assets/fa0773db-14ad-428e-aabc-d282e3634d97) | ![Validation](https://github.com/user-attachments/assets/119a3850-5b09-4b0c-a8a4-8b3f2c59cac9) |


---

## 🛠 Technical Expertise & Best Practices

### **Core Android & Reactive Programming**
*   **Kotlin & Modern API:** Advanced usage of **Sealed Classes** for state modeling, **Extension Functions** for clean code, and **Scope Functions** to reduce boilerplate.
*   **Asynchronous Flow:** Expert implementation of **Kotlin Flow** for reactive data streams from the Database to the UI.
    *   **StateFlow:** Used to manage and persist UI state (Loading/Success/Error) across configuration changes.
    *   **SharedFlow:** Utilized for high-frequency, one-time UI events like Navigation and SnackBar/Toast messages.
*   **Jetpack Compose:** Declarative UI construction using Material 3, managing complex **Recomposition** cycles and state hoisting.

### **Architecture & Engineering**
*   **Clean MVVM Architecture:** Strict separation into **Data, Domain, and Presentation** layers to achieve a highly decoupled and testable codebase.
*   **Business Logic (Use Cases):** Implementation of the **Domain Layer** to handle complex business rules and validations outside of the ViewModel.
*   **Dependency Injection:** Full integration of **Hilt (Dagger)** for automated DI, providing scoped instances of Repositories and Room components.
*   **Error Handling Strategy:** Robust error management using a generic **Resource/Result** wrapper to provide a consistent UI experience during failures.

### **Data Persistence & Tooling**
*   **Local Persistence (Room):** Optimized SQLite management with **Room Database**, including complex DAOs, Entity relationships, and background thread execution via `room-ktx`.
*   **Lifecycle Management:** Usage of `lifecycle-runtime-ktx` to collect flows safely within the Compose lifecycle.


