package pt.isel.pdm.project.ui.game

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.pdm.project.ui.common.CreateGameException
import pt.isel.pdm.project.ui.common.UpdateGameException
import pt.isel.pdm.project.ui.common.GetLobbiesException
import pt.isel.pdm.project.domainmodel.Game
import pt.isel.pdm.project.domainmodel.GameCreateInputModel
import pt.isel.pdm.project.domainmodel.GamePlayInputModel
import pt.isel.pdm.project.domainmodel.GameReloadInputModel
import pt.isel.pdm.project.domainmodel.GiveUpInputModel
import pt.isel.pdm.project.domainmodel.LobbyInputModel
import pt.isel.pdm.project.domainmodel.SirenMapDtoModel
import pt.isel.pdm.project.domainmodel.SirenMessageDtoModel
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface GamesService {
    suspend fun create(input: GameCreateInputModel): String
    suspend fun play(input: GamePlayInputModel, userId: Int, gameId: String, token: String): Game
    suspend fun reload(input: GameReloadInputModel): Game
    suspend fun forfeit(input: GiveUpInputModel): Game
    suspend fun checkLobby(input: LobbyInputModel): String
}

class GameService(
    private val client: OkHttpClient,
    private val gson: Gson,
    ngrokURL: String
) : GamesService {

    private val templateURL = "$ngrokURL/api"

    override suspend fun create(input: GameCreateInputModel): String {
        val request = Request.Builder()
            .url("$templateURL/games")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${input.token}")
            .post(
                """{
                "userId" : "${input.userId}",
                "rule" : "${input.rule}",
                "boardSize" : "${input.boardSize}"
                }""".trimMargin().toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(CreateGameException("Failed to create the game", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(CreateGameException(response.message))
                    else it.resume(
                        gson.fromJson(body.string(), SirenMessageDtoModel::class.java)
                            .toMessageDtoModel()
                    )
                }
            })
        }
    }

    override suspend fun play(
        input: GamePlayInputModel,
        userId: Int,
        gameId: String,
        token: String
    ): Game {
        val request = Request.Builder()
            .url("$templateURL/games/${gameId}")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .post(
                """{
                "userId" : "$userId",
                "row" : "${input.row}",
                "col" : "${input.col}"
                }""".trimMargin().toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(UpdateGameException("Failed to make the play", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    val bodyString = body?.string()
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(UpdateGameException(bodyString ?: response.message))
                    else it.resume(gson.fromJson(bodyString, SirenMapDtoModel::class.java).toGame())
                }
            })
        }
    }

    override suspend fun reload(input: GameReloadInputModel): Game {
        val request = Request.Builder()
            .url("$templateURL/games/${input.gameId}")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${input.token}")
            .method("GET", null)
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(UpdateGameException("Failed to fetch game", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(UpdateGameException(response.message))
                    else it.resume(
                        gson.fromJson(body.string(), SirenMapDtoModel::class.java).toGame()
                    )
                }
            })
        }
    }

    override suspend fun forfeit(input: GiveUpInputModel): Game {
        val request = Request.Builder()
            .url("$templateURL/giveup/${input.gameId}")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${input.token}")
            .method("POST", "".toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(UpdateGameException("Failed to give up", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(UpdateGameException(response.message))
                    else it.resume(
                        gson.fromJson(body.string(), SirenMapDtoModel::class.java).toGame()
                    )
                }
            })
        }
    }

    override suspend fun checkLobby(input: LobbyInputModel): String {
        val request = Request.Builder()
            .url("$templateURL/lobbies/${input.userId}")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer ${input.token}")
            .method("GET", null)
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(GetLobbiesException("Failed to fetch lobbies", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(
                            GetLobbiesException("Failed to fetch lobbies: ${response.code}")
                        )
                    else it.resume(
                        gson.fromJson(body.string(), SirenMessageDtoModel::class.java)
                            .toMessageDtoModel()
                    )
                }
            })
        }
    }
}

