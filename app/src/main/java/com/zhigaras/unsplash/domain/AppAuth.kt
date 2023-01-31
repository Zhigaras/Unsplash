package com.zhigaras.unsplash.domain

import androidx.core.net.toUri
import com.zhigaras.unsplash.model.TokenModel
import net.openid.appauth.*
import kotlin.coroutines.suspendCoroutine

object AppAuth {
    
    private val serviceConfig = AuthorizationServiceConfiguration(
        AuthConfig.AUTH_URI.toUri(),
        AuthConfig.TOKEN_URI.toUri()
    )
    
    fun getAuthorizationRequest(): AuthorizationRequest =
        AuthorizationRequest.Builder(
            serviceConfig,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            AuthConfig.REDIRECT_URL.toUri()
        ).setScopes(AuthConfig.SCOPES)
            .build()
    
    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): String {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, ex ->
                when {
                    response != null -> {
                        //получение токена произошло успешно
                        val token = response.accessToken.orEmpty()
                        
                        continuation.resumeWith(Result.success(token))
                    }
                    //получение токенов произошло неуспешно, показываем ошибку
                    ex != null -> { continuation.resumeWith(Result.failure(ex)) }
                    else -> error("unreachable")
                }
            }
        }
    }
    
    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }
    
    private object AuthConfig {
        const val AUTH_URI = "https://unsplash.com/oauth/authorize"
        const val TOKEN_URI = "https://unsplash.com/oauth/token"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPES = "public"
        const val CLIENT_ID = "VNZuauGLQhmjIs8ZCJJW26m5B3wYoUqYin6iZJdZ30s"
        const val CLIENT_SECRET = "9eXr-6tXL_AncnIiIr4Mnce_xQ1A74zlg6xTmPofTH0"
        const val REDIRECT_URL = "urn:ietf:wg:oauth:2.0:oob"
    }
}