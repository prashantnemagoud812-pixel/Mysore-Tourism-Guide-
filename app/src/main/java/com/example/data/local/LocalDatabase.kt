package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val location: String,
    val rating: Float,
    val imageUrl: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_itineraries")
data class ItineraryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val daysCount: Int,
    val interestsCsv: String,
    val contentSummary: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface MysoreDao {
    @Query("SELECT * FROM bookmarks ORDER BY addedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun removeBookmark(id: String)

    @Query("SELECT * FROM saved_itineraries ORDER BY createdAt DESC")
    fun getAllItineraries(): Flow<List<ItineraryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItinerary(itinerary: ItineraryEntity): Long

    @Query("DELETE FROM saved_itineraries WHERE id = :id")
    suspend fun deleteItinerary(id: Int)
}

@Database(entities = [BookmarkEntity::class, ItineraryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mysoreDao(): MysoreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mysore_explorer_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
