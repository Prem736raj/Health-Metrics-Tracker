package com.health.calculator.bmi.tracker.ui.screens.heartrate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.util.WorkoutRecommendation

@Composable
fun ZoneRecommendationsSection(
    recommendations: List<WorkoutRecommendation>,
    onGoalSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Activity Planning Considerations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Informational guidance; an app estimate is not clearance to exercise.", style = MaterialTheme.typography.bodySmall)
        recommendations.forEachIndexed { index, recommendation ->
            Card(onClick = {
                selectedIndex = if (selectedIndex == index) -1 else index
                onGoalSelected(recommendation.goalName)
            }, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(recommendation.goalName, style = MaterialTheme.typography.titleSmall)
                    Text(recommendation.goalDescription, style = MaterialTheme.typography.bodyMedium)
                    AnimatedVisibility(selectedIndex == index) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(recommendation.keyAdvice, style = MaterialTheme.typography.bodyMedium)
                            recommendation.tips.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }
        }
    }
}
