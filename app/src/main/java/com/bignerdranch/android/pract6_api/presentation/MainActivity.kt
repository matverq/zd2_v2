/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.bignerdranch.android.pract6_api.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip

import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bignerdranch.android.pract6_api.presentation.theme.Pract6_apiTheme
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            Pract6_apiTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "start_screen") {
                    composable("start_screen") { StartScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("menu") { MenuScreen(navController) }
                    composable("discussions") { DiscussionsScreen(navController) }
                    composable("movies") { MoviesScreen() }
                    composable("favorites") { FavoritesScreen() }
                }
            }
        }
    }
}

@Composable
fun StartScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") {
            popUpTo("start_screen") { inclusive = true }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "WorldCinema",
            color = Color(0xFF2196F3),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TimeText()
        }
        item {
            Text(
                text = "WorldCinema",
                color = Color(0xFF2196F3),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
            )
        }
        item {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
        item {
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 8.dp)
            )
        }
        item {
            Button(
                onClick = {
                    if (password.length >= 8 && email.isNotEmpty()) {
                        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        prefs.edit().putString("email", email).apply()
                        navController.navigate("menu")
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(0.8f)
            ) {
                Text("Войти")
            }
        }
    }
}

@Composable
fun MenuScreen(navController: NavController) {
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TimeText()
        }
        item {
            Text(
                text = "Меню",
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
        }
        item {
            Chip(
                label = { Text("Обсуждения") },
                onClick = { navController.navigate("discussions") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
        item {
            Chip(
                label = { Text("Избранное") },
                onClick = { navController.navigate("favorites") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
fun DiscussionsScreen(navController: NavController) {
    val discussions = listOf("Lucifer", "The Witcher", "LOST", "The Magicians")
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TimeText()
        }
        item {
            Text(
                text = "Обсуждения",
                color = Color(0xFF2196F3),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        items(discussions) { title ->
            Chip(
                label = { Text(title) },
                onClick = { if (title == "The Witcher") navController.navigate("movies") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
fun MoviesScreen() {
    val apiKey = "d68c3058"
    val searchQuery = "Witcher"
    var movieList by remember { mutableStateOf<List<Movie>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val url = "https://www.omdbapi.com/?apikey=$apiKey&s=$searchQuery"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray("Search")
                    val tempList = mutableListOf<Movie>()
                    val gson = Gson()
                    for (i in 0 until jsonArray.length()) {
                        val movie = gson.fromJson(jsonArray.getJSONObject(i).toString(), Movie::class.java)
                        tempList.add(movie)
                    }
                    movieList = tempList
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Ошибка: ${e.message}")
                }
            },
            { error -> Log.e("API_ERROR", "Ошибка сети: ${error.message}") }
        )
        queue.add(request)
    }

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TimeText()
        }
        item {
            Text(
                text = "Новое",
                color = Color(0xFF2196F3),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        items(movieList) { movie ->
            Chip(
                label = { Text(movie.Title) },
                icon = {
                    AndroidView(
                        factory = { ctx ->
                            ImageView(ctx).apply {
                                layoutParams = android.view.ViewGroup.LayoutParams(48.dp.value.toInt(), 72.dp.value.toInt())
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }
                        },
                        update = { imageView -> Picasso.get().load(movie.Poster).into(imageView) },
                        modifier = Modifier
                            .size(48.dp, 72.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                },
                onClick = { },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
fun FavoritesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        TimeText()
        Text(text = "Избранное пусто", color = Color.Gray)
    }
}

@androidx.compose.ui.tooling.preview.Preview(device = androidx.compose.ui.tooling.preview.Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun PreviewMenuScreen() {
    Pract6_apiTheme {
        val navController = rememberNavController()
        MenuScreen(navController)
    }
}