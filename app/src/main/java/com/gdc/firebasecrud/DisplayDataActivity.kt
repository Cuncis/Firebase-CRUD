package com.gdc.firebasecrud

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdc.firebasecrud.adapter.BarangAdapter
import com.gdc.firebasecrud.model.Barang
import com.gdc.firebasecrud.util.Utils.Companion.showToast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display_data.*
import kotlinx.android.synthetic.main.dialog_view.view.*

class DisplayDataActivity : AppCompatActivity(), BarangAdapter.LongClickListener {

    private lateinit var ref: DatabaseReference
    private var barangList: ArrayList<Barang> = ArrayList()
    private lateinit var adapter: BarangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_data)

        adapter = BarangAdapter(this)
        rv_barang.layoutManager = LinearLayoutManager(this)
        rv_barang.setHasFixedSize(true)
        adapter.setLongListener(this)
        rv_barang.adapter = adapter

        ref = FirebaseDatabase.getInstance().reference
        showData()

    }

    private fun showData() {
        ref.child("barang").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                barangList.clear()

                for (data in p0.children) {
                    val barang = data.getValue(Barang::class.java)
                    barang?.key = data.key

                    barangList.add(barang!!)
                }

                adapter.setBarangList(barangList)
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d("_DB", "Error: ${p0.details} - ${p0.message}")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val msg = data?.getStringExtra("SUCCESS_EXTRA")
                showToast(this, msg!!)
            }
        }
    }

    override fun onLongClick(position: Int, barang: Barang) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_view, null)
        builder.setView(view)

        val dialog = builder.create()
        view.btn_edit_data.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            intent.putExtra("data", barangList[position])
            startActivityForResult(intent, 1)
            dialog.dismiss()
        }
        view.btn_delete_data.setOnClickListener {
            showToast(this, "Barang Deleted")
            ref.child("barang")
                .child(barang.key!!)
                .removeValue().addOnSuccessListener {
                    Toast.makeText(this, "Data Berhasil Terhapus", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    showToast(this, "Data Gagal DiHapus")
                }

            dialog.dismiss()
        }
        dialog.show()
    }
}
