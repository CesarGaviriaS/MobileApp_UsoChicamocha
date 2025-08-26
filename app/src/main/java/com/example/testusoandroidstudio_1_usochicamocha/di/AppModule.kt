package com.example.testusoandroidstudio_1_usochicamocha.di

import android.content.Context
import androidx.room.Room
import com.example.testusoandroidstudio_1_usochicamocha.data.local.AppDatabase
import com.example.testusoandroidstudio_1_usochicamocha.data.local.TokenManager
import com.example.testusoandroidstudio_1_usochicamocha.data.local.dao.FormDao
import com.example.testusoandroidstudio_1_usochicamocha.data.local.dao.LogDao
import com.example.testusoandroidstudio_1_usochicamocha.data.local.dao.MachineDao
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.ApiService
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.AuthInterceptor
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.TokenAuthenticator
import com.example.testusoandroidstudio_1_usochicamocha.data.repository.AuthRepositoryImpl
import com.example.testusoandroidstudio_1_usochicamocha.data.repository.FormRepositoryImpl
import com.example.testusoandroidstudio_1_usochicamocha.data.repository.LogRepositoryImpl
import com.example.testusoandroidstudio_1_usochicamocha.data.repository.MachineRepositoryImpl
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.AuthRepository
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.FormRepository
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.LogRepository
import com.example.testusoandroidstudio_1_usochicamocha.domain.repository.MachineRepository
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.auth.LoginUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.auth.LogoutUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.auth.ValidateSessionUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.form.GetPendingFormsUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.log.GetLogsUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.machine.GetLocalMachinesUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.machine.SyncMachinesUseCase
import com.example.testusoandroidstudio_1_usochicamocha.util.AppLogger
import com.example.testusoandroidstudio_1_usochicamocha.util.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://pdxs8r4k-8080.use2.devtunnels.ms/"+"api/v1/"


    @Provides
    @Singleton
    fun provideApiService(tokenAuthenticator: TokenAuthenticator, authInterceptor: AuthInterceptor): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor = NetworkMonitor(context)

    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService, tokenManager: TokenManager): AuthRepository = AuthRepositoryImpl(apiService, tokenManager)

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository, logger: AppLogger): LoginUseCase {
        return LoginUseCase(authRepository, logger)
    }

    @Provides
    @Singleton
    fun provideSyncMachinesUseCase(machineRepository: MachineRepository, logger: AppLogger): SyncMachinesUseCase {
        return SyncMachinesUseCase(machineRepository, logger)
    }

    @Provides
    @Singleton
    fun provideValidateSessionUseCase(
        tokenManager: TokenManager,
        authRepository: AuthRepository,
        networkMonitor: NetworkMonitor
    ): ValidateSessionUseCase {
        return ValidateSessionUseCase(tokenManager, authRepository, networkMonitor)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase = LogoutUseCase(authRepository)

    @Provides
    @Singleton
    fun provideFormRepository(
        formDao: FormDao,
        apiService: ApiService
    ): FormRepository = FormRepositoryImpl(formDao, apiService)

    @Provides
    @Singleton
    fun provideGetPendingFormsUseCase(formRepository: FormRepository): GetPendingFormsUseCase = GetPendingFormsUseCase(formRepository)

    @Provides
    @Singleton
    fun provideFormDao(appDatabase: AppDatabase): FormDao = appDatabase.formDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor = AuthInterceptor(tokenManager)

    @Provides
    @Singleton
    fun provideMachineRepository(apiService: ApiService, machineDao: MachineDao): MachineRepository = MachineRepositoryImpl(apiService, machineDao)

    @Provides
    @Singleton
    fun provideGetLocalMachinesUseCase(machineRepository: MachineRepository): GetLocalMachinesUseCase = GetLocalMachinesUseCase(machineRepository)

    @Provides
    @Singleton
    fun provideMachineDao(appDatabase: AppDatabase): MachineDao = appDatabase.machineDao()

    @Provides
    @Singleton
    fun provideLogDao(appDatabase: AppDatabase): LogDao = appDatabase.logDao()

    @Provides
    @Singleton
    fun provideLogRepository(logDao: LogDao): LogRepository = LogRepositoryImpl(logDao)

    @Provides
    @Singleton
    fun provideGetLogsUseCase(logRepository: LogRepository): GetLogsUseCase = GetLogsUseCase(logRepository)
}
