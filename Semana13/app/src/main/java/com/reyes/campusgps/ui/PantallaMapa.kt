package com.reyes.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.campusgps.model.Lugar
import com.reyes.campusgps.model.LugarConDistancia
import com.reyes.campusgps.model.UbicacionUi
import com.reyes.campusgps.ui.theme.FondoHoja
import com.reyes.campusgps.ui.theme.GrisClaro
import com.reyes.campusgps.ui.theme.Lima
import com.reyes.campusgps.ui.theme.Negro
import com.reyes.campusgps.ui.theme.Superficie
import com.reyes.campusgps.ui.theme.TextoSecundario
import com.reyes.campusgps.util.formatearDistancia
import com.reyes.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    // Mejora 1: el mapa y la lista muestran solo los lugares de la categoría elegida
    val lugares by vm.lugaresVisibles.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()
    val masCercano by vm.masCercano.collectAsStateWithLifecycle()
    val categoria by vm.categoria.collectAsStateWithLifecycle()
    // Mejora 2: radio de cercanía
    val radio by vm.radio.collectAsStateWithLifecycle()
    val dentroDelRadio by vm.dentroDelRadio.collectAsStateWithLifecycle()

    // Paso 2: permiso en tiempo de ejecución
    val contexto = LocalContext.current

    fun tienePermiso(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    // Mejora 3: el punto azul nativo solo se activa con el permiso concedido
    var permisoConcedido by remember { mutableStateOf(tienePermiso()) }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        permisoConcedido = resultado.values.any { it }
        if (permisoConcedido) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        // Se revisa otra vez por si el permiso cambió desde Ajustes
        permisoConcedido = tienePermiso()
        if (permisoConcedido) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Botón principal: píldora negra
    val botonBuscar = @Composable { modificador: Modifier ->
        Button(
            onClick = { alPulsar() },
            enabled = estado !is UbicacionUi.Buscando,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Negro,
                contentColor = Color.White,
                disabledContainerColor = Negro.copy(alpha = 0.6f),
                disabledContentColor = Color.White
            ),
            modifier = modificador.height(52.dp)
        ) {
            if (estado is UbicacionUi.Buscando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Lima
                )
            } else {
                Text("Obtener mi ubicación", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }

    Scaffold(containerColor = FondoHoja) { padding ->
        val e = estado
        if (e is UbicacionUi.Error) {
            // Paso 8: sin mapa; título, tarjeta de error y la acción
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("CampusGPS", fontWeight = FontWeight.Bold, fontSize = 28.sp)
                MensajeError(error = e, onReintentar = { alPulsar() })
                botonBuscar(Modifier.fillMaxWidth())
            }
        } else {
            val altoBarraEstado = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

            // El mapa llega hasta arriba (detrás de la barra de estado)
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Paso 3: el mapa ocupa el espacio libre
                    MapaUbicacion(
                        estado = estado,
                        lugares = lugares,
                        seleccionado = seleccionado,
                        masCercano = masCercano,
                        radio = radio,
                        puntoAzulNativo = permisoConcedido,
                        paddingSuperior = altoBarraEstado + 84.dp,
                        paddingInferior = SOLAPE_HOJA,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Tarjeta flotante con el lugar más cercano (Paso 7)
                    TarjetaEstado(
                        estado = estado,
                        lugares = lugares,
                        masCercano = masCercano,
                        radio = radio,
                        dentroDelRadio = dentroDelRadio,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .statusBarsPadding()
                            .padding(top = 10.dp, start = 24.dp, end = 24.dp)
                    )
                }

                // Hoja inferior que se monta sobre el mapa
                Surface(
                    color = FondoHoja,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    shadowElevation = 16.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .subirSobreElMapa(SOLAPE_HOJA)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Asa de la hoja
                        Box(
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(40.dp)
                                .height(4.dp)
                                .background(GrisClaro, RoundedCornerShape(2.dp))
                        )

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                "Lugares cercanos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "${lugares.size} lugares",
                                color = TextoSecundario,
                                fontSize = 13.sp
                            )
                        }

                        // Mejora 1: categorías
                        FiltroCategorias(
                            categorias = vm.categorias,
                            seleccionada = categoria,
                            onElegir = { vm.filtrar(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        // Mejora 2: radio
                        SelectorRadio(
                            radios = vm.radios,
                            seleccionado = radio,
                            onElegir = { vm.cambiarRadio(it) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Botones: buscar (negro) e ir al más cercano (lima) - Paso 7
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            botonBuscar(Modifier.weight(1.2f))
                            Button(
                                onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                                enabled = masCercano != null,
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Lima,
                                    contentColor = Negro,
                                    disabledContainerColor = GrisClaro,
                                    disabledContentColor = TextoSecundario
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Text("Ir al más cercano", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            }
                        }

                        // Paso 6: lista con altura fija
                        ListaLugares(
                            lugares = lugares,
                            seleccionado = seleccionado,
                            masCercanoId = masCercano?.id,
                            onClick = { vm.seleccionar(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            dentroDelRadio = dentroDelRadio
                        )
                    }
                }
            }
        }
    }
}

// Tarjeta blanca que flota sobre el mapa: lugar más cercano y barra de progreso del radio
@Composable
private fun TarjetaEstado(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    masCercano: Lugar?,
    radio: Int?,
    dentroDelRadio: Set<Int>,
    modifier: Modifier = Modifier
) {
    val metros = lugares.firstOrNull { it.lugar.id == masCercano?.id }?.metros

    Surface(
        color = Superficie,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "CAMPUSGPS",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                color = TextoSecundario
            )
            val texto = when {
                masCercano != null && metros != null ->
                    "Más cercano: ${masCercano.nombre} (${formatearDistancia(metros)})"
                estado is UbicacionUi.Buscando -> "Buscando tu ubicación..."
                else -> "Pulsa el botón para empezar"
            }
            Text(
                texto,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Mejora 2: cuántos lugares visibles quedan dentro del radio
            if (radio != null && estado is UbicacionUi.Lista && lugares.isNotEmpty()) {
                val dentro = lugares.count { it.lugar.id in dentroDelRadio }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BarraProgreso(
                        fraccion = dentro.toFloat() / lugares.size,
                        modifier = Modifier.width(120.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "$dentro/${lugares.size} en $radio m",
                        fontSize = 11.sp,
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}

// Barra lima sobre un fondo gris
@Composable
private fun BarraProgreso(fraccion: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(6.dp)
            .background(GrisClaro, CircleShape)
    ) {
        Box(
            Modifier
                .fillMaxWidth(fraccion.coerceIn(0f, 1f))
                .height(6.dp)
                .background(Lima, CircleShape)
        )
    }
}

// Cuánto se monta la hoja inferior sobre el mapa
private val SOLAPE_HOJA = 24.dp

// Sube la hoja sobre el mapa sin dejar hueco abajo: ocupa menos alto en la columna
// (el mapa gana ese espacio) y se dibuja corrida hacia arriba.
private fun Modifier.subirSobreElMapa(solape: Dp) = layout { medible, restricciones ->
    val pieza = medible.measure(restricciones)
    val px = solape.roundToPx()
    layout(pieza.width, (pieza.height - px).coerceAtLeast(0)) {
        pieza.place(0, -px)
    }
}
