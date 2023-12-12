package com.example.socialmediaapp.signIn

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: Any?,
    val bio: Any?
)
