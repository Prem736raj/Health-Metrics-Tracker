package com.health.calculator.bmi.tracker.ui.components.home

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.health.calculator.bmi.tracker.data.model.SearchResult
import com.health.calculator.bmi.tracker.data.model.SearchResultType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<SearchResult>,
    recentSearches: List<String>,
    onClearRecent: () -> Unit,
    onRemoveRecent: (String) -> Unit,
    onResultClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        SearchTextField(
            query = query,
            onQueryChange = {
                onQueryChange(it)
                isExpanded = it.isNotEmpty() || recentSearches.isNotEmpty()
            },
            onFocusChange = { focused ->
                if (focused) isExpanded = true
            },
            onClear = {
                onQueryChange("")
                isExpanded = recentSearches.isNotEmpty()
            },
            focusRequester = focusRequester
        )

        if (isExpanded) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.surface(MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        if (query.isEmpty() && recentSearches.isNotEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(stringResource(R.string.txt_recent_searches), style = MaterialTheme.typography.titleSmall)
                                    TextButton(onClick = onClearRecent) {
                                        Text(stringResource(R.string.txt_clear_all_1))
                                    }
                                }
                            }
                            items(recentSearches) { recent ->
                                RecentSearchItem(
                                    text = recent,
                                    onClick = { onQueryChange(recent) },
                                    onRemove = { onRemoveRecent(recent) }
                                )
                            }
                        } else if (results.isNotEmpty()) {
                            items(results) { result ->
                                SearchResultItem(
                                    result = result,
                                    onClick = {
                                        onResultClick(result)
                                        isExpanded = false
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                        } else if (query.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No results for \"$query\"", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester
) {
    val isDark = AppColors.isDark
    val containerBg = if (isDark) Color(0xFF131C15) else Color(0xFFFFFFFF)
    val outlineBorder = if (isDark) Color(0xFF243426) else Color(0xFFDCE6DA)
    val iconColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)
    val placeholderColor = if (isDark) Color(0xFF6E7E71) else Color(0xFF6B7B6E)
    val textColor = if (isDark) Color.White else Color(0xFF162018)

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(stringResource(R.string.txt_search_health_tools_terms), color = placeholderColor) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = iconColor) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = iconColor)
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            unfocusedContainerColor = containerBg,
            focusedContainerColor = containerBg,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(26.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(26.dp), spotColor = if (isDark) Color.Black.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = if (query.isEmpty()) outlineBorder else iconColor,
                shape = RoundedCornerShape(26.dp)
            )
            .focusRequester(focusRequester)
            .onFocusChanged { state -> onFocusChange(state.isFocused) }
    )
}

@Composable
fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(result.title, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(result.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(result.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        },
        trailingContent = {
            Badge(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Text(result.type.label, modifier = Modifier.padding(horizontal = 4.dp))
            }
        },
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun RecentSearchItem(
    text: String,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text) },
        leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
        trailingContent = {
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Close, contentDescription = "Remove")
            }
        },
        modifier = Modifier.clickable { onClick() }
    )
}
