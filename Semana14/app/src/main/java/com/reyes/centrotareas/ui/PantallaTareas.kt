package com.reyes.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.centrotareas.model.CargaUi
import com.reyes.centrotareas.model.PrimosUi
import com.reyes.centrotareas.ui.theme.Amarillo
import com.reyes.centrotareas.ui.theme.Azul
import com.reyes.centrotareas.ui.theme.AzulCielo
import com.reyes.centrotareas.ui.theme.AzulPalido
import com.reyes.centrotareas.ui.theme.Blanco
import com.reyes.centrotareas.ui.theme.Crema
import com.reyes.centrotareas.ui.theme.Papel
import com.reyes.centrotareas.ui.theme.RojoFondo
import com.reyes.centrotareas.ui.theme.RojoTexto
import com.reyes.centrotareas.ui.theme.Tinta
import com.reyes.centrotareas.ui.theme.TintaSuave
import com.reyes.centrotareas.viewmodel.TareasViewModel

@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val tiempoMaximo by vm.tiempoMaximo.collectAsState()
    val tiempos by vm.tiempos.collectAsState()
    var textoN by rememberSaveable { mutableStateOf("500000") }
    var nCalculado by rememberSaveable { mutableIntStateOf(0) }
    // Prueba de vida: si deja de sumar, la pantalla está congelada
    var toques by rememberSaveable { mutableIntStateOf(0) }

    val calculando = primos is PrimosUi.Calculando
    val cargando = carga is CargaUi.Cargando
    val enCurso = listOf(corriendo, calculando, cargando).count { it }
    val animo = when {
        carga is CargaUi.Error -> Animo.Triste
        enCurso > 0 -> Animo.Ocupado
        else -> Animo.Feliz
    }

    Scaffold(
        containerColor = Crema,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BarraInferior(
                toques = toques,
                enCurso = enCurso,
                listas = tiempos.values.sumOf { it.veces },
                onTocar = { toques++ }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            EncabezadoOndulado("CentroTareas", "La pantalla nunca se congela")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ----- Mascota que reacciona al estado
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Mascota(animo, Modifier.size(96.dp))
                    Spacer(Modifier.size(10.dp))
                    Text(
                        text = when (animo) {
                            Animo.Feliz -> "¡Nunca es tarde para empezar una tarea!"
                            Animo.Ocupado -> "Estoy trabajando… ¡tú sigue tocando!"
                            Animo.Triste -> "Uy, algo falló. ¡Reintentemos!"
                        },
                        color = Tinta,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Papel)
                            .border(2.dp, Tinta, RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    )
                }

                // ----- Tarea con progreso (pasos 1 a 4)
                Tarjeta(titulo = "Tarea con progreso", fondo = AzulPalido, etiqueta = "Pasos 1–4") {
                    // cuadro tipo temporizador con el porcentaje
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Papel)
                            .border(2.dp, Tinta, RoundedCornerShape(18.dp))
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${(progreso * 100).toInt()} %",
                            color = Tinta,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(mensaje, color = TintaSuave, fontSize = 14.sp)
                    }
                    BarraPastilla(
                        progreso = progreso,
                        pista = Papel,
                        relleno = Azul,
                        rayas = Blanco.copy(alpha = 0.25f)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        BotonPastilla(
                            texto = "Iniciar",
                            onClick = { vm.tareaLarga() },
                            fondo = Azul,
                            contenido = Blanco,
                            enabled = !corriendo
                        )
                        BotonPastilla(
                            texto = "Cancelar",
                            onClick = { vm.cancelar() },
                            fondo = Papel,
                            contenido = Tinta,
                            enabled = corriendo
                        )
                    }
                }

                // ----- Contar números primos (paso 5)
                Tarjeta(titulo = "Contar números primos", fondo = Papel, etiqueta = "Paso 5") {
                    OutlinedTextField(
                        value = textoN,
                        // ignora lo que no sean dígitos
                        onValueChange = { nuevo -> if (nuevo.all { it.isDigit() }) textoN = nuevo },
                        label = { Text("Hasta el número") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Blanco,
                            unfocusedContainerColor = Blanco,
                            focusedBorderColor = Azul,
                            unfocusedBorderColor = Tinta,
                            focusedLabelColor = Azul,
                            unfocusedLabelColor = TintaSuave,
                            cursorColor = Azul
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        BotonPastilla(
                            texto = "Contar",
                            onClick = {
                                val n = textoN.toIntOrNull()
                                if (n != null && n >= 2) { // ignora menores que 2
                                    nCalculado = n
                                    vm.contar(n)
                                }
                            },
                            fondo = Amarillo,
                            contenido = Tinta,
                            enabled = !calculando
                        )
                        BotonPastilla(
                            texto = "Cancelar",
                            onClick = { vm.cancelarPrimos() },
                            fondo = Papel,
                            contenido = Tinta,
                            enabled = calculando
                        )
                    }
                    when (val p = primos) {
                        PrimosUi.Inicial -> Unit
                        PrimosUi.Calculando -> Text("Calculando...", color = TintaSuave, fontSize = 14.sp)
                        PrimosUi.Cancelado -> Text("Cancelado", color = TintaSuave, fontSize = 14.sp)
                        is PrimosUi.Listo -> Recuadro(
                            fondo = Amarillo.copy(alpha = 0.35f),
                            texto = Tinta,
                            lineas = listOf(
                                "Primos hasta $nCalculado: ${p.cantidad}",
                                "Tiempo: ${p.milisegundos} ms"
                            )
                        )
                    }
                }

                // ----- Dos cargas (pasos 6 y 7)
                Tarjeta(titulo = "Dos cargas", fondo = AzulCielo, etiqueta = "Pasos 6–7") {
                    Interruptor("Simular error", simularError) { vm.cambiarSimularError(it) }
                    // Mejora 2: tiempo máximo con withTimeout
                    Interruptor("Tiempo máximo 4 s", tiempoMaximo) { vm.cambiarTiempoMaximo(it) }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        BotonPastilla(
                            texto = "Una por una",
                            onClick = { vm.cargarUnaPorUna() },
                            fondo = Papel,
                            contenido = Tinta,
                            enabled = !cargando
                        )
                        BotonPastilla(
                            texto = "A la vez",
                            onClick = { vm.cargarALaVez() },
                            fondo = Tinta,
                            contenido = Blanco,
                            enabled = !cargando
                        )
                    }
                    when (val c = carga) {
                        CargaUi.Inicial -> Unit
                        CargaUi.Cargando -> CircularProgressIndicator(
                            color = Tinta,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        is CargaUi.Exito -> Recuadro(
                            fondo = Papel,
                            texto = Tinta,
                            lineas = listOf(
                                "Modo: ${c.modo}",
                                "Perfil: ${c.perfil}",
                                "Pedidos: ${c.pedidos}",
                                "Tiempo: ${c.milisegundos} ms"
                            )
                        )
                        is CargaUi.Error -> {
                            Recuadro(fondo = RojoFondo, texto = RojoTexto, lineas = listOf(c.mensaje))
                            BotonPastilla(
                                texto = "Reintentar",
                                onClick = { vm.cargarALaVez() },
                                fondo = Papel,
                                contenido = Tinta
                            )
                        }
                    }
                }

                // ----- Mejora 3: comparación de tiempos
                Tarjeta(titulo = "Comparación de tiempos", fondo = Papel, etiqueta = "Mejora 3") {
                    val serie = tiempos["Una por una"]
                    val paralelo = tiempos["A la vez"]
                    FilaCheck(
                        if (serie == null) "Una por una: sin datos"
                        else "Una por una · ${serie.veces} vez(es) · prom. ${serie.promedioMs} ms · últ. ${serie.ultimoMs} ms",
                        serie != null
                    )
                    FilaCheck(
                        if (paralelo == null) "A la vez: sin datos"
                        else "A la vez · ${paralelo.veces} vez(es) · prom. ${paralelo.promedioMs} ms · últ. ${paralelo.ultimoMs} ms",
                        paralelo != null
                    )
                    if (tiempos.isNotEmpty()) {
                        val maximo = maxOf(serie?.promedioMs ?: 0L, paralelo?.promedioMs ?: 0L)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            BarraVertical("Una por una", serie?.promedioMs, maximo, Azul, Blanco.copy(alpha = 0.25f))
                            BarraVertical("A la vez", paralelo?.promedioMs, maximo, Amarillo, Tinta.copy(alpha = 0.15f))
                        }
                        if (serie != null && paralelo != null) {
                            Recuadro(
                                fondo = AzulPalido,
                                texto = Tinta,
                                lineas = listOf(
                                    "A la vez ahorra ${serie.promedioMs - paralelo.promedioMs} ms en promedio",
                                    "Es ${"%.1f".format(serie.promedioMs.toDouble() / paralelo.promedioMs)} veces más rápido"
                                )
                            )
                        }
                        BotonPastilla(
                            texto = "Limpiar",
                            onClick = { vm.limpiarTiempos() },
                            fondo = Papel,
                            contenido = Tinta
                        )
                    } else {
                        Text(
                            "Aún no hay cargas exitosas. Prueba \"Una por una\" y \"A la vez\".",
                            color = TintaSuave,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
