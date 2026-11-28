package com.example.habitflow.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
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
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private val StatsBackground = Color(0xFFF5F5EF)
private val StatsGreen = Color(0xFF244A3A)
private val StatsLightGreen = Color(0xFFE2EDE4)
private val StatsText = Color(0xFF26352D)
private val StatsGray = Color(0xFF7B847D)

@Composable
fun StatisticsScreen(viewModel: HabitViewModel) {

    val habits by viewModel.habits.collectAsState()
    val completions by viewModel.completions.collectAsState()

    val today = LocalDate.now()
    val activeHabits = habits.filter { it.isActive }

    val todayCompletions = completions.filter {
        it.date == today.toString()
    }

    val completedToday = todayCompletions
        .map { it.habitId }
        .distinct()
        .size

    val totalHabits = activeHabits.size
    val remainingHabits = (totalHabits - completedToday).coerceAtLeast(0)

    val progress = if (totalHabits > 0) {
        (completedToday.toFloat() / totalHabits).coerceIn(0f, 1f)
    } else {
        0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "statisticsProgress"
    )

    val lastSevenDays = (6 downTo 0).map {
        today.minusDays(it.toLong())
    }

    val dailyCounts = lastSevenDays.map { date ->
        completions.count { it.date == date.toString() }
    }

    val weeklyTotal = dailyCounts.sum()
    val maxDailyCount = maxOf(1, dailyCounts.maxOrNull() ?: 0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(StatsBackground),
        contentPadding = PaddingValues(
            horizontal = 22.dp,
            vertical = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        item {
            Column {
                Text(
                    text = "HABITFLOW",
                    color = StatsGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Tvoje statistiky 📊",
                    color = StatsText,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Podívej se, jak se ti daří budovat návyky.",
                    color = StatsGray,
                    fontSize = 13.sp
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    title = "Návyky",
                    value = totalHabits.toString(),
                    emoji = "🎯",
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Splněno",
                    value = completedToday.toString(),
                    emoji = "✓",
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Zbývá",
                    value = remainingHabits.toString(),
                    emoji = "⏳",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = StatsGreen
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {
                    Text(
                        text = "DNEŠNÍ ÚSPĚŠNOST",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${(animatedProgress * 100).toInt()} %",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(9.dp)
                            .clip(CircleShape),
                        color = Color(0xFFA7D8B5),
                        trackColor = Color.White.copy(alpha = 0.18f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$completedToday z $totalHabits návyků splněno",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Týdenní aktivita",
                        color = StatsText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "$weeklyTotal zaznamenaných splnění",
                        color = StatsGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        lastSevenDays.forEachIndexed { index, date ->

                            val count = dailyCounts[index]
                            val fraction = count.toFloat() / maxDailyCount

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = count.toString(),
                                    color = StatsGray,
                                    fontSize = 10.sp
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(22.dp)
                                            .height((fraction * 85f + 4f).dp)
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 7.dp,
                                                    topEnd = 7.dp
                                                )
                                            )
                                            .background(
                                                if (date == today) {
                                                    StatsGreen
                                                } else {
                                                    StatsLightGreen
                                                }
                                            )
                                    )
                                }

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = date.dayOfWeek.getDisplayName(
                                        TextStyle.SHORT,
                                        Locale.forLanguageTag("cs-CZ")
                                    ).replaceFirstChar {
                                        it.uppercase(Locale.forLanguageTag("cs-CZ"))
                                    },
                                    color = if (date == today) {
                                        StatsGreen
                                    } else {
                                        StatsGray
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = if (date == today) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Přehled aktivity",
                        color = StatsText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    StatisticRow(
                        label = "Splnění za posledních 7 dní",
                        value = weeklyTotal.toString()
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = StatsBackground
                    )

                    StatisticRow(
                        label = "Aktivní návyky",
                        value = totalHabits.toString()
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = StatsBackground
                    )

                    StatisticRow(
                        label = "Dnešní nesplněné návyky",
                        value = remainingHabits.toString()
                    )
                }
            }
        }

        item {
            if (weeklyTotal == 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = StatsLightGreen
                    )
                ) {
                    Text(
                        text = "🌱 Začni plnit své návyky a brzy zde uvidíš svůj pokrok.",
                        modifier = Modifier.padding(18.dp),
                        color = StatsGreen,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = emoji,
                fontSize = 21.sp
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = value,
                color = StatsText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                color = StatsGray,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun StatisticRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = StatsGray,
            fontSize = 13.sp
        )

        Text(
            text = value,
            color = StatsGreen,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}