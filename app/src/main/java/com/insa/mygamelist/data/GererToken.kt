package com.insa.mygamelist.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.Date

object TokenManager {

    private const val PREFS_NAME = "IGDB_PREFS" //Nom de notre stockage
    private const val TOKEN_KEY = "IGDB_TOKEN"
    private const val TOKEN_EXPIRATION_KEY = "IGDB_TOKEN_EXPIRATION"
    private const val TOKEN_URL = "https://id.twitch.tv/oauth2/token"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    suspend fun getToken(): String {
        val currentToken = sharedPreferences.getString(TOKEN_KEY, null) //Récupère token et expiration time
        val expirationTime = sharedPreferences.getLong(TOKEN_EXPIRATION_KEY, 0)

        if (currentToken == null || isTokenExpired(expirationTime)) { //si le token existe pas ou qu'il est expiré on en fait un autre sinon on garde
            return fetchNewToken()
        }

        return currentToken
    }

    private suspend fun fetchNewToken(): String {
        val url =
            "$TOKEN_URL?client_id=$clientid&client_secret=$clientsecret&grant_type=client_credentials" //la même chose que le cURL

        val request = Request.Builder()
            .url(url)
            .post("".toRequestBody())
            .build()

        val response = withContext(Dispatchers.IO) {
            OkHttpClient().newCall(request).execute()
        }

        if (!response.isSuccessful) throw Exception("Erreur de récupération du token : ${response.code}")

        val responseBody = response.body?.string() ?: throw Exception("Réponse vide")
        val json = JSONObject(responseBody) // La réponse est en Json
        val newToken = json.getString("access_token")
        val expiresIn = json.getLong("expires_in") // Durée de validité (en secondes)

        val expirationTime = (Date().time / 1000) + expiresIn

        withContext(Dispatchers.IO) {
            sharedPreferences.edit().apply { //Stocke le nouveau token
                putString(TOKEN_KEY, newToken)
                putLong(TOKEN_EXPIRATION_KEY, expirationTime)
                apply()
            }
        }

        return newToken
    }

    private fun isTokenExpired(expirationTime: Long): Boolean {
        return Date().time > expirationTime * 1000 // Date().time est en millisecondes
    }
}
