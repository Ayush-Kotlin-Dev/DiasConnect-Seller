package diasconnect.seller.com.repository.product

import diasconnect.seller.com.dao.order.OrderDao
import diasconnect.seller.com.dao.product.ProductDao
import diasconnect.seller.com.dao.product.ProductRow
import diasconnect.seller.com.model.*
import diasconnect.seller.com.util.Response
import io.ktor.http.*
import java.time.LocalDateTime

class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val orderDao: OrderDao
) : ProductRepository {
    override suspend fun addProduct(productTextParams: ProductTextParams): Response<ProductResponse> {
        val productIsAdded = productDao.addProduct(
            Product(
                name = productTextParams.name,
                price = productTextParams.price,
                description = productTextParams.description,
                stock = productTextParams.stock,
                categoryId = productTextParams.categoryId,
                sellerId = productTextParams.sellerId,
                images = productTextParams.images
            )
        )
        return if (productIsAdded != null) {
            Response.Success(
                data = ProductResponse(
                    success = true,
                    message = "Product Added  successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Failed to create post"
                )
            )
        }

    }

    override suspend fun findProductById(id: Long): Response<ProductResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun findProductsBySellerId(sellerId: Long): Response<ProductsResponse> {
        val products = productDao.findProductBySellerId(sellerId)
        return if(products.isNotEmpty()) {
            Response.Success(
                data = ProductsResponse(
                    success = true,
                    products = products.map { toProduct(it) },
                    message = "Products found successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = ProductsResponse(
                    success = false,
                    message = "No products found"
                )
            )
        }
    }

    override suspend fun findProductsByCategoryId(categoryId: Long): Response<ProductResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product): Response<ProductResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(id: Long): Response<ProductResponse> {
        val productIsDeleted = productDao.deleteProduct(id)
        return if (productIsDeleted) {
            Response.Success(
                data = ProductResponse(
                    success = true,
                    message = "Product Deleted successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ProductResponse(
                    success = false,
                    message = "Failed to delete product"
                )
            )
        }
    }

    override suspend fun getQuantityProductOrder(productId: Long, sellerId: Long, dayBefore: String): Int {
        val endDate = LocalDateTime.now()
        val startDate = when (dayBefore) {
            "7 Day" -> endDate.minusDays(7)
            "1 Month" -> endDate.minusMonths(1)
            "3 Months" -> endDate.minusMonths(3)
            "6 Months" -> endDate.minusMonths(6)
            else -> null
        }
        return orderDao.getQuantityProductOrder(productId, sellerId, startDate, endDate)
    }

    private fun toProduct(productRow: ProductRow): Product {
        return Product(
            id = productRow.id,
            name = productRow.name,
            price = productRow.price,
            description = productRow.description,
            stock = productRow.stock,
            categoryId = productRow.categoryId,
            sellerId = productRow.sellerId,
            images = productRow.images,
            createdAt = productRow.createdAt,
            updatedAt = productRow.updatedAt
        )
    }
}