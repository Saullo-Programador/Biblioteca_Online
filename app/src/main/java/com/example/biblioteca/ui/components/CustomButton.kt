package com.example.biblioteca.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.biblioteca.ui.theme.BibliotecaTheme

enum class ButtonType {
    PRIMARY, SECONDARY, TEXT, OUTLINED, DANGER
}

enum class IconPosition {
    START, END
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.START,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    elevation: Dp = 0.dp,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    val colors = getButtonColors(type)
    val border = getButtonBorder(type)

    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        enabled = enabled && !isLoading,
        shape = shape,
        colors = colors,
        border = border,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        contentPadding = contentPadding
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Indicador de Carregamento
            this@Button.AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.5.dp
                )
            }

            // Conteúdo do Botão (Texto e Ícone)
            Row(
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null && iconPosition == IconPosition.START && !isLoading) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )

                if (icon != null && iconPosition == IconPosition.END && !isLoading) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun getButtonColors(type: ButtonType): ButtonColors {
    return when (type) {
        ButtonType.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
        ButtonType.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        ButtonType.OUTLINED, ButtonType.TEXT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        )
        ButtonType.DANGER -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        )
    }
}

@Composable
private fun getButtonBorder(type: ButtonType): BorderStroke? {
    return when (type) {
        ButtonType.OUTLINED -> BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    BibliotecaTheme(
        dynamicColor = false
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomButton(
                text = "Botão Primário",
                onClick = {}
            )
            CustomButton(
                text = "Carregando",
                onClick = {},
                isLoading = true
            )
        }
    }
}