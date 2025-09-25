package com.example.sports.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sports.R
import com.example.sports.domain.model.SportsPerformance
import com.example.sports.domain.model.StorageType
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "MMM dd, yyyy HH:mm"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceListScreen(
    viewModel: PerformanceListViewModel,
    onNavigateToAdd: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.sports_performances_title)) },
                modifier = Modifier.statusBarsPadding()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_performance))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterSection(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::updateFilter
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.performances.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_performances_message),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.performances,
                            key = { it.id }
                        ) { performance ->
                            PerformanceItem(
                                performance = performance,
                                onDelete = { viewModel.deletePerformance(performance) }
                            )
                        }
                    }
                }
            }

            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    selectedFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    val selectedTabIndex = FilterType.entries.indexOf(selectedFilter)

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        FilterType.entries.forEachIndexed { index, filterType ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onFilterSelected(filterType) },
                text = {
                    Text(
                        text = when (filterType) {
                            FilterType.ALL -> stringResource(R.string.filter_all)
                            FilterType.LOCAL -> stringResource(R.string.filter_local)
                            FilterType.REMOTE -> stringResource(R.string.filter_remote)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun PerformanceItem(
    performance: SportsPerformance,
    onDelete: () -> Unit
) {
    val storageColor = when (performance.storageType) {
        StorageType.LOCAL -> MaterialTheme.colorScheme.primaryContainer
        StorageType.REMOTE -> MaterialTheme.colorScheme.secondaryContainer
    }

    val dateFormatter = remember { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = storageColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = performance.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.location_label, performance.location),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = stringResource(R.string.duration_label, performance.duration),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (performance.storageType == StorageType.LOCAL) {
                                    Color.Blue
                                } else {
                                    Color.Green
                                },
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Text(
                        text = performance.storageType.name,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 6.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = dateFormatter.format(performance.createdAt),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_performance),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}