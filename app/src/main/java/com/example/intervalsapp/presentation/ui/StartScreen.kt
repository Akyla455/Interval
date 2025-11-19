package com.example.intervalsapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.intervalsapp.R
import com.example.intervalsapp.presentation.UiLoadState
import com.example.intervalsapp.presentation.WorkoutViewModel

@Composable
fun StartScreen(
    navController: NavHostController
) {
    val viewModel: WorkoutViewModel = hiltViewModel()
    val state by viewModel.loadState.collectAsStateWithLifecycle()

    var timerId by remember { mutableStateOf("68") }

    LaunchedEffect(state) {
        if (state is UiLoadState.Loaded) {
            navController.navigate("workout/$timerId")
            viewModel.resetLoadState()
        }
    }
    StartScreenContent(
        state = state,
        currentId = timerId,
        onIdChange = { timerId = it },
        onLoadTimer = viewModel::loadTimer
    )

}

@Composable
fun StartScreenContent(
    modifier: Modifier = Modifier,
    state: UiLoadState,
    currentId: String,
    onIdChange: (String) -> Unit,
    onLoadTimer: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.start_screen_enter_training_id),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(36.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = currentId,
            onValueChange = onIdChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = state !is UiLoadState.Loading,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                disabledContainerColor = androidx.compose.ui.graphics.Color.LightGray,
                disabledContentColor = androidx.compose.ui.graphics.Color.DarkGray
            ),
            onClick = { onLoadTimer(currentId) }
        ) {
            if (state is UiLoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = androidx.compose.ui.graphics.Color.DarkGray,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.start_screen_download_button_text)
                )
            }
        }
        if (state is UiLoadState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ошибка: ${state.message}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}