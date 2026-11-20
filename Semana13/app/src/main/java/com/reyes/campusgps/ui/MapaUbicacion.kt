package com.reyes.campusgps.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.reyes.campusgps.model.Lugar
import com.reyes.campusgps.model.LugarConDistancia
import com.reyes.campusgps.model.UbicacionUi
import com.reyes.campusgps.ui.theme.Lima
import com.reyes.campusgps.ui.theme.Negro

// Solo dibuja lo que recibe: no consulta Repository ni calcula distancias
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radio: Int?,
    puntoAzulNativo: Boolean,
    modifier: Modifier = Modifier,
    paddingSuperior: Dp = 0.dp,
    paddingInferior: Dp = 0.dp
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Paso 3: cuando llega tu ubicación, la cámara viaja hasta ti (zoom 16)
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val p = estado.posicion
            camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(p.latitud, p.longitud), 16f))
        }
    }

    // Paso 6: al seleccionar un lugar, la cámara viaja hasta él (zoom 17)
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar ?: return@LaunchedEffect
        camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f))
    }

    // Mapa en escala de grises + Mejora 3: punto azul nativo solo si hay permiso
    // (isMyLocationEnabled sin permiso lanzaría SecurityException)
    val propiedades = remember(puntoAzulNativo) {
        MapProperties(
            isMyLocationEnabled = puntoAzulNativo,
            mapStyleOptions = MapStyleOptions(ESTILO_GRIS)
        )
    }
    val ajustes = remember(puntoAzulNativo) { MapUiSettings(myLocationButtonEnabled = puntoAzulNativo) }

    val densidad = LocalDensity.current
    val tamLugar = with(densidad) { 30.dp.roundToPx() }
    val tamCercano = with(densidad) { 40.dp.roundToPx() }
    val tamYo = with(densidad) { 36.dp.roundToPx() }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = propiedades,
        uiSettings = ajustes,
        // Deja libres la tarjeta de arriba y la hoja de abajo (logo de Google y zoom visibles)
        contentPadding = PaddingValues(top = paddingSuperior, bottom = paddingInferior)
    ) {
        // Marcadores propios: círculos con los colores del tema
        val iconoLugar = remember { marcadorCirculo(Negro, Color.White, Color.White, tamLugar) }
        val iconoCercano = remember { marcadorCirculo(Lima, Negro, Negro, tamCercano) }
        val iconoYo = remember { marcadorCirculo(Negro, Lima, Lima, tamYo) }

        if (estado is UbicacionUi.Lista) {
            val p = estado.posicion
            val centro = LatLng(p.latitud, p.longitud)

            // Mejora 2: círculo del radio de cercanía alrededor de tu posición
            if (radio != null) {
                Circle(
                    center = centro,
                    radius = radio.toDouble(),
                    fillColor = Lima.copy(alpha = 0.22f),
                    strokeColor = LimaOscuroTrazo,
                    strokeWidth = 4f
                )
            }

            // Paso 3: tu posición
            val miMarcador = rememberUpdatedMarkerState(position = centro)
            LaunchedEffect(p) { miMarcador.showInfoWindow() }
            Marker(
                state = miMarcador,
                title = "Estás aquí",
                snippet = "Lat ${p.latitud}, Lon ${p.longitud}",
                icon = iconoYo,
                anchor = Offset(0.5f, 0.5f),
                zIndex = 2f
            )
        }

        // Paso 4 y 7: un marcador por lugar; el más cercano resaltado en lima
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                val estadoMarcador = rememberUpdatedMarkerState(
                    position = LatLng(lugar.latitud, lugar.longitud)
                )
                LaunchedEffect(seleccionado) {
                    if (seleccionado == lugar.id) estadoMarcador.showInfoWindow()
                }
                val esCercano = lugar.id == masCercano?.id
                Marker(
                    state = estadoMarcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = if (esCercano) iconoCercano else iconoLugar,
                    anchor = Offset(0.5f, 0.5f),
                    zIndex = if (esCercano) 1f else 0f
                )
            }
        }
    }
}

private val LimaOscuroTrazo = Color(0xFF9DC21F)

// Dibuja un marcador redondo: borde, relleno y un punto al centro
private fun marcadorCirculo(borde: Color, relleno: Color, centro: Color, tamPx: Int): BitmapDescriptor {
    val bitmap = Bitmap.createBitmap(tamPx, tamPx, Bitmap.Config.ARGB_8888)
    val lienzo = Canvas(bitmap)
    val pincel = Paint(Paint.ANTI_ALIAS_FLAG)
    val r = tamPx / 2f
    pincel.color = borde.toArgb()
    lienzo.drawCircle(r, r, r, pincel)
    pincel.color = relleno.toArgb()
    lienzo.drawCircle(r, r, r * 0.72f, pincel)
    pincel.color = centro.toArgb()
    lienzo.drawCircle(r, r, r * 0.3f, pincel)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

// Estilo del mapa en escala de grises (sin íconos de comercios ni transporte)
private const val ESTILO_GRIS = """
[
  {"elementType":"geometry","stylers":[{"color":"#eeeeec"}]},
  {"elementType":"labels.icon","stylers":[{"visibility":"off"}]},
  {"elementType":"labels.text.fill","stylers":[{"color":"#6e6e6a"}]},
  {"elementType":"labels.text.stroke","stylers":[{"color":"#f5f5f3"}]},
  {"featureType":"landscape.man_made","elementType":"geometry","stylers":[{"color":"#e2e2df"}]},
  {"featureType":"poi","elementType":"geometry","stylers":[{"color":"#e4e4e1"}]},
  {"featureType":"poi.park","elementType":"geometry","stylers":[{"color":"#d9d9d5"}]},
  {"featureType":"road","elementType":"geometry","stylers":[{"color":"#ffffff"}]},
  {"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#d4d4d0"}]},
  {"featureType":"road.arterial","elementType":"labels.text.fill","stylers":[{"color":"#7d7d78"}]},
  {"featureType":"transit","stylers":[{"visibility":"off"}]},
  {"featureType":"water","elementType":"geometry","stylers":[{"color":"#c8c8c4"}]}
]
"""
