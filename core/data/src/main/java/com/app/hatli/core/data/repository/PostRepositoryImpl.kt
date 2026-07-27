package com.app.hatli.core.data.repository

import com.app.hatli.core.common.dispatcher.IoDispatcher
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.data.datasource.local.PostLocalDataSource
import com.app.hatli.core.data.datasource.remote.PostRemoteDataSource
import com.app.hatli.core.data.mapper.toDomain
import com.app.hatli.core.data.mapper.toEntity
import com.app.hatli.core.domain.repository.PostRepository
import com.app.hatli.core.model.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val remote: PostRemoteDataSource,
    private val local: PostLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PostRepository {

    override fun observeCachedPosts(): Flow<List<Post>> =
        local.observePosts().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshPosts(): Result<List<Post>> = withContext(ioDispatcher) {
        try {
            val remotePosts = remote.fetchPosts()
            local.upsertAll(remotePosts.map { it.toEntity() })
            Result.Success(remotePosts.map { it.toDomain() })
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }

    override suspend fun getPost(id: Int): Result<Post> = withContext(ioDispatcher) {
        try {
            local.getPost(id)?.let { return@withContext Result.Success(it.toDomain()) }
            val remotePost = remote.fetchPost(id)
            local.upsert(remotePost.toEntity())
            Result.Success(remotePost.toDomain())
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }
}
