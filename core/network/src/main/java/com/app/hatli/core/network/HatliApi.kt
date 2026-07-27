package com.app.hatli.core.network

import com.app.hatli.core.network.dto.PostDto
import retrofit2.http.GET
import retrofit2.http.Path

interface HatliApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): PostDto
}
