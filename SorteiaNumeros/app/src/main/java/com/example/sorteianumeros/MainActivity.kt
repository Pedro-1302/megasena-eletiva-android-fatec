package com.example.sorteianumeros
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sorteionumeros.MyDatabaseHelper
import java.util.Random

class MainActivity : AppCompatActivity() {

    // Declaração de variáveis para os componentes de interface
    lateinit var genButtons: Array<TextView> // Array de TextViews para exibir números sorteados
    private lateinit var imageViewPig: ImageView // ImageView para exibir a imagem de um porco
    private lateinit var genNumberButton: Button // Botão para gerar números
    private var isRotating = false // Variável para rastrear o estado de rotação da imagem
    lateinit var changeScreenButton: Button // Botão para mudar de tela
    private lateinit var dbHelper: MyDatabaseHelper // Objeto para manipular o banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout da atividade
        setContentView(R.layout.activity_main)

        // Inicializa o banco de dados
        dbHelper = MyDatabaseHelper(this)

        // Inicializa a ImageView para a imagem de um porco
        imageViewPig = findViewById(R.id.imageView_pig)

        // Inicializa o array de TextViews para exibir números sorteados
        genButtons = arrayOf(
            findViewById(R.id.genNumber1),
            findViewById(R.id.genNumber2),
            findViewById(R.id.genNumber3),
            findViewById(R.id.genNumber4),
            findViewById(R.id.genNumber5),
            findViewById(R.id.genNumber6)
        )

        // Inicializa o botão para gerar números
        genNumberButton = findViewById(R.id.genButton)

        // Define o ouvinte de clique para o botão de gerar números
        genNumberButton.setOnClickListener {
            // Sorteia 6 números entre 1 e 60
            val numerosSorteados = sortearNumeros(6, 1, 60)

            // Salva cada um dos seis números individualmente no banco de dados
            dbHelper.addSorteio(
                numerosSorteados[0],
                numerosSorteados[1],
                numerosSorteados[2],
                numerosSorteados[3],
                numerosSorteados[4],
                numerosSorteados[5]
            )

            // Cria uma animação de rotação de 360 graus em 1 segundo
            val rotateAnimation = RotateAnimation(
                0f, 360f, // Ângulos de rotação
                Animation.RELATIVE_TO_SELF, 0.5f, // Ponto de origem X
                Animation.RELATIVE_TO_SELF, 0.5f // Ponto de origem Y
            )
            // Define a duração da animação (1 segundo)
            rotateAnimation.duration = 1000
            // Define que a animação será executada apenas uma vez
            rotateAnimation.repeatCount = 0

            // Inicia a animação na ImageView
            imageViewPig.startAnimation(rotateAnimation)

            // Atualiza os TextViews com os números sorteados
            for (i in genButtons.indices) {
                genButtons[i].text = numerosSorteados[i].toString()
            }
        }

        // Inicializa o botão para mudar de tela
        changeScreenButton = findViewById(R.id.btn_change_screen)

        // Define o ouvinte de clique para o botão de mudar de tela
        changeScreenButton.setOnClickListener {
            // Cria uma intenção para iniciar `MainActivity2`
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    // Função para sortear uma quantidade especificada de números dentro de um intervalo
    private fun sortearNumeros(quantidade: Int, min: Int, max: Int): IntArray {
        // Verifica se a quantidade de números é válida dentro do intervalo especificado
        require(quantidade <= max - min + 1) {
            "Quantidade de números a sortear excede o intervalo especificado."
        }
        // Cria um array para armazenar os números sorteados
        val numerosSorteados = IntArray(quantidade)
        // Cria uma instância de Random para gerar números aleatórios
        val random = Random()
        // Sorteia os números
        for (i in 0 until quantidade) {
            var numeroSorteado: Int
            var numeroRepetido: Boolean
            // Gera números aleatórios até encontrar um número que não seja repetido
            do {
                numeroSorteado = random.nextInt(max - min + 1) + min
                numeroRepetido = false
                // Verifica se o número sorteado já existe no array
                for (j in 0 until i) {
                    if (numerosSorteados[j] == numeroSorteado) {
                        numeroRepetido = true
                        break
                    }
                }
            } while (numeroRepetido)
            // Armazena o número sorteado no array
            numerosSorteados[i] = numeroSorteado
        }
        // Retorna o array com os números sorteados
        return numerosSorteados
    }
}