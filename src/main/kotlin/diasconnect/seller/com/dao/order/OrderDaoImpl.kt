package diasconnect.seller.com.dao.order

import diasconnect.seller.com.dao.DatabaseFactory.dbQuery
import diasconnect.seller.com.model.OrderItemType
import diasconnect.seller.com.model.myOrder
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
                            (OrdersTable.createdAt lessEq endDate)
                }

            if (startDate != null) {
                query.andWhere { OrdersTable.createdAt greaterEq startDate }
            }

            query.singleOrNull()?.get(OrderItemsTable.quantity.sum()) ?: 0
        }
    }
    override suspend fun getOrdersBySellerId(sellerId: Long): List<myOrder> = dbQuery {
        (OrdersTable leftJoin OrderItemsTable)
            .slice(OrdersTable.columns + OrderItemsTable.columns)
            .select { OrdersTable.sellerId eq sellerId }
            .groupBy(OrdersTable.id, OrderItemsTable.id)
            .map { resultRow ->
                val orderId = resultRow[OrdersTable.id]
                val items = OrderItemsTable
                    .select { OrderItemsTable.orderId eq orderId }
                    .map {
                        OrderItemType(
                            id = it[OrderItemsTable.id],
                            productId = it[OrderItemsTable.productId],
                            quantity = it[OrderItemsTable.quantity],
                            price = it[OrderItemsTable.price].toString()
                        )
                    }

                myOrder(
                    id = orderId,
                    userId = resultRow[OrdersTable.userId],
                    status = resultRow[OrdersTable.status],
                    total = resultRow[OrdersTable.total],
                    currency = resultRow[OrdersTable.currency],
                    shippingAddress = resultRow[OrdersTable.shippingAddress],
                    paymentMethod = resultRow[OrdersTable.paymentMethod],
                    createdAt = resultRow[OrdersTable.createdAt].toString(),
                    updatedAt = resultRow[OrdersTable.updatedAt].toString(),
                    items = items
                )
            }.distinctBy { it.id }
    }
}