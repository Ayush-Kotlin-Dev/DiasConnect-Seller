package diasconnect.seller.com

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import diasconnect.seller.com.dao.DatabaseFactory
import diasconnect.seller.com.di.configureDI
import diasconnect.seller.com.plugins.*
import diasconnect.seller.com.util.GraphqlConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.FileInputStream
import java.io.IOException

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
//    configureRateLimitedApiKeyAuthentication()
    configureDI()
    configureSecurity()
    configureSockets()
    GraphqlConfig()
    this.configureCORS()

//    val serviceAccountPa/th = "/etc/secrets/service_account_key.json"
//    try {
//
////        val serviceAccountStream = FileInputStream(serviceAccountPath)
//        val serviceAccountStream = this::class.java.classLoader.getResourceAsStream("service-account.json")
//        val options = FirebaseOptions.builder()
//            .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
//            .setStorageBucket("theinstau-3b0cc.appspot.com")
//            .build()
//        FirebaseApp.initializeApp(options)
//    } catch (e: IOException) {
//        e.printStackTrace()
//        throw RuntimeException("Failed to initialize Firebase with service account key", e)
//    }
}