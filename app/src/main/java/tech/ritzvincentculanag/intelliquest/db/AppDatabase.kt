package tech.ritzvincentculanag.intelliquest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.dao.UserDao
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.User

@Database(entities = [User::class, Quest::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Intelliquest"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(loadInitialUsers)
                    .build()

                INSTANCE = instance
                return instance
            }
        }

        private val loadInitialUsers: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val dao = INSTANCE?.userDao()
                val admin = User(
                    userId = 0,
                    firstName = "Ritz Vincent",
                    lastName = "Culanag",
                    middleName = "Vergara",
                    username = "ritchiev",
                    password = "J&Jwuth10",
                    score = 0
                )

                CoroutineScope(Dispatchers.IO).launch {
                    dao?.insert(user = admin)
                }
            }
        }
    }

}