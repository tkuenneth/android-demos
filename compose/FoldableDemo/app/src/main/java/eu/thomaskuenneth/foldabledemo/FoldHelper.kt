package eu.thomaskuenneth.foldabledemo

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowLayoutInfo
import androidx.window.layout.WindowMetrics

data class FoldDef(
    val hasFold: Boolean,
    val foldOrientation: FoldingFeature.Orientation?,
    val foldWidth: Dp,
    val foldHeight: Dp,
    val widthLeftOrTop: Dp,
    val heightLeftOrTop: Dp,
    val widthRightOrBottom: Dp,
    val heightRightOrBottom: Dp,
    val isPortrait: Boolean,
    val windowSizeClass: WindowSizeClass,
)

@Composable
fun createFoldDef(
    layoutInfo: WindowLayoutInfo?,
    windowMetrics: WindowMetrics
): FoldDef {
    var foldOrientation: FoldingFeature.Orientation? = null
    var widthLeftOrTop = 0
    var heightLeftOrTop = 0
    var widthRightOrBottom = 0
    var heightRightOrBottom = 0
    var foldWidth = 0
    var foldHeight = 0
    layoutInfo?.displayFeatures?.forEach { displayFeature ->
        (displayFeature as FoldingFeature).run {
            foldOrientation = orientation
            val foldAdjusted = isSurfaceDuo && (bounds.width() == 0 || bounds.height() == 0)
            foldWidth = bounds.width()
            foldHeight = bounds.height()
            // Surface Duo and Duo 2 width/height (depending on orientation) with hinge
            val widthOrHeight = listOf(2784, 2754)
            if (orientation == FoldingFeature.Orientation.VERTICAL) {
                if (widthOrHeight.contains(windowMetrics.bounds.width())) {
                    if (foldAdjusted) foldWidth = if (foldHeight == 1800) 84 else 66
                }
                widthLeftOrTop = if (foldAdjusted)
                    (windowMetrics.bounds.width() - foldWidth) / 2
                else
                    bounds.left
                heightLeftOrTop = windowMetrics.bounds.height()
                widthRightOrBottom = if (foldAdjusted)
                    (windowMetrics.bounds.width() - foldWidth) / 2
                else
                    windowMetrics.bounds.width() - bounds.right
                heightRightOrBottom = heightLeftOrTop
            } else if (orientation == FoldingFeature.Orientation.HORIZONTAL) {
                if (widthOrHeight.contains(windowMetrics.bounds.height())) {
                    if (foldAdjusted) foldHeight = if (foldWidth == 1800) 84 else 66
                }
                widthLeftOrTop = windowMetrics.bounds.width()
                heightLeftOrTop = bounds.top
                widthRightOrBottom = windowMetrics.bounds.width()
                heightRightOrBottom = if (foldAdjusted)
                    (windowMetrics.bounds.height() - foldHeight) / 2
                else
                    windowMetrics.bounds.height() - bounds.bottom
            }
        }
    }
    return with(LocalDensity.current) {
        FoldDef(
            foldOrientation = foldOrientation,
            widthLeftOrTop = widthLeftOrTop.toDp(),
            heightLeftOrTop = heightLeftOrTop.toDp(),
            widthRightOrBottom = widthRightOrBottom.toDp(),
            heightRightOrBottom = heightRightOrBottom.toDp(),
            foldWidth = foldWidth.toDp(),
            foldHeight = foldHeight.toDp(),
            isPortrait = windowWidthDp(windowMetrics) / windowHeightDp(windowMetrics) <= 1F,
            windowSizeClass = WindowSizeClass.compute(
                dpWidth = windowWidthDp(windowMetrics = windowMetrics).value,
                dpHeight = windowHeightDp(windowMetrics = windowMetrics).value
            ),
            hasFold = foldOrientation != null
        )
    }
}

@Composable
fun windowWidthDp(windowMetrics: WindowMetrics): Dp = with(LocalDensity.current) {
    windowMetrics.bounds.width().toDp()
}

@Composable
fun windowHeightDp(windowMetrics: WindowMetrics): Dp = with(LocalDensity.current) {
    windowMetrics.bounds.height().toDp()
}

private val isSurfaceDuo: Boolean =
    "${Build.MANUFACTURER} ${Build.MODEL}".contains("Microsoft Surface Duo")
