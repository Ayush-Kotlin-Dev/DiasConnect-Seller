package diasconnect.seller.com.dao.order

import diasconnect.seller.com.model.myOrder
import java.time.LocalDateTime

interface OrderDao {
    suspend fun getQuantityProductOrder(productId: Long, sellerId: Long, startDate: LocalDateTime?, endDate: LocalDateTime): Int


    suspend fun getOrdersBySellerId(sellerId: Long): List<myOrder>

}