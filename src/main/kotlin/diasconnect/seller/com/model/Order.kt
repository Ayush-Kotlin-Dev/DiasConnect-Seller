package diasconnect.seller.com.model

import diasconnect.seller.com.dao.order.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
data class myOrder(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val total: Float,
    val currency: String,
    val shippingAddress: String,
    val paymentMethod: String,
    val createdAt: String,
    val updatedAt: String,
    val items: List<OrderItemType>
)
@Serializable
data class OrderItemType(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val price: String
)
