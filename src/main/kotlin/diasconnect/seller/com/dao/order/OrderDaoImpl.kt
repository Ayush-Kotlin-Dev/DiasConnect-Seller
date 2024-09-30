package diasconnect.seller.com.dao.order

import diasconnect.seller.com.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

class OrderDaoImpl : OrderDao {
    override suspend fun getQuantityProductOrder(productId: Long, sellerId: Long, startDate: LocalDateTime?, endDate: LocalDateTime): Int {
        return dbQuery {
            val query = OrderItemsTable
                .innerJoin(OrdersTable, { OrderItemsTable.orderId }, { OrdersTable.id })
                .slice(OrderItemsTable.quantity.sum())
                .select {
                    (OrdersTable.sellerId eq sellerId) and
                            (OrderItemsTable.productId eq productId) and
                            (OrdersTable.date lessEq endDate)
                }

            if (startDate != null) {
                query.andWhere { OrdersTable.date greaterEq startDate }
            }

            query.singleOrNull()?.get(OrderItemsTable.quantity.sum()) ?: 0
        }
    }
}