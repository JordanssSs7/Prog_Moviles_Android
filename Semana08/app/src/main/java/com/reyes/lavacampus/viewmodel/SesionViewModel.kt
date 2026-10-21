package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.entity.Usuario
import com.reyes.lavacampus.data.prefs.PreferenciasStore
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /** true cuando ya se intento recuperar el perfil guardado (el Splash espera esto). */
    private val _sesionLista = MutableStateFlow(false)
    val sesionLista: StateFlow<Boolean> = _sesionLista.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        // Si ya hay un perfil guardado, se recupera el usuario y se salta el login
        viewModelScope.launch {
            val perfil = prefs.perfilActivo.first()
            val id = prefs.usuarioId.first()
            if (perfil.isNotEmpty() && id > 0) {
                _usuarioActual.value = repo.obtenerUsuario(id)
            }
            _sesionLista.value = true
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name -> _error.value =
                    if (rolSeleccionado == Rol.ADMIN) "Esta cuenta no es de administrador"
                    else "Esta cuenta no es de estudiante"
                else -> {
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    _error.value = null
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cambiarModoOscuro(activo: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(activo) }
    }

    fun cambiarNotificaciones(activo: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(activo) }
    }

    fun cambiarRecibirPedidos(activo: Boolean) {
        viewModelScope.launch { prefs.guardarRecibirPedidos(activo) }
    }

    /** Guarda el otro perfil y avisa para navegar a su grafo (modo practica: ambos conviven). */
    fun cambiarPerfil(alTerminar: (Rol) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val otroRol = if (actual.rol == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN
        viewModelScope.launch {
            val otro = repo.obtenerUsuarioPorRol(otroRol.name) ?: return@launch
            prefs.guardarSesion(otro.rol, otro.id)
            _usuarioActual.value = otro
            alTerminar(otroRol)
        }
    }

    fun cerrarSesion(alTerminar: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _error.value = null
            _usuarioActual.value = null
            alTerminar()
        }
    }
}
