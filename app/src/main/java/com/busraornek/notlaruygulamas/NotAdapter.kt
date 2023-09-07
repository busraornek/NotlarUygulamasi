package com.busraornek.notlaruygulamas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference

class NotAdapter(private val mContex: Context,
                 private val notlarListe:List<Notlar>,
                 private val refKisiler: DatabaseReference
)
                :RecyclerView.Adapter<NotAdapter.CardTasarimTutucu>() {


    inner class CardTasarimTutucu(tasarim: View) : RecyclerView.ViewHolder(tasarim) {


        var textViewNotBaslik: TextView
        var textViewNotIcerik: TextView
        var imageViewSil: ImageView
        var linear:LinearLayout

        init {
            textViewNotBaslik = tasarim.findViewById(R.id.idTVNote)
            textViewNotIcerik = tasarim.findViewById(R.id.idTVIcerik)
            imageViewSil = tasarim.findViewById(R.id.idIVDelete)
            linear = tasarim.findViewById(R.id.linearLayout)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContex).inflate(R.layout.notlar_card_tasarim,parent,false)
        return CardTasarimTutucu(tasarim)
    }

    override fun getItemCount(): Int {
        return notlarListe.size
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val not = notlarListe.get(position)

        holder.textViewNotBaslik.text = "${not.not_baslik}"
        holder.textViewNotIcerik.text = "${not.not_icerik}"

        holder.imageViewSil.setOnClickListener {
            Snackbar.make(holder.imageViewSil,"${not.not_baslik} silinsin mi?",Snackbar.LENGTH_SHORT).setAction("EVET"){
                Toast.makeText(mContex,"${not.not_baslik} silindi.",Toast.LENGTH_SHORT).show()
                refKisiler.child(not.not_id!!).removeValue()
            }.show()
        }
        holder.linear.setOnClickListener {
            alertGoster(not)
        }
    }
    fun alertGoster(not:Notlar){
        val tasarim = LayoutInflater.from(mContex).inflate(R.layout.alert_goster,null)
        val editTextBaslik = tasarim.findViewById(R.id.editTextBaslik) as EditText
        val editTextIcerik = tasarim.findViewById(R.id.editTextIcerik) as EditText

        editTextBaslik.setText(not.not_baslik)
        editTextIcerik.setText(not.not_icerik)

        val ad = AlertDialog.Builder(mContex)

        ad.setTitle("Not Güncelle")
        ad.setView(tasarim)
        ad.setPositiveButton("GÜncelle"){ dialogInterface, i->
            val notBaslik = editTextBaslik.text.toString()
            val notIcerik = editTextIcerik.text.toString()
            //güncelleme
            val bilgiler = HashMap<String,Any>()
            bilgiler.put("not_baslik",notBaslik)
            bilgiler.put("not_icerik",notIcerik)
            refKisiler.child(not.not_id!!).updateChildren(bilgiler)


            Toast.makeText(mContex,"$notBaslik güncellendi",Toast.LENGTH_SHORT).show()
        }
        ad.setNegativeButton("İptal"){ dialogInterface , i->
        }
        ad.create().show()

    }

}