package diasconnect.seller.com.model

import diasconnect.seller.com.util.CurrentDateTime
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ProductTextParams(
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val categoryId: Long,
    val sellerId: Long,
    val images: List<String> = emptyList()
)

@Serializable
data class ProductParams(
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val categoryId: Long,
    val sellerId: Long
)
@Serializable
data class Product(
    val id: Long? = null,
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val categoryId: Long,
    val sellerId: Long,
//    val sku: String? = null, // Optional SKU
//    val discount: Float? = null, // Optional discount
//    val rating: Float? = null, // Optional average rating
//    val weight: Float? = null, // Optional weight for shipping
    val images: List<String> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String ? = null,
)

@Serializable
data class ProductResponse (
    val success : Boolean,
    val product : Product? = null,
    val message : String,
)

@Serializable
data class ProductsResponse (
    val success : Boolean,
    val products : List<Product> = emptyList(),
    val message : String,
)