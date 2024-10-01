package pt.isel.pdm.project.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.project.domainmodel.Ranking
import pt.isel.pdm.project.domainmodel.User
import pt.isel.pdm.project.domainmodel.UserInfo
import pt.isel.pdm.project.infrastructure.UserInfoRepository
import pt.isel.pdm.project.domainmodel.UserLoginCredentialInputModel
import pt.isel.pdm.project.domainmodel.UserSignInCredentialInputModel
import pt.isel.pdm.project.ui.common.GetRankingsException
import pt.isel.pdm.project.ui.common.GetUserException
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.LoginException
import pt.isel.pdm.project.ui.common.LogoutException
import pt.isel.pdm.project.ui.common.SignInException
import pt.isel.pdm.project.ui.common.complete
import pt.isel.pdm.project.ui.common.loading
import pt.isel.pdm.project.ui.common.resetToIdle
import kotlin.Result.Companion.success

class UserViewModel(private val service: UsersService, private val repo: UserInfoRepository) : ViewModel() {

    companion object {
        fun factory(service: UsersService, uRep: UserInfoRepository) = viewModelFactory {
            initializer { UserViewModel(service, uRep) }
        }
    }

    private val errorFlow = MutableStateFlow<LoadState<String>>(Idle)

    private val userInfoFlow: MutableStateFlow<LoadState<UserInfo?>> = MutableStateFlow(Idle)

    private val signedInFlow: MutableStateFlow<LoadState<Boolean>> = MutableStateFlow(Idle)

    private val rankingsFlow: MutableStateFlow<LoadState<List<Ranking>>> = MutableStateFlow(Idle)

    private val profileFlow: MutableStateFlow<LoadState<User>> = MutableStateFlow(Idle)

    val error: Flow<LoadState<String>>
        get() = errorFlow.asStateFlow()

    val signedIn: Flow<LoadState<Boolean>>
        get() = signedInFlow.asStateFlow()

    val userInfo: Flow<LoadState<UserInfo?>>
        get() = userInfoFlow.asStateFlow()

    val rankings : Flow<LoadState<List<Ranking>>>
        get() = rankingsFlow.asStateFlow()

    val profile : Flow<LoadState<User>>
        get() = profileFlow.asStateFlow()

    fun getProfile(userId: Int) {
        profileFlow.value = profileFlow.value.loading()
        viewModelScope.launch {
            try {
                val rsp = service.getUser(userId)
                profileFlow.value = profileFlow.value.complete(success(rsp))
            } catch (e: GetUserException) {
                profileFlow.value = profileFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun getUserInfo() {
        userInfoFlow.value.loading()
        viewModelScope.launch {
            userInfoFlow.value = userInfoFlow.value.complete(runCatching { repo.getUserInfo() })
        }
    }

    fun login(input: UserLoginCredentialInputModel) {
        userInfoFlow.value.loading()
        viewModelScope.launch {
            try {
                val rsp = service.login(input)
                repo.updateUserInfo(rsp)
                userInfoFlow.value = userInfoFlow.value.complete(success(rsp))
            } catch (e: LoginException) {
                userInfoFlow.value = userInfoFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun signIn(input: UserSignInCredentialInputModel) {
        signedInFlow.value = signedInFlow.value.loading()
        viewModelScope.launch {
            try {
                service.signIn(input)
                signedInFlow.value = signedInFlow.value.complete(success(true))
            } catch (e: SignInException) {
                signedInFlow.value = signedInFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun logout(token: String) {
        userInfoFlow.value = userInfoFlow.value.loading()
        viewModelScope.launch {
            try{
                service.logout(token)
                repo.clearUserInfo()
                userInfoFlow.value = userInfoFlow.value.complete(success(null))
            }
            catch (e: LogoutException){
                userInfoFlow.value = userInfoFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun getRankings() {
        rankingsFlow.value = rankingsFlow.value.loading()
        viewModelScope.launch {
            try {
                val rsp = service.getRankings()
                rankingsFlow.value = rankingsFlow.value.complete(success(rsp))
            } catch (e: GetRankingsException) {
                rankingsFlow.value = rankingsFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun resetProfile() {
        profileFlow.value = profileFlow.value.resetToIdle()
    }

    fun resetSignedIn() {
        signedInFlow.value = signedInFlow.value.resetToIdle()
    }

    fun rankingToIdle() {
        rankingsFlow.value = rankingsFlow.value.resetToIdle()
    }

    fun errorToIdle() {
        errorFlow.value = errorFlow.value.resetToIdle()
    }
}