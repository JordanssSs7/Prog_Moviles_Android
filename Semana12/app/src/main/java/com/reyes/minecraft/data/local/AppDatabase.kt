package com.reyes.minecraft.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ItemEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        /** Versión 2: agrega la receta de crafteo sin perder los favoritos ya guardados. */
        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE items ADD COLUMN receta TEXT")
            }
        }

        /** Versión 3: nombre en español. Se borran las descripciones guardadas para volver a pedirlas en español. */
        private val MIGRACION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE items ADD COLUMN nombreEs TEXT")
                db.execSQL("UPDATE items SET descripcion = NULL, imagenDetalle = NULL")
            }
        }

        fun crear(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "minecraft.db")
                .addMigrations(MIGRACION_1_2, MIGRACION_2_3)
                .build()
    }
}
