# CentroTareas

## Observación del Paso 1

Al pulsar **Tarea larga (MAL)** la pantalla se queda congelada unos 4 segundos: el botón **Tócame** no suma y no responde a ningún toque. Cuando pasan los 4 segundos aparece directamente **Terminó** y recién ahí se procesan los toques que hice mientras estaba congelada.

El texto **Trabajando...** nunca se ve porque `Thread.sleep(4000)` corre en el **hilo principal**, que es el mismo que dibuja la pantalla. El mensaje sí cambia a *Trabajando...*, pero el hilo principal no queda libre para redibujar; cuando por fin se libera, el mensaje ya cambió a *Terminó*, así que solo se dibuja el último valor.

Si la espera fuera de 10 segundos, Android mostraría el diálogo **ANR** (*La aplicación no responde*), que aparece cuando el hilo principal no atiende un toque en unos 5 segundos, y ofrecería **Cerrar app** o **Esperar**.
