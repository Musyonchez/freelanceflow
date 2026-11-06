package com.daleen.freelanceflow.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// CSS box-shadow -> Modifier.shadow(elevation, shape, ambientColor, spotColor) translations.
// See docs/19-ui-design-system.md section 5. Alpha hex: .09->0x17, .035->0x09, .28->0x47,
// applied to the RGB from each source rgba().
val ShadowHeroColor = Color(0x17263D33) // 0 16px 50px rgba(38,61,51,.09) -- hero card, action sheet
val ShadowHeroElevation: Dp = 16.dp
val ShadowPanelColor = Color(0x09293A32) // 0 8px 30px rgba(41,58,50,.035) -- panels
val ShadowPanelElevation: Dp = 8.dp
val ShadowFabColor = Color(0x47175F4A) // 0 10px 25px rgba(23,95,74,.28) -- FAB
val ShadowFabElevation: Dp = 10.dp
