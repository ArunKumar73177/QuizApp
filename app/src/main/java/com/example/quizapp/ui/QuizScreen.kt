package com.example.quizapp.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.viewmodel.QuizUiState
import com.example.quizapp.viewmodel.QuizViewModel
import kotlin.math.PI


private val Purple       = Color(0xFF6C63FF)
private val PurpleLight  = Color(0xFFEDE9FF)
private val PurpleMuted  = Color(0xFFC4C0FF)
private val Background   = Color(0xFFF8F7FF)
private val CardBorder   = Color(0xFFE0DEFF)
private val TextDark     = Color(0xFF1A1A2E)
private val TextMuted    = Color(0xFF999999)
private val White        = Color(0xFFFFFFFF)


@Composable
fun QuizScreen(viewModel: QuizViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when (val state = uiState) {
            is QuizUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Purple
                )
            }
            is QuizUiState.Active -> {

                QuizContent(
                    state = state,
                    onOptionSelected = viewModel::onOptionSelected,
                    onNextClicked = viewModel::onNextClicked
                )

                if (state.showResult) {
                    ResultDialog(
                        score = viewModel.calculateScore(state),
                        total = state.questions.size,
                        onRestart = viewModel::restartQuiz,
                        onDismiss = viewModel::restartQuiz
                    )
                }
            }
        }
    }
}


@Composable
fun QuizContent(
    state: QuizUiState.Active,
    onOptionSelected: (Int, Int) -> Unit,
    onNextClicked: () -> Unit
) {
    val currentQuestion = state.questions[state.currentIndex]
    val selectedIndex   = state.selectedAnswers[currentQuestion.id]
    val progress        = (state.currentIndex + 1).toFloat() / state.questions.size
    val isLastQuestion  = state.currentIndex == state.questions.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 32.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Purple,
                trackColor = PurpleLight
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${state.currentIndex + 1}/${state.questions.size}",
                color = Purple,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Surface(
            shape = RoundedCornerShape(20.dp),
            color = PurpleLight
        ) {
            Text(
                text = "Grammar Quiz",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                color = Purple,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))


        AnimatedContent(
            targetState = state.currentIndex,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it } + fadeOut())
            },
            label = "question_anim"
        ) { index ->
            val question = state.questions[index]
            Text(
                text = question.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                lineHeight = 27.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))


        AnimatedContent(
            targetState = state.currentIndex,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it } + fadeOut())
            },
            label = "options_anim"
        ) { index ->
            val question = state.questions[index]
            val selected = state.selectedAnswers[question.id]

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                question.options.forEachIndexed { optionIndex, optionText ->
                    OptionCard(
                        letter = ('A' + optionIndex).toString(),
                        text = optionText,
                        isSelected = selected == optionIndex,
                        onClick = { onOptionSelected(question.id, optionIndex) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNextClicked,
            enabled = selectedIndex != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                disabledContainerColor = PurpleMuted,
                contentColor = White,
                disabledContentColor = White
            )
        ) {
            Text(
                text = if (isLastQuestion) "See Results" else "Next →",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun OptionCard(
    letter: String,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PurpleLight else White,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) Purple else CardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = if (isSelected) Purple else White,
                border = if (isSelected) null
                else BorderStroke(1.5.dp, CardBorder),
                modifier = Modifier.size(32.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = letter,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) White else Purple
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                color = TextDark,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ResultDialog(
    score: Int,
    total: Int,
    onRestart: () -> Unit,
    onDismiss: () -> Unit
) {
    val percentage = if (total > 0) (score * 100) / total else 0
    val message = when {
        percentage == 100 -> "Perfect Score! 🎉"
        percentage >= 80  -> "Great Work!"
        percentage >= 60  -> "Good Effort!"
        else              -> "Keep Practicing!"
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { score.toFloat() / total },
                        modifier = Modifier.fillMaxSize(),
                        color = Purple,
                        trackColor = PurpleLight,
                        strokeWidth = 8.dp
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "/ $total",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = message,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You scored $percentage% on the Grammar Quiz",
                    fontSize = 13.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, Purple),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Purple)
                    ) {
                        Text("Review", fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = onRestart,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple)
                    ) {
                        Text("Try Again", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}