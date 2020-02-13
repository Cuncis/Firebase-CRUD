package com.gdc.firebasecrud.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gdc.firebasecrud.R
import com.gdc.firebasecrud.model.Barang
import kotlinx.android.synthetic.main.item_barang.view.*

class BarangAdapter(private val context: Context): RecyclerView.Adapter<BarangAdapter.BarangHolder>() {

    private var barangList: List<Barang> = ArrayList()
    private lateinit var longClickListener: LongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangHolder {
        return BarangHolder(LayoutInflater.from(context).inflate(R.layout.item_barang, parent, false))
    }

    override fun getItemCount(): Int = barangList.size

    override fun onBindViewHolder(holder: BarangHolder, position: Int) {
        holder.bind(barangList[position])
    }

    fun setBarangList(barangList: List<Barang>) {
        this.barangList = barangList
        notifyDataSetChanged()
    }

    fun setLongListener(longClickListener: LongClickListener) {
        this.longClickListener = longClickListener
    }

    inner class BarangHolder(view: View): RecyclerView.ViewHolder(view), View.OnLongClickListener {
        val namaBarang: TextView? = view.tv_namabarang

        init {
            itemView.setOnLongClickListener(this)
        }

        fun bind(barang: Barang) {
            namaBarang?.text = barang.nama
        }

        override fun onLongClick(p0: View?): Boolean {
            longClickListener.onLongClick(adapterPosition, barangList[adapterPosition])
            return true
        }
    }

    interface LongClickListener {
        fun onLongClick(position: Int, barang: Barang)
    }

}














