package com.example.habitflow.ui.screens

import android.app.TimePickerDialog
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.habitflow.data.local.HabitEntity
import com.example.habitflow.viewmodel.HabitViewModel
import java.util.Calendar
import java.util.Locale

private val HabitsBackground = Color(0xFFF5F5EF)
private val HabitsGreen = Color(0xFF244A3A)
private val HabitsLightGreen = Color(0xFFE2EDE4)
private val HabitsText = Color(0xFF26352D)
private val HabitsGray = Color(0xFF7B847D)
private val HabitsRed = Color(0xFFB84A4A)

@Composable
fun HabitsScreen(
    habitViewModel: HabitViewModel
) {
    val habits by habitViewModel.habits.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<HabitEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HabitsBackground)
            .padding(horizontal = 22.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "HABITFLOW",
            color = HabitsGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Moje návyky",
            color = HabitsText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Malé kroky vedou k velkým změnám.",
            color = HabitsGray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = {
                editingHabit = null
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HabitsGreen
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            )
        ) {
            Text(
                text = "+  Přidat nový návyk",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Všechny návyky",
                color = HabitsText,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Surface(
                color = HabitsLightGreen,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "${habits.size} celkem",
                    color = HabitsGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(
                        horizontal = 11.dp,
                        vertical = 6.dp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(13.dp))

        if (habits.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
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
                    Text("🌱", fontSize = 34.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Zatím nemáš žádné návyky",
                        color = HabitsText,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Přidej svůj první návyk a začni.",
                        color = HabitsGray,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(
                    items = habits,
                    key = { it.id }
                ) { habit ->
                    HabitCard(
                        habit = habit,
                        onEdit = {
                            editingHabit = habit
                            showDialog = true
                        },
                        onDelete = {
                            habitViewModel.deleteHabit(habit)
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        HabitDialog(
            habit = editingHabit,
            onDismiss = {
                showDialog = false
                editingHabit = null
            },
            onSave = { name, description, emoji, repeatType, reminderTime ->
                if (editingHabit == null) {
                    habitViewModel.addHabit(
                        name = name,
                        description = description,
                        emoji = emoji,
                        repeatType = repeatType,
                        reminderTime = reminderTime
                    )
                } else {
                    habitViewModel.updateHabit(
                        editingHabit!!.copy(
                            name = name,
                            description = description,
                            emoji = emoji,
                            repeatType = repeatType,
                            reminderTime = reminderTime
                        )
                    )
                }

                showDialog = false
                editingHabit = null
            }
        )
    }
}

@Composable
private fun HabitCard(
    habit: HabitEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            HabitsLightGreen,
                            RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = habit.emoji,
                        fontSize = 26.sp
                    )
                }

                Spacer(modifier = Modifier.width(13.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = habit.name,
                        color = HabitsText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (habit.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = habit.description,
                            color = HabitsGray,
                            fontSize = 12.sp
                        )
                    }
                }

                TextButton(onClick = onEdit) {
                    Text("✏️")
                }

                TextButton(onClick = onDelete) {
                    Text("🗑️")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = HabitsBackground)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = HabitsLightGreen,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = when (habit.repeatType) {
                            "DAILY" -> "Každý den"
                            "WEEKLY" -> "Každý týden"
                            "ONCE" -> "Jednorázově"
                            else -> habit.repeatType
                        },
                        color = HabitsGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 7.dp
                        )
                    )
                }

                if (!habit.reminderTime.isNullOrBlank()) {
                    Text(
                        text = "🔔 ${habit.reminderTime}",
                        color = HabitsGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
@Composable
private fun HabitDialog(
    habit: HabitEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String?) -> Unit
) {
    var name by remember(habit) {
        mutableStateOf(habit?.name ?: "")
    }

    var description by remember(habit) {
        mutableStateOf(habit?.description ?: "")
    }

    var emoji by remember(habit) {
        mutableStateOf(habit?.emoji ?: "⭐")
    }

    var repeatType by remember(habit) {
        mutableStateOf(habit?.repeatType ?: "DAILY")
    }

    var reminderTime by remember(habit) {
        mutableStateOf(habit?.reminderTime ?: "")
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val emojis = listOf(
        "⭐", "🏃", "💧", "📖", "🧹", "💪", "🧘", "🎯"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = if (habit == null) {
                    "Nový návyk"
                } else {
                    "Upravit návyk"
                },
                color = HabitsText,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Název návyku") },
                    singleLine = true,
                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Popis") },
                    singleLine = true,
                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Vyber ikonu",
                    color = HabitsText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    emojis.forEach { item ->
                        Surface(
                            onClick = { emoji = item },
                            shape = RoundedCornerShape(11.dp),
                            color = if (emoji == item) {
                                HabitsLightGreen
                            } else {
                                HabitsBackground
                            }
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item,
                                    fontSize = 19.sp
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Opakování",
                    color = HabitsText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(
                        "DAILY" to "Každý den",
                        "WEEKLY" to "Každý týden",
                        "ONCE" to "Jednorázově"
                    ).forEach { (type, label) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = repeatType == type,
                                onClick = { repeatType = type },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = HabitsGreen
                                )
                            )

                            Text(
                                text = label,
                                color = HabitsText,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                HorizontalDivider(color = HabitsBackground)

                Text(
                    text = "Připomínka",
                    color = HabitsText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            val initialHour = reminderTime
                                .takeIf { it.isNotBlank() }
                                ?.substringBefore(":")
                                ?.toIntOrNull()
                                ?: calendar.get(Calendar.HOUR_OF_DAY)

                            val initialMinute = reminderTime
                                .takeIf { it.isNotBlank() }
                                ?.substringAfter(":")
                                ?.toIntOrNull()
                                ?: calendar.get(Calendar.MINUTE)

                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    reminderTime = String.format(
                                        Locale.ROOT,
                                        "%02d:%02d",
                                        hour,
                                        minute
                                    )
                                },
                                initialHour,
                                initialMinute,
                                true
                            ).show()
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (reminderTime.isBlank()) {
                                "🔔 Vybrat čas"
                            } else {
                                "🔔 $reminderTime"
                            }
                        )
                    }

                    if (reminderTime.isNotBlank()) {
                        TextButton(
                            onClick = { reminderTime = "" }
                        ) {
                            Text(
                                text = "Zrušit",
                                color = HabitsRed
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = name.isNotBlank(),
                onClick = {
                    onSave(
                        name.trim(),
                        description.trim(),
                        emoji,
                        repeatType,
                        reminderTime.ifBlank { null }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = HabitsGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Uložit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Zrušit",
                    color = HabitsGray
                )
            }
        }
    )
}