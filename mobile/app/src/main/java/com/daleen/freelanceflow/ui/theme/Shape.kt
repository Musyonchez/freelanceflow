package com.daleen.freelanceflow.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Corner radii extracted from the Figma Make export -- see docs/19-ui-design-system.md section 4.
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(7.dp), // tags
    small = RoundedCornerShape(11.dp), // icon buttons
    medium = RoundedCornerShape(18.dp), // metric / workspace-stat cards
    large = RoundedCornerShape(20.dp), // panels
    extraLarge = RoundedCornerShape(24.dp), // hero cards, action sheet top
)

// M3's Shapes object only has 6 slots; the mock uses more distinct corner treatments than that.
val FabShape = RoundedCornerShape(18.dp)
val TimerControlShape = RoundedCornerShape(12.dp)
val PrimaryButtonShape = RoundedCornerShape(13.dp)
val AvatarShape = RoundedCornerShape(13.dp)
val TimerCardShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 18.dp, bottomEnd = 18.dp)
val ActionSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
