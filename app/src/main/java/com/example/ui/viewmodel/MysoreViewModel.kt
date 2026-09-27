package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookmarkEntity
import com.example.data.local.ItineraryEntity
import com.example.data.model.*
import com.example.data.repository.MysoreRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    EXPLORE,
    DETAIL,
    MAP,
    PLANNER,
    FOOD,
    SAVED
}

data class UiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val selectedAttraction: Attraction? = null,
    val selectedCategory: Category? = null,
    val searchQuery: String = "",
    val activeInterests: Set<String> = setOf("Heritage", "Food", "Nature"),
    val plannerDays: Int = 2,
    val generatedPlan: List<GeneratedDayPlan> = emptyList(),
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val savedItineraries: List<ItineraryEntity> = emptyList(),
    val statusMessage: String? = null
)

class MysoreViewModel(private val repository: MysoreRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val attractions: List<Attraction> = repository.getAttractions()
    val foods: List<FoodItem> = repository.getFoodItems()
    val experiences: List<ExperienceItem> = repository.getExperiences()
    val travelInfo: TravelInfo = repository.getTravelInfo()
    val mapMarkers: List<MysoreMapMarker> = repository.getMapMarkers()
    val weather: WeatherInfo = WeatherInfo()

    val searchSuggestions = listOf(
        "Mysore Palace",
        "Best dosa near me",
        "Hotels under ₹3000",
        "Things to do at night",
        "Chamundi Hill 1000 steps",
        "Pure Mysore Pak",
        "Brindavan musical fountain"
    )

    init {
        // Collect Bookmarks
        viewModelScope.launch {
            repository.getAllBookmarks().collect { list ->
                _uiState.update { it.copy(bookmarks = list) }
            }
        }

        // Collect Saved Itineraries
        viewModelScope.launch {
            repository.getAllSavedItineraries().collect { list ->
                _uiState.update { it.copy(savedItineraries = list) }
            }
        }

        // Generate initial default itinerary
        generateTripItinerary(2, setOf("Heritage", "Food", "Nature"))
    }

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectAttraction(attraction: Attraction) {
        _uiState.update {
            it.copy(
                selectedAttraction = attraction,
                currentScreen = AppScreen.DETAIL
            )
        }
    }

    fun selectCategory(category: Category?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleInterest(interest: String) {
        _uiState.update { state ->
            val updated = state.activeInterests.toMutableSet()
            if (updated.contains(interest)) {
                updated.remove(interest)
            } else {
                updated.add(interest)
            }
            state.copy(activeInterests = updated)
        }
    }

    fun setPlannerDays(days: Int) {
        _uiState.update { it.copy(plannerDays = days) }
    }

    fun generateTripItinerary(days: Int, interests: Set<String>) {
        val plan = repository.generateItinerary(days, interests)
        _uiState.update {
            it.copy(
                plannerDays = days,
                activeInterests = interests,
                generatedPlan = plan
            )
        }
    }

    fun toggleBookmark(attraction: Attraction) {
        viewModelScope.launch {
            val isBookmarked = _uiState.value.bookmarks.any { it.id == attraction.id }
            repository.toggleBookmark(attraction, isBookmarked)
            _uiState.update {
                it.copy(
                    statusMessage = if (isBookmarked) "Removed from Saved" else "Saved to Bookmarks"
                )
            }
        }
    }

    fun saveCurrentItinerary() {
        viewModelScope.launch {
            val state = _uiState.value
            val title = "${state.plannerDays}-Day Mysore ${state.activeInterests.joinToString(" & ")} Trip"
            val summary = state.generatedPlan.joinToString(" | ") { day ->
                "Day ${day.dayNumber}: ${day.themeTitle} (${day.slots.size} stops)"
            }
            repository.saveItinerary(
                title = title,
                daysCount = state.plannerDays,
                interests = state.activeInterests.toList(),
                summary = summary
            )
            _uiState.update {
                it.copy(statusMessage = "Itinerary saved to My Trips!")
            }
        }
    }

    fun deleteSavedItinerary(id: Int) {
        viewModelScope.launch {
            repository.deleteItinerary(id)
            _uiState.update { it.copy(statusMessage = "Itinerary removed") }
        }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }
}

class MysoreViewModelFactory(private val repository: MysoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MysoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MysoreViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
