package com.example.data.model

data class Attraction(
    val id: String,
    val name: String,
    val tagLine: String,
    val category: Category,
    val rating: Float,
    val reviewCount: Int,
    val location: String,
    val distanceKm: Float,
    val durationText: String,
    val entryFee: String,
    val timings: String,
    val bestTimeToVisit: String,
    val description: String,
    val historicalHighlights: List<String>,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val audioSnippet: String,
    val isIlluminatedAtNight: Boolean = false
)

enum class Category(val displayName: String, val iconEmoji: String, val description: String) {
    HERITAGE("Heritage", "🏰", "Discover royal palaces and historical landmarks"),
    NATURE("Nature", "🌿", "Gardens, lakes, hills and peaceful escapes"),
    FOOD("Food", "🍛", "Discover Mysore's famous local food and restaurants"),
    SHOPPING("Shopping", "🛍️", "Silk, sandalwood, handicrafts and local markets"),
    CULTURE("Culture", "🎭", "Festivals, traditions, art and royal heritage"),
    STAY("Stay", "🏨", "Find heritage palaces, resorts and boutique stays")
}

data class FoodItem(
    val id: String,
    val name: String,
    val kannadaName: String,
    val shortDescription: String,
    val fullStory: String,
    val priceRange: String,
    val famousPlaces: List<String>,
    val isVegetarian: Boolean = true,
    val imageUrl: String,
    val tag: String
)

data class ExperienceItem(
    val id: String,
    val title: String,
    val duration: String,
    val category: String,
    val priceGuide: String,
    val description: String,
    val bestTime: String,
    val location: String,
    val highlights: List<String>,
    val imageUrl: String
)

data class TravelInfo(
    val bestSeason: String,
    val bestMonths: String,
    val airport: String,
    val railHub: String,
    val roadExpressway: String,
    val gettingAround: List<TransportOption>,
    val tips: List<String>,
    val dayTrips: List<DayTrip>
)

data class TransportOption(
    val mode: String,
    val description: String,
    val priceGuide: String,
    val tip: String
)

data class DayTrip(
    val destination: String,
    val distanceKm: Int,
    val travelTime: String,
    val highlights: String,
    val imageUrl: String
)

data class MysoreMapMarker(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: Category,
    val latitude: Double,
    val longitude: Double,
    val rating: Float,
    val image: String
)

data class ItinerarySlot(
    val timeSlot: String, // Morning, Afternoon, Evening, Night
    val activityTitle: String,
    val placeName: String,
    val description: String,
    val duration: String,
    val travelTip: String,
    val category: Category,
    val isCheckmarked: Boolean = false
)

data class GeneratedDayPlan(
    val dayNumber: Int,
    val themeTitle: String,
    val slots: List<ItinerarySlot>
)

data class WeatherInfo(
    val tempC: Int = 27,
    val condition: String = "Pleasant & Sunny",
    val humidity: Int = 54,
    val windKmh: Int = 12,
    val sunsetTime: String = "6:18 PM",
    val palaceLightingStatus: String = "Sunday 7:00 PM – 8:00 PM (100,000 bulbs)",
    val travelRecommendation: String = "Perfect day for palace grounds & Chamundi Hill breeze."
)
