package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.QuizRepository
import com.example.quizapp.model.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


sealed class QuizUiState {
    object Loading : QuizUiState()

    data class Active(
        val questions: List<Question>,
        val currentIndex: Int = 0,
        val selectedAnswers: Map<Int, Int> = emptyMap(), // questionId -> optionIndex
        val showResult: Boolean = false
    ) : QuizUiState()
}


class QuizViewModel(
    private val repository: QuizRepository = QuizRepository()
) : ViewModel() {


    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)


    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {

        loadQuestions()
    }

    private fun loadQuestions() {
        val questions = repository.getQuestions()
        _uiState.value = QuizUiState.Active(questions = questions)
    }


    fun onOptionSelected(questionId: Int, optionIndex: Int) {
        _uiState.update { current ->
            if (current is QuizUiState.Active) {
                current.copy(
                    selectedAnswers = current.selectedAnswers + (questionId to optionIndex)
                )
            } else current
        }
    }


    fun onNextClicked() {
        _uiState.update { current ->
            if (current is QuizUiState.Active) {
                val isLastQuestion = current.currentIndex == current.questions.lastIndex
                current.copy(
                    currentIndex = if (isLastQuestion) current.currentIndex
                    else current.currentIndex + 1,
                    showResult = isLastQuestion
                )
            } else current
        }
    }


    fun calculateScore(state: QuizUiState.Active): Int {
        return state.questions.count { question ->
            state.selectedAnswers[question.id] == question.correctAnswerIndex
        }
    }


    fun restartQuiz() {
        loadQuestions()
    }
}