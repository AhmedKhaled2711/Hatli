package com.app.hatli.core.data.datasource.local

import com.app.hatli.core.database.dao.PostDao
import com.app.hatli.core.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLocalDataSource @Inject constructor(
    private val postDao: PostDao,
) {
    fun observePosts(): Flow<List<PostEntity>> = postDao.observeAll()
    suspend fun getPost(id: Int): PostEntity? = postDao.getById(id)
    suspend fun upsertAll(posts: List<PostEntity>) = postDao.upsertAll(posts)
    suspend fun upsert(post: PostEntity) = postDao.upsert(post)
    suspend fun clear() = postDao.clear()
    suspend fun delete(id: Int) = postDao.deleteById(id)
}
