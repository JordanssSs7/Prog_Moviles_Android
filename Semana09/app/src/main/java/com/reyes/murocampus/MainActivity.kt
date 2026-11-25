package com.reyes.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.reyes.murocampus.data.PostRepositoryRemote
import com.reyes.murocampus.data.remote.RetrofitClient
import com.reyes.murocampus.ui.PantallaCrear
import com.reyes.murocampus.ui.PantallaDetalle
import com.reyes.murocampus.ui.PantallaLista
import com.reyes.murocampus.ui.theme.MurocampusTheme
import com.reyes.murocampus.viewmodel.PostsViewModel
import com.reyes.murocampus.viewmodel.PostsViewModelFactory

private val IdSeleccionadoSaver = Saver<Int?, Int>(
    save = { it ?: -1 },
    restore = { if (it == -1) null else it }
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repositorio = PostRepositoryRemote(RetrofitClient.api)
        val factoria = PostsViewModelFactory(repositorio)
        val vm = ViewModelProvider(this, factoria)[PostsViewModel::class.java]

        setContent {
            MurocampusTheme {
                var idSeleccionado by rememberSaveable(stateSaver = IdSeleccionadoSaver) {
                    mutableStateOf<Int?>(null)
                }
                var mostrandoCrear by rememberSaveable { mutableStateOf(false) }

                when {
                    idSeleccionado != null -> PantallaDetalle(
                        vm = vm,
                        id = idSeleccionado ?: 0,
                        onVolver = { idSeleccionado = null }
                    )

                    mostrandoCrear -> PantallaCrear(
                        vm = vm,
                        onVolver = { mostrandoCrear = false }
                    )

                    else -> PantallaLista(
                        vm = vm,
                        onPostClick = { id ->
                            vm.abrir(id)
                            idSeleccionado = id
                        },
                        onCrearClick = { mostrandoCrear = true }
                    )
                }
            }
        }
    }
}
