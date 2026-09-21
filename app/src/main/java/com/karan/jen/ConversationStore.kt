package com.karan.jen

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class ConversationEntry(val role: String, val text: String)

class ConversationStore(context: Context) {
    private val prefs = context.getSharedPreferences("jen_memory", Context.MODE_PRIVATE)
    private val key = "conversation"
    private val maxEntries = 20

    fun add(role: String, text: String) {
        val entries = entries().toMutableList()
        entries.add(ConversationEntry(role, text.trim()))
        val kept = entries.takeLast(maxEntries)
        val json = JSONArray()
        kept.forEach {
            json.put(JSONObject().put("role", it.role).put("text", it.text))
        }
        prefs.edit().putString(key, json.toString()).apply()
    }

    fun entries(): List<ConversationEntry> {
        val raw = prefs.getString(key, null) ?: return emptyList()
        return try {
            val json = JSONArray(raw)
            (0 until json.length()).mapNotNull { index ->
                val item = json.optJSONObject(index) ?: return@mapNotNull null
                val role = item.optString("role")
                val text = item.optString("text")
                if (role.isBlank() || text.isBlank()) null else ConversationEntry(role, text)
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun clear() {
        prefs.edit().remove(key).apply()
    }
}
