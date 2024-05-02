package com.example.sorteionumeros

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Método chamado quando o banco de dados é criado pela primeira vez
    override fun onCreate(db: SQLiteDatabase) {
        // Cria a tabela com as colunas especificadas para armazenar os números sorteados
        val query = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NUMERO_1 INTEGER NOT NULL,
            $COLUMN_NUMERO_2 INTEGER NOT NULL,
            $COLUMN_NUMERO_3 INTEGER NOT NULL,
            $COLUMN_NUMERO_4 INTEGER NOT NULL,
            $COLUMN_NUMERO_5 INTEGER NOT NULL,
            $COLUMN_NUMERO_6 INTEGER NOT NULL
        );
    """.trimIndent()
        db.execSQL(query) // Executa a consulta SQL para criar a tabela
    }

    // Método chamado quando o banco de dados precisa ser atualizado
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Remove a tabela existente
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        // Cria novamente a tabela
        onCreate(db)
    }

    // Método para adicionar um novo sorteio ao banco de dados
    fun addSorteio(numero1: Int, numero2: Int, numero3: Int, numero4: Int, numero5: Int, numero6: Int): Long {
        // Obtém uma instância do banco de dados para escrita
        val db = this.writableDatabase
        // Cria um objeto ContentValues para armazenar os valores
        val cv = ContentValues()
        // Adiciona cada número ao ContentValues
        cv.put(COLUMN_NUMERO_1, numero1)
        cv.put(COLUMN_NUMERO_2, numero2)
        cv.put(COLUMN_NUMERO_3, numero3)
        cv.put(COLUMN_NUMERO_4, numero4)
        cv.put(COLUMN_NUMERO_5, numero5)
        cv.put(COLUMN_NUMERO_6, numero6)
        // Insere os valores no banco de dados
        val result = db.insert(TABLE_NAME, null, cv)
        // Verifica se a inserção foi bem-sucedida
        if (result == -1L) {
            Log.e("MyDatabaseHelper", "Failed to add sorteio") // Log de erro se a inserção falhar
        } else {
            Log.d("MyDatabaseHelper", "Sorteio added successfully") // Log de sucesso se a inserção for bem-sucedida
        }
        // Retorna o ID do novo registro inserido
        return result
    }

    // Método para buscar todos os sorteios do banco de dados
    fun fetchAllSorteios(): Cursor? {
        // Consulta SQL para selecionar todos os registros da tabela
        val query = "SELECT * FROM " + TABLE_NAME
        // Obtém uma instância do banco de dados para leitura
        val db = this.readableDatabase
        // Inicializa o cursor para armazenar os resultados da consulta
        var cursor: Cursor? = null
        // Executa a consulta SQL e armazena os resultados no cursor
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        // Retorna o cursor com os resultados
        return cursor
    }

    companion object {
        // Constantes para os detalhes do banco de dados
        private const val DATABASE_NAME = "NumerosSorteados.db" // Nome do banco de dados
        private const val DATABASE_VERSION = 1 // Versão do banco de dados
        private const val TABLE_NAME = "sorteios" // Nome da tabela
        private const val COLUMN_ID = "_id" // Coluna para o ID do registro
        // Constantes para os nomes das colunas dos números sorteados
        const val COLUMN_NUMERO_1 = "numero_1"
        const val COLUMN_NUMERO_2 = "numero_2"
        const val COLUMN_NUMERO_3 = "numero_3"
        const val COLUMN_NUMERO_4 = "numero_4"
        const val COLUMN_NUMERO_5 = "numero_5"
        const val COLUMN_NUMERO_6 = "numero_6"
    }
}