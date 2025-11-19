package com.example.intervalsapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.intervalsapp.domain.model.IntervalTimer
import com.example.intervalsapp.domain.model.WorkoutUpdate
import com.example.intervalsapp.domain.repository.WorkoutStateRepository
import com.example.intervalsapp.domain.useCase.GetIntervalTimerUseCase
import com.example.intervalsapp.domain.useCase.ObserveLocationUseCase
import com.example.intervalsapp.domain.useCase.ObserveWorkoutUseCase
import com.example.intervalsapp.domain.useCase.StartWorkoutUseCase
import com.example.intervalsapp.domain.useCase.StopWorkoutUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTimerUseCase: GetIntervalTimerUseCase,
    private val observeWorkoutUseCase: ObserveWorkoutUseCase,
    private val observeLocationUseCase: ObserveLocationUseCase,
    private val startUseCase: StartWorkoutUseCase,
    private val stopUseCase: StopWorkoutUseCase,
    private val workoutStateRepository: WorkoutStateRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _loadState = MutableStateFlow<UiLoadState>(UiLoadState.Idle)
    val loadState = _loadState.asStateFlow()

    private val _timer = MutableStateFlow<IntervalTimer?>(null)
    val timer = _timer.asStateFlow()

    private val _workout = MutableStateFlow<WorkoutUpdate?>(null)
    val workout = _workout.asStateFlow()

    private val _locations = MutableStateFlow<List<LatLng>>(emptyList())
    val locations = _locations.asStateFlow()

    init {

        val timerId = savedStateHandle.get<String>("timerId")

        if (timerId != null) {
            loadTimer(timerId)
        }

        viewModelScope.launch {
            observeWorkoutUseCase().collect { upd -> _workout.value = upd }
        }
        viewModelScope.launch {
            observeLocationUseCase().collect { locs -> _locations.value = locs }
        }
    }

    fun loadTimer(id: String) {
        viewModelScope.launch {
            _loadState.value = UiLoadState.Loading
            val res = getTimerUseCase(id)
            res.onSuccess {
                _timer.value = it
                _loadState.value = UiLoadState.Loaded
            }.onFailure {
                _loadState.value = UiLoadState.Error(it.message ?: "Ошибка")
            }
        }
    }

    fun resetLoadState() {
        _loadState.value = UiLoadState.Idle
    }

    fun start() {
        val currentTimerId = savedStateHandle.get<String>("timerId")
        if (currentTimerId != null) {
            viewModelScope.launch {
                workoutStateRepository.saveActiveTimerId(currentTimerId)
                _timer.value?.let { startUseCase(it) }
            }
        }
    }
    fun stop() = stopUseCase()
}