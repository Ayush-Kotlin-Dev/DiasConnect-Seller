package diasconnect.seller.com.dao.order

import diasconnect.seller.com.dao.product.ProductsTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object OrdersTable : Table("orders") {
    val id = long("order_id").autoIncrement()
    val date = datetime("date")
    val sellerId = long("seller_id")
    override val primaryKey = PrimaryKey(id)
}

object OrderItemsTable : Table("order_items") {
    val id = long("item_id").autoIncrement()
    val orderId = long("order_id").references(OrdersTable.id)
    val productId = long("product_id").references(ProductsTable.id)
    val quantity = integer("quantity")
    override val primaryKey = PrimaryKey(id)
}