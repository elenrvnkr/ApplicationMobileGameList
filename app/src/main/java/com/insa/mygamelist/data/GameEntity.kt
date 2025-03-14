package com.insa.mygamelist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// On récupère tout les games pour les mettre en string dans la database
@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val coverUrl: String?,
    val releaseDate: Long?,
    val genres: String?,
    val platforms: String?,
    val platform_logos: String?,
    val summary: String?,
    val rating: String?
)


fun GameEntity.toGame(): Game {
    return Game(
        id = this.id,
        name = this.name,
        cover = mapOf("image_id" to (this.coverUrl ?: "")),
        first_release_date = this.releaseDate ?: 0,
        genres = this.genres?.split(";")?.map { mapOf("name" to it) } ?: listOf(mapOf("name" to "Non disponible")),
        platforms = this.platforms?.split(";")?.mapIndexed { index, platformName ->
            val logos = this.platform_logos?.split(";") ?: emptyList()
            Platform(null, platformName, PlatformLogo(null, logos.getOrNull(index)))
        } ?: emptyList()
        ,
        summary = this.summary ?: "Résumé par défaut",
        total_rating = this.rating ?: ""
    )
}

fun Game.toGameEntity(): GameEntity {
    return GameEntity(
        id = id,
        name = name ?: "",
        summary = summary ?: null,
        rating = total_rating?.toString() ?: "0",
        coverUrl = cover?.get("image_id") ?: null,
        genres = genres?.joinToString(", ") { it["name"] ?: "" } ?: null,
        platforms = platforms?.joinToString(",") { it.name ?: "" } ?: "",
        platform_logos = platforms
            ?.mapNotNull { it.platform_logo?.image_id }
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString(",")
            ?: "",
        releaseDate = first_release_date
    )
}

data class Game(val id: Long, val cover: Map<String, String>, val first_release_date: Long, val genres: List<Map<String,String>>, val name: String, val platforms: List<Platform>, val summary: String, val total_rating: String)
data class PlatformLogo(
    val id: Int?,
    val image_id: String? // L'url s'écrit https://images.igdb.com/igdb/image/upload/t_{size}/{image_id}.png
)
data class Platform(
    val id: Int?,
    val name: String?, // Pour l'accessibilité
    val platform_logo: PlatformLogo?
)