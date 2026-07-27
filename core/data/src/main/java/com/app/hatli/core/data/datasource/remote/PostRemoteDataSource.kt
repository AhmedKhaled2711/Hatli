package com.app.hatli.core.data.datasource.remote

import com.app.hatli.core.network.HatliApi
import com.app.hatli.core.network.dto.PostDto
import javax.inject.Inject

class PostRemoteDataSource @Inject constructor(
    private val api: HatliApi,
) {
    suspend fun fetchPosts(): List<PostDto> = api.getPosts()
    suspend fun fetchPost(id: Int): PostDto = api.getPost(id)
}
