package com.health.calculator.bmi.tracker.ui.screens.articles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.ui.components.SupportingLeafMark
import com.health.calculator.bmi.tracker.ui.components.detail.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthArticlesScreen(
    onNavigateBack: () -> Unit,
    onOpenBloodPressureEducation: () -> Unit,
    onOpenWaterEducation: () -> Unit
) {
    DetailScreenTheme(DetailFeature.ARTICLES) {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = { Text(stringResource(R.string.health_articles_title)) }, navigationIcon = {
                IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
            })
        }) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SupportingLeafMark()
                        Text(stringResource(R.string.health_articles_subtitle), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                    }
                }
                item { ReadingShelfItem("01", stringResource(R.string.bp_education_button), Icons.Outlined.FavoriteBorder, onOpenBloodPressureEducation) }
                item { ReadingShelfItem("02", stringResource(R.string.hydration_education_button), Icons.Outlined.WaterDrop, onOpenWaterEducation) }
            }
        }
    }
}

@Composable
private fun ReadingShelfItem(number: String, title: String, icon: ImageVector, onClick: () -> Unit) {
    // A book spine and ruled shelf give education its own editorial composition.
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.heightIn(min = 132.dp).padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(number, style = MaterialTheme.typography.labelLarge, color = DetailColors.Secondary)
                Icon(icon, null, tint = DetailColors.Accent)
            }
            VerticalDivider(Modifier.height(76.dp), color = DetailColors.Outline)
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = DetailColors.Accent)
        }
    }
}
