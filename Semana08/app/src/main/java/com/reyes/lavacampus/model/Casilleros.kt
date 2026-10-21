package com.reyes.lavacampus.model

object Casilleros {
    /** C-01 ... C-30 */
    val todos: List<String> = (1..30).map { "C-%02d".format(it) }
}
