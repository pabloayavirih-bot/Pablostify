package edu.ucb.pablostify.movielist.data

import edu.ucb.pablostify.movielist.domain.model.Movie
import edu.ucb.pablostify.movielist.domain.repository.MovieRepository
import edu.ucb.pablostify.movielist.domain.valueobject.MovieFilter

class MovieRepositoryImpl : MovieRepository {
    private val allMovies = listOf(
        Movie("matrix", "The Matrix", listOf("Acción", "Ciencia ficción"), "", 4.8f),
        Movie("spiderman", "Spider-Man", listOf("Acción", "Aventura"), "", 4.5f),
        Movie("interstellar", "Interstellar", listOf("Ciencia ficción", "Drama"), "", 4.9f)
    )

    override suspend fun getPopularMovies(filter: MovieFilter): Result<List<Movie>> {
        val movies = if (filter.query.isBlank()) allMovies else allMovies.filter {
            it.title.contains(filter.query, ignoreCase = true) || it.genres.any { genre -> genre.contains(filter.query, ignoreCase = true) }
        }
        return Result.success(movies)
    }
}
