package com.intersiot.ohmybank.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.intersiot.ohmybank.R
import com.intersiot.ohmybank.model.TransactDTO
import java.text.SimpleDateFormat
import java.util.*

class TransactAdapter(options: FirebaseRecyclerOptions<TransactDTO.DepositAndWithdrawal>) :
        FirebaseRecyclerAdapter<TransactDTO.DepositAndWithdrawal,
                TransactAdapter.TransactHolder>(options) {

    class TransactHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        var timestamp: TextView
        var account: TextView
        var transferCache: TextView
        var accountInCache: TextView

        init {
            timestamp = itemView.findViewById(R.id.textViewTimestamp)
            account = itemView.findViewById(R.id.transactName)
            transferCache = itemView.findViewById(R.id.transactCache)
            accountInCache = itemView.findViewById(R.id.transactMoney)
        }
    }

    override fun onCreateViewHolder (
            parent: ViewGroup,
            viewType: Int): TransactHolder {

        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction, parent, false)
        return TransactHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(
            holder: TransactHolder,
            position: Int,
            model: TransactDTO.DepositAndWithdrawal) {

        holder.account.text = model.account
        holder.transferCache.text = model.deposit.toString()
        holder.transferCache.text = model.withdrawal.toString()
        holder.transferCache.text = model.cache.toString()
    }

}
