package tech.ritzvincentculanag.intelliquest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.dao.QuestDao
import tech.ritzvincentculanag.intelliquest.dao.UserDao
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.model.User

@Database(entities = [User::class, Quest::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun questDao(): QuestDao

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

                val user = INSTANCE?.userDao()
                val quest = INSTANCE?.questDao()
                val adminRitchie = User(
                    userId = 0,
                    firstName = "Ritz Vincent",
                    lastName = "Culanag",
                    middleName = "Vergara",
                    username = "ritchiev",
                    password = "J&Jwuth10",
                    score = 0
                )
                val adminJayb = User(
                    userId = 0,
                    firstName = "Aljayvee",
                    lastName = "Versola",
                    middleName = "Posadas",
                    username = "aljayveevee",
                    password = "J&Jwuth10",
                    score = 0
                )

                CoroutineScope(Dispatchers.IO).launch {
                    user?.insert(user = adminRitchie)
                    user?.insert(user = adminJayb)
                    quest?.insert(Quest(
                        questId = 0,
                        originUserId = 1,
                        name = "Android 101",
                        description = "Test your knowledge in the field of Android programming. " +
                                "A beginner friendly and a refresher.",
                        questType = QuestType.EASY
                    ))
                    quest?.insert(Quest(
                        questId = 0,
                        originUserId = 1,
                        name = "Math 101",
                        description = "A basic true or false question for basic math knowledge " +
                                "such as integers, absolute value and more.",
                        questType = QuestType.EASY
                    ))
                    quest?.insert(Quest(
                        questId = 0,
                        originUserId = 1,
                        name = "Math 102",
                        description = "Revisit all the concepts you've learned in math through " +
                                "a multiple choice quest!",
                        questType = QuestType.MEDIUM,
                        isTimed = true,
                        timeDuration = 10
                    ))
                    quest?.insert(Quest(
                        questId = 0,
                        originUserId = 1,
                        name = "Java 101",
                        description = "Think you're knowledgeable enough? Try this Java quest to " +
                                "see how much concept and knowledge you know in the language.",
                        questType = QuestType.HARD,
                        isTimed = true,
                        timeDuration = 10
                    ))
                }
            }
        }
    }

}