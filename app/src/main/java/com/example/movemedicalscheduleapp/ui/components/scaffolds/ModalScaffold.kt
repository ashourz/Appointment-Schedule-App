package com.example.movemedicalscheduleapp.ui.components.scaffolds

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.movemedicalscheduleapp.ui.components.bottombars.ModalBottomBar
import com.example.movemedicalscheduleapp.ui.components.toolbars.BasicTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalScaffold(
    title: String,
    actionButtonText: String,
    validation: () -> Boolean,
    onActionButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    content: @Composable (ColumnScope) -> Unit
) {
    Scaffold(
        topBar = { BasicTopBar(title) },
        bottomBar = {
            ModalBottomBar(
                actionText = actionButtonText,
                onCancelButtonClick = { onCancelButtonClick() },
                onAddButtonClick = {
                    if(validation()) {
                        onActionButtonClick()
                    }}
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            content(this)
        }
    }
}

