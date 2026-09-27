package com.example

import com.example.data.local.BookmarkEntity
import com.example.data.local.ItineraryEntity
import com.example.data.local.MysoreDao
import com.example.data.repository.MysoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    private val fakeDao = object : MysoreDao {
        override fun getAllBookmarks(): Flow<List<BookmarkEntity>> = flowOf(emptyList())
        override fun isBookmarked(id: String): Flow<Boolean> = flowOf(false)
        override suspend fun insertBookmark(bookmark: BookmarkEntity) {}
        override suspend fun removeBookmark(id: String) {}
        override fun getAllItineraries(): Flow<List<ItineraryEntity>> = flowOf(emptyList())
        override suspend fun insertItinerary(itinerary: ItineraryEntity): Long = 1L
        override suspend fun deleteItinerary(id: Int) {}
    }

    private val repository = MysoreRepository(fakeDao)

    @Test
    fun testAttractionsLoadedSuccessfully() {
        val attractions = repository.getAttractions()
        assertTrue(attractions.isNotEmpty())
        assertTrue(attractions.any { it.name.contains("Mysore Palace") })
        assertTrue(attractions.any { it.name.contains("Chamundi") })
    }

    @Test
    fun testGenerateItineraryDaysCount() {
        val plan1 = repository.generateItinerary(1, setOf("Heritage", "Food"))
        assertEquals(1, plan1.size)
        assertEquals(4, plan1[0].slots.size)

        val plan3 = repository.generateItinerary(3, setOf("Heritage", "Nature", "Photography"))
        assertEquals(3, plan3.size)
    }

    @Test
    fun testFoodItemsHaveFamousPlaces() {
        val foods = repository.getFoodItems()
        assertTrue(foods.isNotEmpty())
        foods.forEach { food ->
            assertTrue(food.famousPlaces.isNotEmpty())
        }
    }
}
