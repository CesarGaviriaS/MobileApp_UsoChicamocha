package com.example.testusoandroidstudio_1_usochicamocha.data.remote

import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.FormDto
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.LoginRequest
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.LoginResponse
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.MachineDto
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.NewAccessTokenResponse
import com.example.testusoandroidstudio_1_usochicamocha.data.remote.dto.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Interfaz que define todos los endpoints de nuestro backend.
interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @POST("auth/token/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<NewAccessTokenResponse>
    @GET("machine")
    suspend fun getMachines(): Response<List<MachineDto>>
    @POST("inspection")
    suspend fun syncForm(@Body form: FormDto): Response<Unit>
}
