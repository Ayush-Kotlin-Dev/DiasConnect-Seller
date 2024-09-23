package diasconnect.seller.com.di

import diasconnect.seller.com.dao.product.ProductDao
import diasconnect.seller.com.dao.product.ProductDaoImpl
import diasconnect.seller.com.dao.user.UserDao
import diasconnect.seller.com.dao.user.UserDaoImpl
import diasconnect.seller.com.repository.auth.AuthRepository
import diasconnect.seller.com.repository.auth.AuthRepositoryImpl
import diasconnect.seller.com.repository.product.ProductRepository
import diasconnect.seller.com.repository.product.ProductRepositoryImpl
import org.koin.dsl.module

val appModule = module {


    single <UserDao>{ UserDaoImpl() }
    single  <AuthRepository>{ AuthRepositoryImpl(get()) }

    single <ProductDao>{ ProductDaoImpl() }
    single <ProductRepository>{ ProductRepositoryImpl(get()) }

}