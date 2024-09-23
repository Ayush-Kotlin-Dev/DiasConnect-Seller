package diasconnect.seller.com.di

import diasconnect.seller.com.dao.user.UserDao
import diasconnect.seller.com.dao.user.UserDaoImpl
import diasconnect.seller.com.repository.auth.AuthRepository
import diasconnect.seller.com.repository.auth.AuthRepositoryImpl
import org.koin.dsl.module

val appModule = module {


    single <UserDao>{ UserDaoImpl() }
    single  <AuthRepository>{ AuthRepositoryImpl(get()) }

}