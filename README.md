# 📝 QuizApp — Offline MCQ Grammar Quiz

A clean, offline MCQ quiz app built with **Kotlin**, **Jetpack Compose**, and **MVVM Architecture** as part of an Android Developer Intern assignment.

---

## 📱 Screenshots

| Question Screen | Answer Selected | Result Dialog |
|:-:|:-:|:-:|
| ![Q Screen](https://via.placeholder.com/180x380/F8F7FF/6C63FF?text=Q+Screen) | ![Selected](https://via.placeholder.com/180x380/F8F7FF/6C63FF?text=Selected) | ![Result](https://via.placeholder.com/180x380/F8F7FF/6C63FF?text=Result) |

> Replace placeholder images above with actual screenshots from your phone.

---

## ✨ Features

- 📖 5 offline grammar questions — no internet needed
- ☝️ Single answer selection per question
- 📊 Progress bar showing current question (e.g. 2/5)
- ✅ Next button disabled until an answer is selected
- 🏆 Result dialog at the end with score, percentage & dynamic message
- 🔄 Try Again to restart the quiz from Q1
- 💜 Clean purple UI matching modern Material Design

---

## 🏗️ Architecture

This app follows **MVVM (Model-View-ViewModel)** architecture with strict separation of concerns.

```
com.example.quizapp/
│
├── model/
│   └── Question.kt          # Data class — pure data, no logic
│
├── data/
│   └── QuizRepository.kt    # Data source — all 5 questions live here
│
├── viewmodel/
│   └── QuizViewModel.kt     # Business logic + state management
│
└── ui/
    ├── QuizScreen.kt        # All Compose UI composables
    └── theme/
        └── Theme.kt         # Material3 color theme
```

### Why MVVM?

| Layer | Responsibility | Knows About |
|-------|---------------|-------------|
| `Question` | Holds raw data | Nothing |
| `QuizRepository` | Provides questions | `Question` only |
| `QuizViewModel` | Manages state & logic | `Repository` + `Question` |
| `QuizScreen` | Renders UI | `ViewModel` state only |

Each layer only talks to the layer directly below it. The UI never calculates the score. The ViewModel never imports Compose. The Repository never knows about UI state.

---

## 🔄 State Management

State is managed using **Kotlin StateFlow** and a **sealed class**:

```kotlin
sealed class QuizUiState {
    object Loading : QuizUiState()
    data class Active(
        val questions: List<Question>,
        val currentIndex: Int = 0,
        val selectedAnswers: Map<Int, Int> = emptyMap(),
        val showResult: Boolean = false
    ) : QuizUiState()
}
```

**Why sealed class?** — Forces exhaustive state handling. The UI must handle every possible state. No null crashes, no forgotten cases.

**Why StateFlow?** — Lifecycle-aware reactive stream. The UI observes it via `collectAsStateWithLifecycle()` which auto-pauses when app goes to background — preventing memory leaks.

**Why immutable `copy()`?** — Compose detects state changes by reference equality. Creating a new object via `copy()` guarantees Compose sees the change and recomposes only the affected parts of the UI — not the whole screen.

```kotlin
// Every state update creates a NEW object — never mutates existing state
_uiState.update { current ->
    if (current is QuizUiState.Active) {
        current.copy(
            selectedAnswers = current.selectedAnswers + (questionId to optionIndex)
        )
    } else current
}
```

---

## 🛠️ Tech Stack

| Technology | Usage |
|-----------|-------|
| **Kotlin** | Primary language |
| **Jetpack Compose** | Declarative UI — no XML layouts |
| **Material3** | UI components & theming |
| **ViewModel** | Survives configuration changes (rotation) |
| **StateFlow** | Reactive state management |
| **MVVM** | Architecture pattern |

**Minimum SDK:** API 24 (Android 7.0) — runs on ~99.2% of devices

---

## 🚀 How to Run

1. Clone the repository:
```bash
git clone https://github.com/ArunKumar73177/QuizApp.git
```

2. Open in **Android Studio** (latest stable version)

3. Let Gradle sync complete

4. Run on a physical device or emulator (API 24+)

> No API keys, no internet, no setup — just clone and run.

---

## 📐 Key Design Decisions

**Mock Repository over Room DB**
The assignment permitted a mock repo. I still wrapped data in a Repository class so swapping to Room DB in future only requires changing one file — the ViewModel remains untouched.

**Sealed class for UI state**
Rather than managing multiple boolean flags (`isLoading`, `showResult`, etc.), a single sealed class represents every possible state. This is safer and easier to extend.

**`collectAsStateWithLifecycle` over `collectAsState`**
The lifecycle-aware variant automatically cancels collection when the lifecycle owner is stopped — no manual cleanup needed.

**Disabled Next button UX**
The Next/See Results button stays disabled until an option is selected. This prevents accidental progression and guides the user clearly.

---

## 🔮 Future Improvements

- [ ] Room Database for question persistence
- [ ] Multiple quiz categories (Grammar, Vocabulary, etc.)
- [ ] Question shuffle / randomization
- [ ] Review mode — see correct answers after quiz
- [ ] High score tracking
- [ ] Dark mode support

---

## 👨‍💻 Author

**Arun Kumar**  
Android Developer Intern Assignment Submission  
April 2026

---

## 📄 License

```
MIT License — free to use and modify
```
