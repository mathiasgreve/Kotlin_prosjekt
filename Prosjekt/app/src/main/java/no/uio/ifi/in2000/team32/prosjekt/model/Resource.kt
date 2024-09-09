package no.uio.ifi.in2000.team32.prosjekt.model

/**
 * A sealed class that represents the state of a resource that is being loaded.
 *
 * @param <T> The type of data held by this resource.
 */
sealed class Resource<T> {

    /**
     * Represents the loading state of a resource.
     */
    class Loading<T> : Resource<T>()

    /**
     * Represents the success state of a resource with the loaded data.
     *
     * @param data The data that was loaded successfully.
     */
    class Success<T>(val data: T) : Resource<T>()

    /**
     * Represents the error state of a resource with an exception.
     *
     * @param exception The exception that occurred during loading.
     */
    class Error<T>(val exception: Exception) : Resource<T>()
}