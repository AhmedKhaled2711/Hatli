package com.app.hatli.core.domain.usecase

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.PostRepository
import com.app.hatli.core.model.Post
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(): Result<List<Post>> = repository.refreshPosts()
}

class ObservePostsUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    operator fun invoke() = repository.observeCachedPosts()
}

class GetPostUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(id: Int): Result<Post> = repository.getPost(id)
}
