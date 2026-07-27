package com.app.hatli.core.data.mapper

import com.app.hatli.core.database.entity.PostEntity
import com.app.hatli.core.model.Post
import com.app.hatli.core.network.dto.PostDto

fun PostDto.toEntity(): PostEntity = PostEntity(
    id = id,
    userId = userId,
    title = title,
    body = body,
)

fun PostEntity.toDomain(): Post = Post(
    id = id,
    userId = userId,
    title = title,
    body = body,
)

fun PostDto.toDomain(): Post = Post(
    id = id,
    userId = userId,
    title = title,
    body = body,
)
