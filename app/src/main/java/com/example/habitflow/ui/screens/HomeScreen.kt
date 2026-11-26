package com.example.habitflow.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow.viewmodel.HabitViewModel

private val Background = Color(0xFFF5F5EF)
private val DarkGreen = Color(0xFF244A3A)
private val LightGreen = Color(0xFFE2EDE4)
private val TextDark = Color(0xFF26352D)
private val TextGray = Color(0xFF7B847D)

@Composable
fun HomeScreen(viewModel: HabitViewModel) {

    val habits by viewModel.habits.collectAsState()

    val activeHabits = habits.filter { it.isActive }
    val completedCount = activeHabits.count { it.isCompleted }

    val progress = if (activeHabits.isEmpty()) {
        0f
    } else {
        completedCount.toFloat() / activeHabits.size
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "dailyProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 22.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "HABITFLOW",
            color = DarkGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Dobrý den! 👋",
            color = TextDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Každý den je nová příležitost.",
            color = TextGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(26.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkGreen
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                Text(
                    text = "TVŮJ DNEŠNÍ POKROK",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = "$completedCount / ${activeHabits.size}",
                            color = Color.White,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "splněných návyků",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            color = Color.White,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = Color(0xFFA7D8B5),
                    trackColor = Color.White.copy(alpha = 0.18f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = when {
                        activeHabits.isEmpty() ->
                            "Zatím nemáš žádné aktivní návyky."

                        progress == 1f ->
                            "Skvělá práce! Všechny návyky splněny 🎉"

                        else ->
                            "Pokračuj, jde ti to skvěle!"
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Dnešní návyky",
                color = TextDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Surface(
                color = LightGreen,
                shape = RoundedCornerShape(20.dp)
            ) {

                Text(
                    text = "${activeHabits.size} celkem",
                    color = DarkGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (activeHabits.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "🌱",
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Začni budovat své návyky",
                        color = TextDark,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Přidej svůj první návyk v záložce Návyky.",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                }
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(11.dp),
                modifier = Modifier.weight(1f)
            ) {

                items(
                    items = activeHabits,
                    key = { it.id }
                ) { habit ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (habit.isCompleted) {
                                Color(0xFFF0F5F0)
                            } else {
                                Color.White
                            }
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 14.dp,
                                    vertical = 13.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(LightGreen),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = habit.emoji,
                                    fontSize = 25.sp
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 13.dp)
                            ) {

                                Text(
                                    text = habit.name,
                                    color = if (habit.isCompleted) {
                                        DarkGreen
                                    } else {
                                        TextDark
                                    },
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                if (habit.description.isNotBlank()) {

                                    Spacer(
                                        modifier = Modifier.height(3.dp)
                                    )

                                    Text(
                                        text = habit.description,
                                        color = TextGray,
                                        fontSize = 12.sp
                                    )
                                }

                                if (!habit.reminderTime.isNullOrBlank()) {

                                    Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = "Připomínka",
                                            tint = DarkGreen,
                                            modifier = Modifier.size(13.dp)
                                        )

                                        Spacer(
                                            modifier = Modifier.width(4.dp)
                                        )

                                        Text(
                                            text = habit.reminderTime,
                                            color = DarkGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Checkbox(
                                checked = habit.isCompleted,
                                onCheckedChange = {
                                    viewModel.toggleHabitCompletion(habit)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = DarkGreen,
                                    uncheckedColor = Color(0xFFB7C0B8),
                                    checkmarkColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}