
import androidx.room.TypeConverters
import androidx.room.Database
import androidx.room.RoomDatabase
import com.intentaction.reminder.db.dao.ActionIntentDao
import com.intentaction.reminder.db.entity.IntentAction
import com.intentaction.reminder.helpers.Converters

@Database(entities = [IntentAction::class], version = 1)
@TypeConverters(Converters::class) // Include this if you have custom type converters
abstract class AppDatabase : RoomDatabase() {
    abstract fun intentDao(): ActionIntentDao
}


