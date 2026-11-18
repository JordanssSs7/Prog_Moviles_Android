package com.reyes.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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

// Solo dibuja lo que recibe: no consulta Repository ni calcula distancias
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radio: Int?,
    puntoAzulNativo: Boolean,
    modifier: Modifier = Modifier
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

    // Mejora 3: punto azul nativo y botón "mi ubicación", solo si hay permiso
    // (isMyLocationEnabled sin permiso lanzaría SecurityException)
    val propiedades = remember(puntoAzulNativo) { MapProperties(isMyLocationEnabled = puntoAzulNativo) }
    val ajustes = remember(puntoAzulNativo) { MapUiSettings(myLocationButtonEnabled = puntoAzulNativo) }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = propiedades,
        uiSettings = ajustes
    ) {
        if (estado is UbicacionUi.Lista) {
            val p = estado.posicion
            val centro = LatLng(p.latitud, p.longitud)

            // Mejora 2: círculo del radio de cercanía alrededor de tu posición
            if (radio != null) {
                Circle(
                    center = centro,
                    radius = radio.toDouble(),
                    fillColor = Color(0x332B7BC0),
                    strokeColor = Color(0xFF2B7BC0),
                    strokeWidth = 4f
                )
            }

            // Paso 3: tu posición, marcador rojo por defecto
            val miMarcador = rememberUpdatedMarkerState(position = centro)
            LaunchedEffect(p) { miMarcador.showInfoWindow() }
            Marker(
                state = miMarcador,
                title = "Estás aquí",
                snippet = "Lat ${p.latitud}, Lon ${p.longitud}"
            )
        }

        // Paso 4 y 7: un marcador por lugar; azul, y el más cercano en verde
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                val estadoMarcador = rememberUpdatedMarkerState(
                    position = LatLng(lugar.latitud, lugar.longitud)
                )
                LaunchedEffect(seleccionado) {
                    if (seleccionado == lugar.id) estadoMarcador.showInfoWindow()
                }
                val color = if (lugar.id == masCercano?.id) {
                    BitmapDescriptorFactory.HUE_GREEN
                } else {
                    BitmapDescriptorFactory.HUE_AZURE
                }
                Marker(
                    state = estadoMarcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(color)
                )
            }
        }
    }
}
