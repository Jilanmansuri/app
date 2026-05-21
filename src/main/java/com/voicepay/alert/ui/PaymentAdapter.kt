package com.voicepay.alert.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.voicepay.alert.R
import com.voicepay.alert.data.local.PaymentEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentAdapter : ListAdapter<PaymentEntity, PaymentAdapter.PaymentViewHolder>(PaymentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = getItem(position)
        holder.bind(payment)
    }

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvSender: TextView = itemView.findViewById(R.id.tvSender)
        private val tvApp: TextView = itemView.findViewById(R.id.tvApp)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(payment: PaymentEntity) {
            tvAmount.text = "₹${payment.amount}"
            tvSender.text = payment.sender
            tvApp.text = payment.appName
            
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            tvTime.text = sdf.format(Date(payment.timestamp))
        }
    }

    class PaymentDiffCallback : DiffUtil.ItemCallback<PaymentEntity>() {
        override fun areItemsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity): Boolean {
            return oldItem == newItem
        }
    }
}
