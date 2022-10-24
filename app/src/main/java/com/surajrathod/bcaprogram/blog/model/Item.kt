package com.surajrathod.bcaprogram.blog.model

data class Item(
    val author: Author,
    val blog: Blog,
    val content: String,
    val etag: String,
    val id: String,
    val kind: String,
    val labels: List<String>,
    val published: String,
    val replies: Replies,
    val selfLink: String,
    val title: String,
    val updated: String,
    val url: String
)