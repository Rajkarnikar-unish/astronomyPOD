package com.example.astronomypod.utils


// Sealed is kind of abstract class which only allows certain classes to inherit it
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
) {

    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    class Loading<T>(): Resource<T>()

}