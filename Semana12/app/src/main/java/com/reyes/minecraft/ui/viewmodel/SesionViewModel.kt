package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.AuthRepository
import com.reyes.minecraft.data.repository.ResultadoLogin
import com.reyes.minecraft.data.repository.ResultadoRegistro
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Sesión de toda la app. Exito(null) significa "no hay sesión": se muestra bienvenida / login. */
class SesionViewModel(private val auth: AuthRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Usuario?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Usuario?>> = _estado.asStateFlow()

    private val _ingresando = MutableStateFlow(false)
    val ingresando: StateFlow<Boolean> = _ingresando.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    private val _registrando = MutableStateFlow(false)
    val registrando: StateFlow<Boolean> = _registrando.asStateFlow()

    private val _errorRegistro = MutableStateFlow<String?>(null)
    val errorRegistro: StateFlow<String?> = _errorRegistro.asStateFlow()

    /** Correo de la cuenta recién creada: la UI manda a la persona al login con este correo. */
    private val _cuentaCreada = MutableStateFlow<String?>(null)
    val cuentaCreada: StateFlow<String?> = _cuentaCreada.asStateFlow()

    /** Nombre del jugador que acaba de iniciar sesión: la UI muestra "¡Bienvenido!" una sola vez. */
    private val _bienvenida = MutableStateFlow<String?>(null)
    val bienvenida: StateFlow<String?> = _bienvenida.asStateFlow()

    /** Correo recién registrado: la UI muestra "¡Cuenta creada!" una sola vez. */
    private val _confirmacionRegistro = MutableStateFlow<String?>(null)
    val confirmacionRegistro: StateFlow<String?> = _confirmacionRegistro.asStateFlow()

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
                is ResultadoLogin.Exito -> {
                    _cuentaCreada.value = null
                    _bienvenida.value = r.usuario.jugador
                    _estado.value = UiState.Exito(r.usuario)
                }
                ResultadoLogin.CredencialesInvalidas -> _errorLogin.value = "Correo o contraseña incorrectos"
                ResultadoLogin.SinConexion -> _errorLogin.value = "Sin conexión a internet"
                ResultadoLogin.SinRol -> _errorLogin.value = "Tu usuario no tiene un rol asignado en Firestore"
                is ResultadoLogin.Otro -> _errorLogin.value = r.mensaje
            }
            _ingresando.value = false
        }
    }

    fun registrar(correo: String, clave: String, confirmar: String, jugador: String) {
        val error = validarRegistro(correo, clave, confirmar, jugador)
        if (error != null) {
            _errorRegistro.value = error
            return
        }
        viewModelScope.launch {
            _registrando.value = true
            _errorRegistro.value = null
            when (val r = auth.registrar(correo.trim(), clave, jugador.trim())) {
                ResultadoRegistro.Exito -> {
                    _cuentaCreada.value = correo.trim()
                    _confirmacionRegistro.value = correo.trim()
                }
                ResultadoRegistro.CorreoEnUso -> _errorRegistro.value = "Ese correo ya tiene una cuenta"
                ResultadoRegistro.CorreoInvalido -> _errorRegistro.value = "El correo no es válido"
                ResultadoRegistro.ClaveDebil -> _errorRegistro.value = "La contraseña es muy débil"
                ResultadoRegistro.SinConexion -> _errorRegistro.value = "Sin conexión a internet"
                is ResultadoRegistro.Otro -> _errorRegistro.value = r.mensaje
            }
            _registrando.value = false
        }
    }

    fun limpiarErrores() {
        _errorLogin.value = null
        _errorRegistro.value = null
    }

    fun bienvenidaMostrada() {
        _bienvenida.value = null
    }

    fun confirmacionRegistroMostrada() {
        _confirmacionRegistro.value = null
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _bienvenida.value = null
        _errorLogin.value = null
        _cuentaCreada.value = null
        _estado.value = UiState.Exito(null)
    }

    private fun validarRegistro(correo: String, clave: String, confirmar: String, jugador: String): String? = when {
        !REGEX_JUGADOR.matches(jugador.trim()) -> "Nombre de jugador no válido: usa de 3 a 16 caracteres, solo letras sin tildes, números o guion bajo (_), y sin espacios"
        !REGEX_CORREO.matches(correo.trim()) -> "Escribe un correo válido"
        clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        clave != confirmar -> "Las contraseñas no coinciden"
        else -> null
    }

    private companion object {
        val REGEX_JUGADOR = Regex("^[A-Za-z0-9_]{3,16}$")
        val REGEX_CORREO = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    }
}
