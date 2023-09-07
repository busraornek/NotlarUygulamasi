package com.busraornek.notlaruygulamas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.busraornek.notlaruygulamas.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notlarListe: ArrayList<Notlar>
    private lateinit var adapter: NotAdapter
    private lateinit var refKisiler: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


       // binding.toolbar.title = "Notlar Uygulaması"
        setSupportActionBar(binding.toolbar)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)

        val dp = FirebaseDatabase.getInstance()
        refKisiler = dp.getReference("notlar")
        notlarListe = ArrayList()
        adapter = NotAdapter(this,notlarListe,refKisiler)
        binding.rv.adapter = adapter
        tumKisiler()



        adapter = NotAdapter(this,notlarListe,refKisiler)
        binding.rv.adapter = adapter

        binding.imageViewEkle.setOnClickListener {
            alertGoster()
        }

    }

    fun alertGoster(){
        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_goster,null)
        val editTextBaslik = tasarim.findViewById(R.id.editTextBaslik) as EditText
        val editTextIcerik = tasarim.findViewById(R.id.editTextIcerik) as EditText
        val ad = AlertDialog.Builder(this)

        ad.setTitle("Not Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("Ekle"){ dialogInterface, i->
            val notBaslik = editTextBaslik.text.toString()
            val notIcerik = editTextIcerik.text.toString()

            val kisi = Notlar("",notBaslik,notIcerik)
             refKisiler.push().setValue(kisi)

            Toast.makeText(this,"$notBaslik",Toast.LENGTH_SHORT).show()
        }
        ad.setNegativeButton("İptal"){ dialogInterface , i->
        }
        ad.create().show()

    }
    fun tumKisiler(){
        refKisiler.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                notlarListe.clear()
                for(c in snapshot.children){
                    val not = c.getValue(Notlar::class.java)

                    if(not != null){
                        not.not_id = c.key
                        notlarListe.add(not)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



}


