package tech.ritzvincentculanag.intelliquest.util

import android.content.Context
import android.content.SharedPreferences
import tech.ritzvincentculanag.intelliquest.model.User

private const val PREFERENCE_NAME = "Intelliquest"

private const val USER_ACTIVE = "USER_ACTIVE"
private const val USER_ID = "USER_ID"
private const val SKIP_INTRODUCTION = "SKIP_INTRODUCTION"

class SessionManager(context: Context) {

    private val preferences: SharedPreferences
    private val preferencesEditor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferencesEditor = preferences.edit()
    }

    fun saveSession(user: User) {
        preferencesEditor.putInt(USER_ID, user.userId)
        preferencesEditor.putBoolean(USER_ACTIVE, true)
        preferencesEditor.commit()
    }

    fun skipIntroduction() {
        preferencesEditor.putBoolean(SKIP_INTRODUCTION, true)
        preferencesEditor.commit()
    }

    fun clearSession() {
        preferencesEditor.clear()
        preferencesEditor.commit()
    }

    fun userIsActive(): Boolean = preferences.getBoolean(USER_ACTIVE, false)

    fun hasSkipIntroduction(): Boolean = preferences.getBoolean(SKIP_INTRODUCTION, false)

}