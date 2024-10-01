package pt.isel.pdm.project.ui.user

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import pt.isel.pdm.project.ui.common.GetRankingsException
import pt.isel.pdm.project.ui.common.GetUserException
import pt.isel.pdm.project.ui.common.LoginException
import pt.isel.pdm.project.ui.common.LogoutException
import pt.isel.pdm.project.ui.common.SignInException
import pt.isel.pdm.project.domainmodel.Ranking
import pt.isel.pdm.project.domainmodel.SirenListDtoModel
import pt.isel.pdm.project.domainmodel.SirenMapDtoModel
import pt.isel.pdm.project.domainmodel.User
import pt.isel.pdm.project.domainmodel.UserInfo
import pt.isel.pdm.project.domainmodel.UserLoginCredentialInputModel
import pt.isel.pdm.project.domainmodel.UserSignInCredentialInputModel
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface UsersService {
    suspend fun login(input: UserLoginCredentialInputModel): UserInfo
    suspend fun signIn(input: UserSignInCredentialInputModel): User
    suspend fun logout(token: String)
    suspend fun getRankings(): List<Ranking>
    suspend fun getUser(userId: Int): User
}

class UserService(
    private val client: OkHttpClient,
    private val gson: Gson,
    ngrokURL: String
) : UsersService {

    private val templateURL = "$ngrokURL/api"

    override suspend fun login(input: UserLoginCredentialInputModel): UserInfo {
        val request = Request.Builder()
            .url("$templateURL/login")
            .addHeader("accept", "application/json")
            .post("""{
                "username" : "${input.username}",
                "password" : "${input.password}"
                }""".trimMargin().toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(LoginException("Failed to login", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(LoginException(response.message))
                    else it.resume(
                        gson.fromJson(body.string(), SirenMapDtoModel::class.java).toUserInfo()
                    )
                }
            })
        }
    }

    override suspend fun signIn(input: UserSignInCredentialInputModel): User {
        val request = Request.Builder()
            .url("$templateURL/signin")
            .addHeader("accept", "application/json")
            .post("""{
                "name" : "${input.username}",
                "email" : "${input.email}",
                "password" : "${input.password}"
                }""".trimMargin().toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(SignInException("Failed to sign in", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null){
                        it.resumeWithException(SignInException(response.message))}
                    else it.resume(gson.fromJson(body.string(), SirenMapDtoModel::class.java).toUser())
                }
            })
        }
    }

    override suspend fun logout(token: String) {
        val request = Request.Builder()
            .url("$templateURL/logout")
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .method("POST", "".toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(LogoutException("Failed to logout", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(LogoutException(response.message))
                    else it.resume(Unit)
                }
            })
        }
    }

    override suspend fun getRankings(): List<Ranking> {
        val request = Request.Builder()
            .url ("$templateURL/rankings")
            .addHeader("accept", "application/json")
            .method("GET", null)
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(GetRankingsException("Failed to fetch rankings", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(GetRankingsException("Failed to fetch rankings: ${response.code}"))
                    else
                        it.resume(gson.fromJson(body.string(), SirenListDtoModel::class.java).toRankingDtoModel())
                }
            })
        }
    }


    override suspend fun getUser(userId: Int): User {
        val request = Request.Builder()
            .url ("$templateURL/users/$userId")
            .addHeader("accept", "application/json")
            .method("GET", null)
            .build()
        return suspendCoroutine {
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    it.resumeWithException(GetUserException("Failed to fetch user", e))
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val body = response.body
                    if (!response.isSuccessful || body == null)
                        it.resumeWithException(GetUserException("Failed to fetch user: ${response.code}"))
                    else
                        it.resume(gson.fromJson(body.string(), SirenMapDtoModel::class.java).toUser())
                }
            })
        }
    }
}
