package diasconnect.seller.com.repository.auth

import diasconnect.seller.com.dao.user.UserDao
import diasconnect.seller.com.model.AuthResponse
import diasconnect.seller.com.model.AuthResponseData
import diasconnect.seller.com.model.SignInParams
import diasconnect.seller.com.model.SignUpParams
import diasconnect.seller.com.plugins.generateToken
import diasconnect.seller.com.security.hashPassword
import diasconnect.seller.com.util.Response
import io.ktor.http.*

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAlreadyExist(params.email)) {
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "A user with this email already exists!"
                )
            )
        } else {
            val insertedUser = userDao.createUser(username = params.name, email = params.email, passwordHash = hashPassword(params.password))

            if (insertedUser == null) {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Oops, sorry we could not register the user, try later!"
                    )
                )
            } else {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = insertedUser.id,
                            name = insertedUser.username,
                            email = insertedUser.email,
                            token = generateToken(params.email),
                            created = insertedUser.createdAt,
                            updated = insertedUser.updatedAt
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        val user = userDao.findUserByEmail(params.email)

        return if (user == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "Invalid credentials, no user with this email!"
                )
            )
        } else {
            val hashedPassword = hashPassword(params.password)

            if (user.passwordHash  == hashedPassword) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = user.id,
                            name = user.username,
                            email = user.email,
                            token = generateToken(params.email),
                            created = user.createdAt,
                            updated = user.updatedAt
                        )
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = AuthResponse(
                        errorMessage = "Invalid credentials, wrong password!"
                    )
                )
            }
        }
    }

    private suspend fun userAlreadyExist(email: String): Boolean {
        return userDao.findUserByEmail(email) != null
    }
}












