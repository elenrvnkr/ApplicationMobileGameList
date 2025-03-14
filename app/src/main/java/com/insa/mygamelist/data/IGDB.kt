package com.insa.mygamelist.data


import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.SupervisorJob



object IGDB {

    var games = mutableStateListOf<Game>()

    var isLoading = mutableStateOf(false) //pour le démarrage

    var isLoading2= mutableStateOf(false) //pour le scrolling

    private lateinit var db: GameDatabase

    private var offset = 500
    private const val limit = 500

    fun initDatabase(context: Context) { // Récupérer database
        db = GameDatabase.getDatabase(context)
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    fun load(context: Context) { //load à l'ouverture de l'appli
        scope.launch {
            isLoading.value = true // Tant que je charge, je veux l'écran de chargement


            try {
                TokenManager.init(context) // Avoir le token
                val gamesResponse = fetchGames(0) // Récupérer mes jeux avec le token
                saveToDatabase(gamesResponse) // Sauver ce que j'ai récupéré dans ma database
                updateUI(gamesResponse)

            } catch (e: Exception) {
                e.printStackTrace()
                val cachedGames = loadFromDatabase() // Si je n'ai pas réussi à récupérer mes jeux en ligne, je les prends de la database
                if (cachedGames.isNotEmpty()) {
                    updateUI(cachedGames)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                    }}
            }finally {
                isLoading.value = false // Fin du chargement initial

            }}
    }

    fun loadMoreGames() { //pour le scrolling infini
        if (isLoading2.value) return

        CoroutineScope(Dispatchers.IO).launch {
            isLoading2.value = true
            try {
                val newGames = fetchGames(offset) // On change l'offset pour pas récupérer les mêmes jeux en boucle
                games.addAll(newGames) // Même chose qu'updateUI sauf que je supprime pas les jeux déjà présents
                offset += limit
                saveToDatabase(newGames)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading2.value = false
            }
        }
    }

    private suspend fun saveToDatabase(games: List<Game>) {
        val gameEntities = games.map { it.toGameEntity() } //je créé mes string que je vais mettre dans ma base de donnée
        db.gameDao().insertGames(gameEntities) //je mets dans ma base de donnée
    }

    private suspend fun loadFromDatabase(): List<Game> {
        return db.gameDao().getAllGames().map { it.toGame() } //je récupère les jeux dans  ma base de donnée
    }

    private fun updateUI(
        gamesResponse: List<Game>
    ) {
        games.clear()
        games.addAll(gamesResponse)

    }
}
