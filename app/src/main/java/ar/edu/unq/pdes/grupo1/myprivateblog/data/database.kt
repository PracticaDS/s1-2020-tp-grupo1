package ar.edu.unq.pdes.grupo1.myprivateblog.data

import android.content.Context
import android.graphics.Color
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.Serializable

typealias EntityID = Int

@Database(entities = [BlogEntry::class], version = 1)
@TypeConverters(ThreeTenTimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun blogEntriesDao(): BlogEntriesDao

    companion object {
        fun generateDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "myprivateblog.db"
        ).build()
    }

}

@Entity(
    tableName = "BlogEntries"
)
data class BlogEntry(

    @PrimaryKey(autoGenerate = true)
    var uid: EntityID = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "bodyPath")
    val bodyPath: String,

    @ColumnInfo(name = "imagePath")
    val imagePath: String? = null,

    @ColumnInfo(name = "is_deleted")
    val deleted: Boolean = false,

    @ColumnInfo(name = "date")
    val date: OffsetDateTime? = null,

    @ColumnInfo(name = "cardColor")
    val cardColor: Int = Color.WHITE

) : Serializable

@Dao
interface BlogEntriesDao {
    @Query("SELECT * FROM BlogEntries ORDER BY date DESC")
    fun getAll(): Flowable<List<BlogEntry>>

    @Query("SELECT * FROM BlogEntries WHERE uid = :entryId LIMIT 1")
    fun loadById(entryId: EntityID): Flowable<BlogEntry>

    @Insert
    fun insertAll(entries: List<BlogEntry>): Completable

    @Insert
    fun insert(entries: BlogEntry): Single<Long>

    @Update
    fun updateAll(entries: List<BlogEntry>): Completable

    @Update
    fun update(entry: BlogEntry): Completable


    @Delete
    fun delete(entry: BlogEntry): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(blogEntries: List<BlogEntry>): Completable
}

object ThreeTenTimeTypeConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}
