package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.scrollbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Expressive Fast-Scroll ScrollBar with Floating Index Label.
 *
 * @param listState LazyListState tracking scroll position
 * @param labelResolver Function mapping current item index to a short label string
 * @param modifier Custom modifier
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveScrollBar(
    listState: LazyListState,
    labelResolver: (Int) -> String,
    modifier: Modifier = Modifier,
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
) {
    val coroutineScope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }

    val totalItemsCount = listState.layoutInfo.totalItemsCount
    val firstVisibleIndex = listState.firstVisibleItemIndex

    val scrollFraction by remember {
        derivedStateOf {
            if (totalItemsCount == 0) 0f
            else (firstVisibleIndex.toFloat() / totalItemsCount.toFloat()).coerceIn(0f, 1f)
        }
    }

    val currentLabel by remember {
        derivedStateOf {
            if (totalItemsCount == 0) ""
            else labelResolver(firstVisibleIndex.coerceIn(0, totalItemsCount - 1))
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(48.dp)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        // Track
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(trackColor)
        )

        // Thumb + Label
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .pointerInput(totalItemsCount) {
                    detectVerticalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            val totalHeight = size.height.toFloat()
                            if (totalHeight > 0 && totalItemsCount > 0) {
                                val deltaFraction = dragAmount / totalHeight
                                val targetIndex = ((scrollFraction + deltaFraction) * totalItemsCount)
                                    .roundToInt()
                                    .coerceIn(0, totalItemsCount - 1)

                                coroutineScope.launch {
                                    listState.scrollToItem(targetIndex)
                                }
                            }
                        }
                    )
                },
            contentAlignment = Alignment.TopEnd
        ) {
            val density = LocalDensity.current
            val thumbOffsetPx = remember(scrollFraction) {
                with(density) { (scrollFraction * 300.dp.toPx()).roundToInt() }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset { IntOffset(0, thumbOffsetPx) }
            ) {
                // Floating Index Label
                AnimatedVisibility(
                    visible = isDragging,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(ShapeCache.smooth12)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentLabel,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // ScrollBar Thumb Bar
                Box(
                    modifier = Modifier
                        .size(
                            width = if (isDragging) 10.dp else 6.dp,
                            height = if (isDragging) 48.dp else 36.dp
                        )
                        .clip(CircleShape)
                        .background(thumbColor)
                )
            }
        }
    }
}
