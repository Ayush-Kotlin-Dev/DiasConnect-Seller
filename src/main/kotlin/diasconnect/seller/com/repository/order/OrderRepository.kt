package diasconnect.seller.com.repository.order

import diasconnect.seller.com.model.myOrder
import diasconnect.seller.com.util.Response


interface OrderRepository {
    suspend fun getOrdersBySellerId(sellerId: Long): Response<List<myOrder>>
}
