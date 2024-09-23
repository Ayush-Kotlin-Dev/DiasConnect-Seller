package diasconnect.seller.com.dao.product

import diasconnect.seller.com.dao.DatabaseFactory.dbQuery
import diasconnect.seller.com.model.Product
import diasconnect.seller.com.util.IdGenerator
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert

interface ProductDao {

    suspend fun addProduct(product: Product): ProductRow?

    suspend fun findProductById(id: Long): ProductRow?

    suspend fun findProductBySellerId(sellerId: Long): List<ProductRow>

    suspend fun findProductByCategoryId(categoryId: Long): List<ProductRow>

    suspend fun updateProduct(product: Product): ProductRow?

    suspend fun deleteProduct(id: Long): Boolean
}

