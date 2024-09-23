package diasconnect.seller.com.util

// Generic class to handle response
// from the repository
sealed class Response<T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val message: String, val data: T? = null) : Response<T>()
}