package com.reyes.tecsupfit.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.tecsupfit.ui.theme.GreenPrimary
import com.reyes.tecsupfit.ui.theme.GreenLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFitScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Mi perfil") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = GreenLight,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("DR", color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Diego Ramos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Plan Premium", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("14", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Clases", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("3", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Rachas", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}