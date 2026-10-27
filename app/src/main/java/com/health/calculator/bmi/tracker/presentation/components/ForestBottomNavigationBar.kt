package com.health.calculator.bmi.tracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.core.navigation.BottomNavItem
import com.health.calculator.bmi.tracker.ui.components.forest.BotanicalPalette
import com.health.calculator.bmi.tracker.ui.components.forest.ForestText
import com.health.calculator.bmi.tracker.ui.components.forest.ForestIcons

@Composable
internal fun ForestBottomNavigationBar(currentRoute: String?, onItemClick: (BottomNavItem) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 12.dp, vertical = 5.dp)
        .clip(CircleShape).background(if (BotanicalPalette.isLight) BotanicalPalette.Surface else BotanicalPalette.Background)
        .border(.8.dp, BotanicalPalette.Outline, CircleShape).selectableGroup()
        .padding(horizontal = 5.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        BottomNavItem.entries.forEach { item ->
            val selected = currentRoute == item.route
            Column(Modifier.weight(1f).heightIn(min = 48.dp)
                .selectable(selected, role = Role.Tab, onClick = { onItemClick(item) }),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Box(Modifier.width(60.dp).height(28.dp).clip(CircleShape)
                    .then(if (selected) Modifier.background(Brush.horizontalGradient(listOf(BotanicalPalette.Selected, BotanicalPalette.Selected.copy(alpha = .88f)))) else Modifier), Alignment.Center) {
                    Icon(if (item == BottomNavItem.INSIGHTS) ForestIcons.Trend else if (selected) item.selectedIcon else item.unselectedIcon, item.contentDescription,
                        Modifier.size(22.dp), tint = if (selected) BotanicalPalette.Ink else BotanicalPalette.Secondary)
                }
                ForestText(item.title, 12f, if (selected) BotanicalPalette.Primary else BotanicalPalette.Secondary, bold = selected)
            }
        }
    }
}
