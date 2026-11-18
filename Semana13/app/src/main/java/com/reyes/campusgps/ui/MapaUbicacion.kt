package com.reyes.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.reyes.campusgps.model.LugarConDistancia
import com.reyes.campusgps.model.UbicacionUi

// Solo dibuja lo que recibe: no consulta Repository ni calcula distancias
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
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

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // Paso 3: tu posición, marcador rojo por defecto
        if (estado is UbicacionUi.Lista) {
            val p = estado.posicion
            val miMarcador = rememberUpdatedMarkerState(position = LatLng(p.latitud, p.longitud))
            LaunchedEffect(p) { miMarcador.showInfoWindow() }
            Marker(
                state = miMarcador,
                title = "Estás aquí",
                snippet = "Lat ${p.latitud}, Lon ${p.longitud}"
            )
        }

        // Paso 4: un marcador azul por lugar
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                val estadoMarcador = rememberUpdatedMarkerState(
                    position = LatLng(lugar.latitud, lugar.longitud)
                )
                LaunchedEffect(seleccionado) {
                    if (seleccionado == lugar.id) estadoMarcador.showInfoWindow()
                }
                Marker(
                    state = estadoMarcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }
    }
}
