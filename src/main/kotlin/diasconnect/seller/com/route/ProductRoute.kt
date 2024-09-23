package diasconnect.seller.com.route

import diasconnect.seller.com.model.Product
import diasconnect.seller.com.model.ProductParams
import diasconnect.seller.com.model.ProductTextParams
import diasconnect.seller.com.repository.product.ProductRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.ProductRouting() {
    val repository by inject<ProductRepository>()

    route(path = "/product") {
        post(path = "/add") {
            try {
                val product = call.receive<ProductParams>()
                val dummyImageList = listOf("image1", "image2", "image3")
                val result = repository.addProduct(
                    ProductTextParams(
                        name = product.name,
                        price = product.price,
                        description = product.description,
                        stock = product.stock,
                        categoryId = product.categoryId,
                        sellerId = product.sellerId,
                        images = dummyImageList
                    )
                )
                call.respond(
                    status = result.code,
                    message = result.data
                )
            } catch (e: BadRequestException) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "An unexpected error occurred, try again"
                )
            }
        }

        get(path = "/{id}") {
            // Get product by id
        }

        get(path = "/seller/{sellerId}") {
            try{
                val sellerId = call.parameters["sellerId"]?.toLongOrNull()
                if (sellerId == null) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Invalid seller id"
                    )
                    return@get
                }
                val result = repository.findProductsBySellerId(sellerId)
                call.respond(
                    status = result.code,
                    message = result.data
                )
            } catch (e: BadRequestException) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "An unexpected error occurred, try again"
                )
            }
        }

        get(path = "/category/{categoryId}") {
            // Get products by category id
        }

        post(path = "/update") {
            // Update product
        }

        delete(path = "/delete/{id}") {
            try {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Invalid id"
                    )
                    return@delete
                }
                val result = repository.deleteProduct(id)
                call.respond(
                    status = result.code,
                    message = result.data
                )
            } catch (e: BadRequestException) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "An unexpected error occurred, try again"
                )
            }

        }
    }
}