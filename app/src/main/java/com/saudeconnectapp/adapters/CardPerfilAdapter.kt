import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.saudeconnectapp.databinding.ItemCardPerfilOneBinding
import com.saudeconnectapp.model.CarrosselPerfil

class CardPerfilAdapter(
    private val context: Context, val listaCarrosselPerfil: MutableList<CarrosselPerfil>
) : RecyclerView.Adapter<CardPerfilAdapter.CardCarroselViewHolder>() {


    private var listener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCarroselViewHolder {
        val itemLista =
            ItemCardPerfilOneBinding.inflate(LayoutInflater.from(context), parent, false)
        return CardCarroselViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: CardCarroselViewHolder, position: Int) {

        // Configurar os textos e imagens do card
        holder.title.text = listaCarrosselPerfil[position].title.toString()
        holder.img.setImageResource(listaCarrosselPerfil[position].img!!)
        holder.count.text = listaCarrosselPerfil[position].count.toString()
        holder.item1.text = listaCarrosselPerfil[position].item1.toString()
        holder.ultimas.text = listaCarrosselPerfil[position].ultimas.toString()

        // Configurar clique no botão do card
        holder.btn.setOnClickListener {
            listener?.invoke(position)  // Chama o listener com a posição do item clicado
        }
    }

    override fun getItemCount() = listaCarrosselPerfil.size

    inner class CardCarroselViewHolder(binding: ItemCardPerfilOneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.txtTitle
        val img = binding.imgBackgroundCardPerfil
        val count = binding.txtCardPerfilCount
        val item1 = binding.txtItemOne
        val ultimas = binding.ultimaSalvas
        val btn: Button = binding.btnAddPerfilCard  // O botão no card
    }
}