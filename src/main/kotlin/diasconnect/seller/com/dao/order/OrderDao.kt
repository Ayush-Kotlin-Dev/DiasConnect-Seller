package diasconnect.seller.com.dao.order

import java.time.LocalDateTime

interface OrderDao {
    suspend fun getQuantityProductOrder(productId: Long, sellerId: Long, startDate: LocalDateTime?, endDate: LocalDateTime): Int
}