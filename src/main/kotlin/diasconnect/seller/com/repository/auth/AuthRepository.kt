package diasconnect.seller.com.repository.auth

import diasconnect.seller.com.dao.user.UserRow
import diasconnect.seller.com.model.AuthResponse
import diasconnect.seller.com.model.SignInParams
import diasconnect.seller.com.model.SignUpParams
import diasconnect.seller.com.util.Response


interface AuthRepository {
    suspend fun signUp(params: SignUpParams): Response<AuthResponse>
    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}