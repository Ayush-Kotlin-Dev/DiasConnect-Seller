package diasconnect.seller.com.route

import diasconnect.seller.com.repository.order.OrderRepository
import diasconnect.seller.com.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRoutes() {
    val orderRepository: OrderRepository by inject()

    route("/orders") {
        get("/seller/{sellerId}") {
            val sellerId = call.parameters["sellerId"]?.toLongOrNull()
            if (sellerId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid seller ID")
                return@get
            }

            when (val result = orderRepository.getOrdersBySellerId(sellerId)) {
                is Response.Success -> call.respond(HttpStatusCode.OK, result.data)
                is Response.Error -> call.respond(result.code, result.data)
            }
        }
    }
}