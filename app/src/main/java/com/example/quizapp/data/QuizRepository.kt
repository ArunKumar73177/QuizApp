package com.example.quizapp.data

import com.example.quizapp.model.Question


class QuizRepository {

    fun getQuestions(): List<Question> = listOf(
        Question(
            id = 1,
            text = "What is the definition of a noun in English grammar?",
            options = listOf(
                "A verb that describes an action",
                "A word that modifies a verb",
                "A word that names a person, place, or thing",
                "A connecting word between clauses"
            ),
            correctAnswerIndex = 2
        ),
        Question(
            id = 2,
            text = "Which sentence uses the correct form of \"their / there / they're\"?",
            options = listOf(
                "Their going to the park.",
                "There car is very new.",
                "They're at the store right now.",
                "Their is no way out."
            ),
            correctAnswerIndex = 2
        ),
        Question(
            id = 3,
            text = "Which word correctly completes: \"I ___ to the store yesterday.\"",
            options = listOf(
                "go",
                "goes",
                "going",
                "went"
            ),
            correctAnswerIndex = 3
        ),
        Question(
            id = 4,
            text = "Identify the grammatically correct sentence:",
            options = listOf(
                "Neither of the boys were ready.",
                "Neither of the boys was ready.",
                "Neither of the boys are ready.",
                "Neither of the boys be ready."
            ),
            correctAnswerIndex = 1
        ),
        Question(
            id = 5,
            text = "Which sentence uses the Oxford comma correctly?",
            options = listOf(
                "I bought apples, oranges and bananas.",
                "I bought apples oranges, and bananas.",
                "I bought apples, oranges, and bananas.",
                "I bought, apples, oranges and bananas."
            ),
            correctAnswerIndex = 2
        )
    )
}