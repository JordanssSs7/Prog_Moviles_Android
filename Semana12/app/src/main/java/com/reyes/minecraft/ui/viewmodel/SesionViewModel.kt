package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.AuthRepository
import com.reyes.minecraft.data.repository.ResultadoLogin
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Sesión de toda la app. Exito(null) significa "no hay sesión": se muestra el login. */
class SesionViewModel(private val auth: AuthRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Usuario?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Usuario?>> = _estado.asStateFlow()

    private val _ingresando = MutableStateFlow(false)
    val ingresando: StateFlow<Boolean> = _ingresando.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    init {
        // R11: si Firebase guardó la sesión, la restauramos sin pedir la clave otra vez.
        viewModelScope.launch { _estado.value = UiState.Exito(auth.restaurarSesion()) }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _errorLogin.value = "Escribe tu correo y tu contraseña"
            return
        }
        viewModelScope.launch {
            _ingresando.value = true
            _errorLogin.value = null
            when (val r = auth.iniciarSesion(correo.trim(), clave)) {
                is ResultadoLogin.Exito -> _estado.value = UiState.Exito(r.usuario)
                ResultadoLogin.CredencialesInvalidas -> _errorLogin.value = "Correo o contraseña incorrectos"
                ResultadoLogin.SinConexion -> _errorLogin.value = "Sin conexión a internet"
                ResultadoLogin.SinRol -> _errorLogin.value = "Tu usuario no tiene un rol asignado en Firestore"
                is ResultadoLogin.Otro -> _errorLogin.value = r.mensaje
            }
            _ingresando.value = false
        }
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _errorLogin.value = null
        _estado.value = UiState.Exito(null)
    }
}
