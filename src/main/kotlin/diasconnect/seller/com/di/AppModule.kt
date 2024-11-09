package diasconnect.seller.com.di

import diasconnect.seller.com.dao.order.OrderDao
import diasconnect.seller.com.dao.order.OrderDaoImpl
import diasconnect.seller.com.dao.product.ProductDao
import diasconnect.seller.com.dao.product.ProductDaoImpl
import diasconnect.seller.com.dao.user.UserDao
import diasconnect.seller.com.dao.user.UserDaoImpl
import diasconnect.seller.com.repository.auth.AuthRepository
import diasconnect.seller.com.repository.auth.AuthRepositoryImpl
import diasconnect.seller.com.repository.order.OrderRepository
import diasconnect.seller.com.repository.order.OrderRepositoryImpl
import diasconnect.seller.com.repository.product.ProductRepository
import diasconnect.seller.com.repository.product.ProductRepositoryImpl
import org.koin.dsl.module

val appModule = module {


    single <UserDao>{ UserDaoImpl() }
    single  <AuthRepository>{ AuthRepositoryImpl(get()) }

    single <ProductDao>{ ProductDaoImpl() }

    single <ProductRepository>{ ProductRepositoryImpl(get(),get()) }

    single<OrderDao>{ OrderDaoImpl() }


    single <OrderRepository>{ OrderRepositoryImpl(get()) }

}