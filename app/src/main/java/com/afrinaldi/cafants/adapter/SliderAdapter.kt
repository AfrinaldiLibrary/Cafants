package com.afrinaldi.cafants.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afrinaldi.cafants.databinding.SlideItemBinding
import com.afrinaldi.cafants.model.PlantsModel

class SliderAdapter: RecyclerView.Adapter<SliderAdapter.ViewHolder>(){
    private val items = ArrayList<PlantsModel>()
    fun addData(item: List<PlantsModel>) {
        items.clear()
        items.addAll(item)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: SlideItemBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView
        fun bind(data: PlantsModel){
            binding.ivSlideImage.setImageResource(data.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SlideItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}