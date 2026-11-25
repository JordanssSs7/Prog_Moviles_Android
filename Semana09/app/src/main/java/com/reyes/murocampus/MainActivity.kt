package com.reyes.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.reyes.murocampus.data.PostRepositoryRemote
import com.reyes.murocampus.data.remote.RetrofitClient
import com.reyes.murocampus.ui.PantallaLista
import com.reyes.murocampus.ui.theme.MurocampusTheme
import com.reyes.murocampus.viewmodel.PostsViewModel
import com.reyes.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repositorio = PostRepositoryRemote(RetrofitClient.api)
        val factoria = PostsViewModelFactory(repositorio)
        val vm = ViewModelProvider(this, factoria)[PostsViewModel::class.java]

        setContent {
            MurocampusTheme {
                PantallaLista(vm = vm)
            }
        }
    }
}
