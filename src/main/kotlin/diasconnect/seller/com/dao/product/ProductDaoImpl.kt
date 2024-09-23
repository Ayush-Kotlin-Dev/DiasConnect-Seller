package diasconnect.seller.com.dao.product

import diasconnect.seller.com.dao.DatabaseFactory.dbQuery
import diasconnect.seller.com.dao.product.ProductsTable.innerJoin
import diasconnect.seller.com.model.Product
import diasconnect.seller.com.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ProductDaoImpl : ProductDao {
    override suspend fun addProduct(product: Product): ProductRow? {
        return dbQuery {
            // Insert the product and get the insert statement
            val insertStatement = ProductsTable.insert {
                it[id] = IdGenerator.generateId() // Assuming IdGenerator is defined
                it[name] = product.name
                it[price] = product.price
                it[description] = product.description
                it[stock] = product.stock
                it[categoryId] = product.categoryId
                it[sellerId] = product.sellerId
            }

            // Get the generated product ID
            val productId = insertStatement.resultedValues?.singleOrNull()?.get(ProductsTable.id) ?: return@dbQuery null

            // Insert each image
            product.images.forEach { image ->
                ProductImagesTable.insert {
                    it[ProductImagesTable.productId] = productId // Use the productId from the insert
                    it[imageUrl] = image // Ensure you're accessing the imageUrl correctly
                }
            }

            // Convert the inserted product row back to ProductRow
            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToProduct(it, product.images)
            }
        }
    }

    override suspend fun findProductById(id: Long): ProductRow? {
        TODO("Not yet implemented")
    }

    override suspend fun findProductBySellerId(sellerId: Long): List<ProductRow> {
        return dbQuery {
            (ProductsTable leftJoin ProductImagesTable)
                .slice(ProductsTable.columns + ProductImagesTable.imageUrl)
                .select { ProductsTable.sellerId eq sellerId }
                .groupBy { it[ProductsTable.id] }
                .map { (_, rows) ->
                    val productRow = rows.first()
                    val images = rows.map { it[ProductImagesTable.imageUrl] }
                    rowToProduct(productRow, images)
                }
        }
    }


    override suspend fun findProductByCategoryId(categoryId: Long): List<ProductRow> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product): ProductRow? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(id: Long): Boolean {
        return dbQuery {
            // First, delete the associated images
            ProductImagesTable.deleteWhere { productId eq id }

            // Then, delete the product
            val deletedRows = ProductsTable.deleteWhere { ProductsTable.id eq id }
            deletedRows > 0
        }
    }


    private fun rowToProduct(row: ResultRow, images: List<String> = emptyList()): ProductRow {
        return ProductRow(
            id = row[ProductsTable.id],
            name = row[ProductsTable.name],
            price = row[ProductsTable.price],
            description = row[ProductsTable.description],
            stock = row[ProductsTable.stock],
            images = images,
            categoryId = row[ProductsTable.categoryId],
            sellerId = row[ProductsTable.sellerId],
            createdAt = row[ProductsTable.createdAt].toString(),
            updatedAt = row[ProductsTable.updatedAt].toString()
        )
    }


}