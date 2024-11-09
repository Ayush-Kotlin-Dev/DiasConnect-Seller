package diasconnect.seller.com.repository.order

import diasconnect.seller.com.dao.order.OrderDao
import diasconnect.seller.com.model.myOrder
import diasconnect.seller.com.util.Response
import io.ktor.http.*

class OrderRepositoryImpl(private val dao: OrderDao) : OrderRepository {
    override suspend fun getOrdersBySellerId(sellerId: Long): Response<List<myOrder>> {
        return try {
            val orders = dao.getOrdersBySellerId(sellerId)
            Response.Success(orders)
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = emptyList()
            )
        }
    }
}