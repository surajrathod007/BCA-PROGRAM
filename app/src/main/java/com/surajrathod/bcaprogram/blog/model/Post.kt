package com.surajrathod.bcaprogram.blog.model

data class Post(
    val etag: String,
    val items: List<Item>,
    val kind: String
)