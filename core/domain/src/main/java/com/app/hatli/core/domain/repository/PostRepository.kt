package com.app.hatli.core.domain.repository

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun observeCachedPosts(): Flow<List<Post>>
    suspend fun refreshPosts(): Result<List<Post>>
    suspend fun getPost(id: Int): Result<Post>
}
