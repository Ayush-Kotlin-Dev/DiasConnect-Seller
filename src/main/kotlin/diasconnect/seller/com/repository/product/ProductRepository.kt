package diasconnect.seller.com.repository.product

import diasconnect.seller.com.dao.product.ProductRow
import diasconnect.seller.com.model.*
import diasconnect.seller.com.util.Response

interface ProductRepository {

    suspend fun addProduct(productTextParams: ProductTextParams): Response<ProductResponse>

    suspend fun findProductById(id: Long): Response<ProductResponse>

    suspend fun findProductsBySellerId(sellerId: Long): Response<ProductsResponse>

    suspend fun findProductsByCategoryId(categoryId: Long): Response<ProductResponse>

    suspend fun updateProduct(product: Product): Response<ProductResponse>

    suspend fun deleteProduct(id: Long): Response<ProductResponse>

    suspend fun getQuantityProductOrder(productId: Long, sellerId: Long, dayBefore: String): Int
}