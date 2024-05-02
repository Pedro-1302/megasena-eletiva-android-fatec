package com.example.sorteianumeros

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sorteionumeros.MyDatabaseHelper

class MainActivity2 : AppCompatActivity() {

    // Declaração de variáveis para os componentes de interface
    lateinit var recycleView: RecyclerView // RecyclerView para exibir os sorteios
    private lateinit var dbHelper: MyDatabaseHelper // Objeto para manipular o banco de dados
    private lateinit var sorteioAdapter: SorteioAdapter // Adapter para o RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout da atividade
        setContentView(R.layout.activity_main2)

        // Ativa a configuração de borda a borda (edge-to-edge)
        enableEdgeToEdge()

        // Define o comportamento do layout de acordo com as margens de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Define as margens do layout com base nos insets de sistema
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa a RecyclerView para exibir os sorteios
        recycleView = findViewById(R.id.rvw_numeros_sorteados)
        recycleView.layoutManager = LinearLayoutManager(this) // Define o layout manager

        // Inicializa o objeto de manipulação do banco de dados
        dbHelper = MyDatabaseHelper(this)
        // Busca os sorteios do banco de dados
        val sorteios = fetchSorteios()

        // Cria e define o adapter para o RecyclerView
        sorteioAdapter = SorteioAdapter(sorteios)
        recycleView.adapter = sorteioAdapter // Define o adapter para o RecyclerView
    }

    @SuppressLint("Range")
    private fun fetchSorteios(): List<List<Int>> {
        // Lista para armazenar sorteios com listas de números
        val sorteios = mutableListOf<List<Int>>()
        // Busca todos os sorteios do banco de dados
        val cursor = dbHelper.fetchAllSorteios()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // Lista para armazenar os números de um sorteio
                    val numeros = mutableListOf<Int>()
                    // Adiciona os números do cursor para a lista
                    numeros.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NUMERO_1)))
                    numeros.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NUMERO_2)))
                    numeros.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NUMERO_3)))
                    numeros.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NUMERO_4)))
                    numeros.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NUMERO_5)))
                    numeros.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NUMERO_6)))

                    // Adiciona a lista de números à lista de sorteios
                    sorteios.add(numeros)
                } while (cursor.moveToNext()) // Move para o próximo sorteio no cursor
            }
            cursor.close() // Fecha o cursor
        }
        // Retorna a lista de sorteios
        return sorteios
    }

    class SorteioAdapter(private val sorteios: List<List<Int>>) : RecyclerView.Adapter<SorteioAdapter.SorteioViewHolder>() {

        // ViewHolder para cada item do RecyclerView
        class SorteioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Declaração de variáveis para os TextViews de números
            val textFieldS1: TextView = itemView.findViewById(R.id.textField_s1)
            val textFieldS2: TextView = itemView.findViewById(R.id.textField_s2)
            val textFieldS3: TextView = itemView.findViewById(R.id.textField_s3)
            val textFieldS4: TextView = itemView.findViewById(R.id.textField_s4)
            val textFieldS5: TextView = itemView.findViewById(R.id.textField_s5)
            val textFieldS6: TextView = itemView.findViewById(R.id.textField_s6)

            // Declaração da TextView para o número do registro
            val textFieldNumero: TextView = itemView.findViewById(R.id.textField_numero)
        }

        // Cria o ViewHolder para cada item do RecyclerView
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SorteioViewHolder {
            // Infla o layout do item de sorteio
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sorteio_item_layout, parent, false)
            // Retorna uma nova instância de SorteioViewHolder
            return SorteioViewHolder(view)
        }

        // Preenche o ViewHolder com dados
        override fun onBindViewHolder(holder: SorteioViewHolder, position: Int) {
            // Acessa os números do sorteio correspondente
            val numeros = sorteios[position]

            // Preenche as TextViews com os números
            holder.textFieldS1.text = numeros[0].toString()
            holder.textFieldS2.text = numeros[1].toString()
            holder.textFieldS3.text = numeros[2].toString()
            holder.textFieldS4.text = numeros[3].toString()
            holder.textFieldS5.text = numeros[4].toString()
            holder.textFieldS6.text = numeros[5].toString()

            // Preenche a TextView com o número do registro
            holder.textFieldNumero.text = (position + 1).toString()
        }

        // Retorna o número de itens na lista de sorteios
        override fun getItemCount(): Int = sorteios.size
    }
}