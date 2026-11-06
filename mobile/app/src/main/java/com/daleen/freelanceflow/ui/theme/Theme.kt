package com.daleen.freelanceflow.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Role mapping from the Figma Make export -- see docs/19-ui-design-system.md section 1.3.
private val LightColorScheme = lightColorScheme(
    primary = Brand,
    onPrimary = Color.White,
    primaryContainer = Mint,
    onPrimaryContainer = BrandDark,
    secondary = Purple,
    onSecondary = OnPurpleIcon,
    secondaryContainer = Blue,
    onSecondaryContainer = OnBlueIcon,
    tertiary = Peach,
    onTertiary = OnPeachIcon,
    tertiaryContainer = Amber,
    onTertiaryContainer = AvatarOnAmber,
    background = Canvas,
    onBackground = Ink,
    surface = Surface,
    onSurface = Ink,
    surfaceVariant = SurfaceSoft,
    onSurfaceVariant = Muted,
    outline = Line,
    outlineVariant = Line,
    inverseSurface = BrandDark,
    inverseOnSurface = Color.White,
)

// Inferred, not observed -- the Figma export has no dark-mode artboards.
// See docs/19-ui-design-system.md section 1.4.
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = Mint,
    secondary = Purple,
    onSecondary = OnPurpleIcon,
    secondaryContainer = Blue,
    onSecondaryContainer = OnBlueIcon,
    tertiary = Peach,
    onTertiary = OnPeachIcon,
    tertiaryContainer = Amber,
    onTertiaryContainer = AvatarOnAmber,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
)

@Composable
fun FreelanceFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off by default: dynamic color pulls from the device wallpaper (Android 12+), which
    // would override this app's fixed brand identity. See docs/20-ui-migration-plan.md Phase 1.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
