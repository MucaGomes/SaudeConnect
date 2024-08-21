import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.saudeconnectapp.databinding.ItemCardPerfilOneBinding
import com.saudeconnectapp.model.CarrosselPerfil

class CardPerfilAdapter(
    private val context: Context, val listaCarrosselPerfil: MutableList<CarrosselPerfil>
) : RecyclerView.Adapter<CardPerfilAdapter.CardCarroselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCarroselViewHolder {
        val itemLista =
            ItemCardPerfilOneBinding.inflate(LayoutInflater.from(context), parent, false)
        return CardCarroselViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: CardCarroselViewHolder, position: Int) {
        holder.title.text = listaCarrosselPerfil[position].title.toString()
        holder.img.setImageResource(listaCarrosselPerfil[position].img!!)
        holder.count.text = listaCarrosselPerfil[position].count.toString()
        holder.item1.text = listaCarrosselPerfil[position].item1.toString()
        holder.item2.text = listaCarrosselPerfil[position].item2.toString()
        holder.item3.text = listaCarrosselPerfil[position].item3.toString()
        holder.ultimas.text = listaCarrosselPerfil[position].ultimas.toString()
        holder.btn.setOnClickListener {
            Toast.makeText(context,"teste", Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount() = listaCarrosselPerfil.size

    inner class CardCarroselViewHolder(binding: ItemCardPerfilOneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.txtCardPerfilSalvas
        val img = binding.imgBackgroundCardPerfil
        val count = binding.txtCardPerfilCount
        val item1 = binding.txtItemOne
        val item2 = binding.txtItemTwo
        val item3 = binding.txtItemThree
        val ultimas = binding.ultimaSalvas
        val btn = binding.btnAddPerfilCard
    }
}