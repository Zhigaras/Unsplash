package com.zhigaras.unsplash.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.data.MainRepository
import com.zhigaras.unsplash.domain.AppAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext app: Context,
    private val mainRepository: MainRepository
) : ViewModel() {
    
    private val authService = AuthorizationService(app)
    
    private val _authSuccessEventChannel = Channel<Unit>()
    val authSuccessEventChannel get() = _authSuccessEventChannel.receiveAsFlow()
    
    private val _ladingFlow = MutableStateFlow(false)
    val ladingFlow get() = _ladingFlow.asStateFlow()
    
    private val _toastEventChannel = Channel<Int>()
    val toastEventChannel get() = _toastEventChannel.receiveAsFlow()
    
    fun prepareAuthPageIntent(openAuthPage: (Intent) -> Unit) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = AppAuth.getAuthorizationRequest()
        
        val authPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )
        Log.d("AAA", "Generated verifier=${authRequest.codeVerifier},challenge=${authRequest.codeVerifierChallenge}")
        openAuthPage(authPageIntent)
    }
    
    fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()
        viewModelScope.launch {
            when {
                exception != null -> _toastEventChannel.send(R.string.authentication_failed)
                tokenExchangeRequest != null ->
                    onAuthCodeReceived(tokenExchangeRequest)
            }
        }
    }
    
    private suspend fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        _ladingFlow.value = true
        runCatching {
            AppAuth.performTokenRequestSuspend(
                authService = authService,
                tokenRequest = tokenRequest
            )
        }.onSuccess {
            _ladingFlow.value = false
            mainRepository.saveAccessToken(it)
            Log.d("AAA", it)
            _authSuccessEventChannel.send(Unit)
        }.onFailure {
            _ladingFlow.value = false
            _toastEventChannel.send(R.string.auth_cancel)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}