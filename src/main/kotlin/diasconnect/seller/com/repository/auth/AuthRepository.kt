package diasconnect.seller.com.repository.auth

import diasconnect.seller.com.dao.user.UserRow
import diasconnect.seller.com.model.AuthResponse
import diasconnect.seller.com.model.SignInParams
import diasconnect.seller.com.model.SignUpParams


interface AuthRepository {
    suspend fun signUp(params: SignUpParams): AuthResponse
    suspend fun signIn(params: SignInParams): AuthResponse

    suspend fun findUserById(id: String): UserRow?
}