package com.kreggscode.wearosbmi.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.kreggscode.wearosbmi.model.BMIRecord
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TrackerScreen(
    records: List<BMIRecord>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BMITheme.colors.primaryBackground)
            .verticalScroll(scrollState)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (records.isEmpty()) {
            // Empty State
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📈",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "No records yet",
                    fontSize = 14.sp,
                    color = BMITheme.colors.secondaryText
                )
                Text(
                    text = "Calculate BMI to start tracking",
                    fontSize = 12.sp,
                    color = BMITheme.colors.secondaryText,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            // Graph Section
            GraphSection(records)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Statistics
            StatsSection(records)
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Recent Records
            Text(
                text = "Recent Records",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BMITheme.colors.primaryText,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 12.dp)
            )
            
            records.takeLast(5).reversed().forEach { record ->
                RecordCard(record)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Back Button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Text("← Back", fontSize = 12.sp)
        }
    }
}

@Composable
private fun GraphSection(records: List<BMIRecord>) {
    val isDarkTheme = BMITheme.colors.primaryBackground == com.bmi.aipowered.ui.theme.DarkPrimaryBackground
    val displayRecords = records.takeLast(10)
    if (displayRecords.isEmpty()) return
    
    val maxBMI = displayRecords.maxOf { it.bmi }
    val minBMI = displayRecords.minOf { it.bmi }
    val range = (maxBMI - minBMI).coerceAtLeast(1f)
    val chartHeight = 120.dp
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BMITheme.colors.secondaryBackground,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = "BMI Trend",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BMITheme.colors.primaryText,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Beautiful parabolic/curved graph with color shades
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
        ) {
            androidx.compose.foundation.Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val width = size.width
                val height = size.height
                val pointCount = displayRecords.size
                val pointSpacing = width / (pointCount - 1).coerceAtLeast(1)
                
                // Calculate points for smooth curve
                val points = displayRecords.mapIndexed { index, record ->
                    val x = index * pointSpacing
                    val normalizedBMI = ((record.bmi - minBMI) / range).coerceIn(0f, 1f)
                    val y = height - (normalizedBMI * height * 0.8f) - height * 0.1f
                    androidx.compose.ui.geometry.Offset(x, y)
                }
                
                // Draw gradient fill area under curve
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(points.first().x, height)
                    points.forEach { point ->
                        lineTo(point.x, point.y)
                    }
                    lineTo(points.last().x, height)
                    close()
                }
                
                // Gradient fill with color shades based on BMI values
                val gradientColors = displayRecords.map { record ->
                    when {
                        record.bmi < 18.5 -> if (isDarkTheme) Color(0xFF3B82F6) else Color(0xFF0066FF)
                        record.bmi < 25 -> if (isDarkTheme) Color(0xFF10B981) else Color(0xFF00AA44)
                        record.bmi < 30 -> if (isDarkTheme) Color(0xFFF59E0B) else Color(0xFFFF8800)
                        else -> if (isDarkTheme) Color(0xFFEF4444) else Color(0xFFDD0000)
                    }
                }
                
                // Draw filled area with gradient
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientColors.first().copy(alpha = 0.3f),
                            gradientColors.last().copy(alpha = 0.1f)
                        )
                    )
                )
                
                // Draw smooth curved line
                if (points.size >= 2) {
                    val curvePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(points.first().x, points.first().y)
                        for (i in 1 until points.size) {
                            val prevPoint = points[i - 1]
                            val currentPoint = points[i]
                            val controlPoint1 = androidx.compose.ui.geometry.Offset(
                                prevPoint.x + (currentPoint.x - prevPoint.x) / 3,
                                prevPoint.y
                            )
                            val controlPoint2 = androidx.compose.ui.geometry.Offset(
                                currentPoint.x - (currentPoint.x - prevPoint.x) / 3,
                                currentPoint.y
                            )
                            cubicTo(
                                controlPoint1.x, controlPoint1.y,
                                controlPoint2.x, controlPoint2.y,
                                currentPoint.x, currentPoint.y
                            )
                        }
                    }
                    
                    // Draw curve with gradient
                    drawPath(
                        path = curvePath,
                        brush = Brush.horizontalGradient(
                            colors = gradientColors,
                            startX = 0f,
                            endX = width
                        ),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 4.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round
                        )
                    )
                    
                    // Draw data points with glow
                    points.forEachIndexed { index, point ->
                        val pointColor = gradientColors[index]
                        // Glow effect
                        drawCircle(
                            color = pointColor.copy(alpha = 0.4f),
                            radius = 8.dp.toPx(),
                            center = point
                        )
                        // Main point
                        drawCircle(
                            color = pointColor,
                            radius = 5.dp.toPx(),
                            center = point
                        )
                        // Inner highlight
                        drawCircle(
                            color = Color.White.copy(alpha = 0.6f),
                            radius = 2.dp.toPx(),
                            center = point
                        )
                    }
                }
            }
        }
        
        // X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Older",
                fontSize = 9.sp,
                color = BMITheme.colors.secondaryText
            )
            Text(
                text = "Recent",
                fontSize = 9.sp,
                color = BMITheme.colors.secondaryText
            )
        }
    }
}

@Composable
private fun StatsSection(records: List<BMIRecord>) {
    val avgBMI = records.map { it.bmi }.average()
    val maxBMI = records.maxOf { it.bmi }
    val minBMI = records.minOf { it.bmi }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BMITheme.colors.secondaryBackground,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatBox("Average", String.format("%.1f", avgBMI))
        StatBox("Highest", String.format("%.1f", maxBMI))
        StatBox("Lowest", String.format("%.1f", minBMI))
    }
}

@Composable
private fun StatBox(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BMITheme.colors.accent
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = BMITheme.colors.secondaryText,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun RecordCard(record: BMIRecord) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val date = dateFormat.format(Date(record.timestamp))
    
    val categoryColor = when (record.category) {
        "Underweight" -> Color(0xFF3B82F6)
        "Normal" -> Color(0xFF10B981)
        "Overweight" -> Color(0xFFF59E0B)
        else -> Color(0xFFEF4444)
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BMITheme.colors.secondaryBackground,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${record.height.toInt()} cm • ${record.weight.toInt()} kg",
                fontSize = 11.sp,
                color = BMITheme.colors.primaryText,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = date,
                fontSize = 9.sp,
                color = BMITheme.colors.secondaryText,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = String.format("%.1f", record.bmi),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = categoryColor
            )
            Text(
                text = record.category,
                fontSize = 9.sp,
                color = categoryColor,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
