package diasconnect.seller.com.route

import com.google.firebase.cloud.StorageClient
import diasconnect.seller.com.model.Product
import diasconnect.seller.com.model.ProductParams
import diasconnect.seller.com.model.ProductQuantity
import diasconnect.seller.com.model.ProductTextParams
import diasconnect.seller.com.repository.product.ProductRepository
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.util.*

fun Routing.ProductRouting() {
    val repository by inject<ProductRepository>()

    route(path = "/product") {
        post(path = "/add") {
            try {
                val multipart = call.receiveMultipart()
                var productParams: ProductParams? = null
                val imageUrls = mutableListOf<String>()

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val fileExtension = part.originalFileName?.substringAfterLast('.', "")
                            val fileBytes = part.streamProvider().readBytes()

                            // Upload to Firebase
                            val bucket = StorageClient.getInstance().bucket()
                            val uniqueFileName = "${UUID.randomUUID()}.${fileExtension}"
                            val blob = bucket.create("product_images/$uniqueFileName", fileBytes)
                            println(blob)

                            // Generate a UUID as a token
                            val token = UUID.randomUUID().toString()
                            blob.toBuilder().setMetadata(mapOf("firebaseStorageDownloadTokens" to token)).build().update()

                            // Construct the public URL with token
                            val publicUrl = "https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/product_images%2F$uniqueFileName?alt=media&token=$token"
                            imageUrls.add(publicUrl)
                        }
                        is PartData.FormItem -> {
                            if (part.name == "product_data") {
                                productParams = Json.decodeFromString<ProductParams>(part.value)
                            }
                        }
                        else -> {}
                    }
                    part.dispose()
                }

                if (productParams != null) {
                    if (imageUrls.isEmpty()) {
                        call.respond(HttpStatusCode.BadRequest, "No images uploaded")
                        return@post
                    }

                    val result = repository.addProduct(
                        ProductTextParams(
                            name = productParams!!.name,
                            price = productParams!!.price,
                            description = productParams!!.description,
                            stock = productParams!!.stock,
                            categoryId = productParams!!.categoryId,
                            sellerId = productParams!!.sellerId,
                            images = imageUrls
                        )
                    )
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid product data")
                }
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

        get("/dashboard/{sellerId}") {
            val sellerId = call.parameters["sellerId"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid seller ID")
            val dayBefore = call.request.queryParameters["dayBefore"] ?: "All"

            val products = repository.findProductsBySellerId(sellerId).data.products
            val productQuantities = products.map { product ->
                val quantity = repository.getQuantityProductOrder(product.id!!, sellerId, dayBefore)
                ProductQuantity(product, quantity)
            }

            call.respond(productQuantities)
        }
    }
}