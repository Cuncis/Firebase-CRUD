package com.gdc.firebasecrud

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.gdc.firebasecrud.model.Barang
import com.gdc.firebasecrud.util.Utils.Companion.showToast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_data.*

class AddDataActivity : AppCompatActivity() {

    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        ref = FirebaseDatabase.getInstance().reference

        val barang: Barang? = intent.getParcelableExtra("data")

        if (barang != null) {
            et_namabarang.setText(barang.nama)
            et_merkbarang.setText(barang.merk)
            et_hargabarang.setText(barang.harga)

            btn_submit.setOnClickListener {
                barang.nama = et_namabarang.text.toString()
                barang.merk = et_merkbarang.text.toString()
                barang.harga = et_hargabarang.text.toString()

                updateBarang(barang)
            }
        } else {
            btn_submit.setOnClickListener {
                if (et_namabarang.text.isNotEmpty() && et_merkbarang.text.isNotEmpty() && et_hargabarang.text.isNotEmpty()) {
                    submitBarang(Barang(et_namabarang.text.toString(), et_merkbarang.text.toString(), et_hargabarang.text.toString()))
                } else {
                    Toast.makeText(this, "Data barang tidak boleh kosong", Toast.LENGTH_SHORT).show()

                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(et_namabarang.windowToken, 0)
                }
            }
        }
    }

    private fun submitBarang(barang: Barang) {
        ref.child("barang").push().setValue(barang).addOnCompleteListener {
            et_namabarang.setText("")
            et_merkbarang.setText("")
            et_hargabarang.setText("")
            Toast.makeText(this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateBarang(barang: Barang) {
        ref.child("barang")     // akses parent index, ibaratnya seperti nama tabel
            .child(barang.key!!)          // select barang berdasarkan key
            .setValue(barang)             // set value barang yang baru
            .addOnSuccessListener {
                val intent = Intent()
                intent.putExtra("SUCCESS_EXTRA", "Data Berhasil Diubah")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            .addOnFailureListener {
                showToast(this, "Data Gagal Diubah")
            }

    }
}
