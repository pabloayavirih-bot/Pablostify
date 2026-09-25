package edu.ucb.pablostify.movielist.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.pablostify.movielist.presentation.viewmodel.MovieListEffects
import edu.ucb.pablostify.movielist.presentation.viewmodel.MovieListEvents
import edu.ucb.pablostify.movielist.presentation.viewmodel.MovieListViewModel
import edu.ucb.pablostify.navigation.NavRoute
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MovieListScreen(navController: NavHostController, viewModel: MovieListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MovieListEffects.NavigateToDetail -> navController.navigate(NavRoute.MovieDetail(effect.movieId))
                MovieListEffects.NavigateToProfile -> navController.navigate(NavRoute.Profile)
                is MovieListEffects.ShowMessage -> Unit
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Películas populares")
            Button(onClick = { viewModel.emitEvent(MovieListEvents.OnProfile) }) { Text("Perfil") }
        }
        OutlinedTextField(
            value = state.query,
            onValueChange = { viewModel.emitEvent(MovieListEvents.OnSearchChanged(it)) },
            label = { Text("Buscar película o género") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        )
        state.errorMessage?.let { Text(it) }
        if (state.isLoading) Text("Cargando...")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.movies, key = { it.id }) { movie ->
                Card(modifier = Modifier.fillMaxWidth().clickable { viewModel.emitEvent(MovieListEvents.OnMovieSelected(movie.id)) }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(movie.title)
                        Text(movie.genres.joinToString(" · "))
                        Text("Puntuación: ${movie.rating}")
                    }
                }
            }
        }
    }
}
