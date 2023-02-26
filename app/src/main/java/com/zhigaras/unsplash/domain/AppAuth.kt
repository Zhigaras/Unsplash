package com.zhigaras.unsplash.domain

import androidx.core.net.toUri
import net.openid.appauth.*
import kotlin.coroutines.suspendCoroutine

object AppAuth {
    
    private val serviceConfig = AuthorizationServiceConfiguration(
        AuthConfig.AUTH_URI.toUri(),
        AuthConfig.TOKEN_URI.toUri(),
        null,
        AuthConfig.LOGOUT_URL.toUri()
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
            authService.performTokenRequest(
                tokenRequest,
                getClientAuthentication()
            ) { response, ex ->
                when {
                    response != null -> {
                        val token = response.accessToken.orEmpty()
                        continuation.resumeWith(Result.success(token))
                    }
                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                    }
                    else -> error("unreachable")
                }
            }
        }
    }
    
    fun getLogoutRequest(token: String?): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfig)
            .setPostLogoutRedirectUri(AuthConfig.REDIRECT_URL.toUri())
            .setIdTokenHint(token)
            .build()
    }
    
    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }
    
    object AuthConfig {
        const val AUTH_URI = "https://unsplash.com/oauth/authorize"
        const val TOKEN_URI = "https://unsplash.com/oauth/token"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPES =
            "public read_user write_user read_photos write_photos write_likes write_followers read_collections write_collections"
        const val CLIENT_ID = "VNZuauGLQhmjIs8ZCJJW26m5B3wYoUqYin6iZJdZ30s"
        const val CLIENT_SECRET = "9eXr-6tXL_AncnIiIr4Mnce_xQ1A74zlg6xTmPofTH0"
        const val REDIRECT_URL = "com.zhigaras.unsplash://unsplash"
        const val LOGOUT_URL = "https://unsplash.com/logout"
    }
}