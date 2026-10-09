    package com.reyes.clinicasaludplus.ui.screens.sedes

    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.draw.clip
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.navigationBarsPadding
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.LocationOn
    import androidx.compose.material.icons.filled.Phone
    import androidx.compose.material.icons.outlined.Schedule
    import androidx.compose.material3.HorizontalDivider
    import androidx.compose.material3.Icon
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import com.reyes.clinicasaludplus.data.repository.Repositorio
    import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
    import com.reyes.clinicasaludplus.ui.components.BotonPrimario
    import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
    import com.reyes.clinicasaludplus.ui.theme.Blanco
    import com.reyes.clinicasaludplus.ui.theme.BordeSuave
    import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
    import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
    import com.reyes.clinicasaludplus.ui.theme.SlateTexto
    import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
    import com.reyes.clinicasaludplus.ui.theme.Tam

    @Composable
    fun SedeDetalleScreen(
        sedeId: String,
        alAgendar: () -> Unit,
        alVolver: () -> Unit
    ) {
        val sede = remember(sedeId) { Repositorio.obtenerSede(sedeId) }
        val especialidades = remember(sedeId) { Repositorio.especialidadesDeSede(sedeId) }
        val totalDoctores = remember(sedeId) { Repositorio.buscarMedicos(sedeId = sedeId).size }

        Scaffold(
            containerColor = Blanco,
            topBar = { BarraSuperiorConVolver(titulo = "Sede ${sede?.nombre.orEmpty()}", alVolver = alVolver) },
            bottomBar = {
                if (sede != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        BotonPrimario(texto = "Agendar una cita", onClick = alAgendar)
                    }
                }
            }
        ) { innerPadding ->
            if (sede == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontró la sede seleccionada. Vuelve atrás y elige otra.",
                        color = RojoAlerta,
                        fontSize = Tam.Cuerpo,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                return@Scaffold
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(sede.fotoRes),
                    contentDescription = "Sede ${sede.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                FilaDato(Icons.Default.LocationOn, "Dirección", sede.direccion)
                HorizontalDivider(color = BordeSuave)
                FilaDato(Icons.Default.Phone, "Teléfono", sede.telefono)
                HorizontalDivider(color = BordeSuave)
                FilaDato(Icons.Outlined.Schedule, "Horario de atención", sede.horarioAtencion)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Especialidades en esta sede",
                    fontSize = Tam.Subtitulo,
                    fontWeight = FontWeight.Bold,
                    color = NavyTitulo
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (totalDoctores == 1) "1 doctor disponible" else "$totalDoctores doctores disponibles",
                    fontSize = Tam.Cuerpo,
                    color = SlateTexto
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    especialidades.forEach { esp ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = SuperficieSuave
                        ) {
                            Text(
                                text = esp.nombre,
                                fontSize = Tam.Cuerpo,
                                color = NavyTitulo,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    @Composable
    private fun FilaDato(icono: ImageVector, titulo: String, valor: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE6E2D6)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = VerdePrimario,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = titulo, fontSize = Tam.Pequeno, color = SlateTexto)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = valor,
                    fontSize = Tam.Cuerpo,
                    fontWeight = FontWeight.Medium,
                    color = NavyTitulo
                )
            }
        }
    }
