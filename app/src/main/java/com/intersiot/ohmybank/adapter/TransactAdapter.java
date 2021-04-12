package com.intersiot.ohmybank.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.intersiot.ohmybank.R;
import com.intersiot.ohmybank.model.TransactDTO;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactAdapter extends
        FirestoreRecyclerAdapter<TransactDTO, TransactAdapter.TransactHolder> {

    public TransactAdapter(FirestoreRecyclerOptions<TransactDTO> options) {
        super(options);
    }

    @NotNull
    public TransactAdapter.TransactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactHolder(view);
    }

    @SuppressLint("SetTextI18n")
    protected void onBindViewHolder(TransactAdapter.TransactHolder holder,
                                    int position,
                                    TransactDTO model) {
        // timestamp Long -> String 타입 변환
        Date date = new Date(model.getTimestamp());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        holder.timestamp.setText(format.format(date));
        holder.account.setText(model.getAccount());
        holder.payment.setText(Integer.toString(model.getPayment()));
        holder.accountCache.setText(Integer.toString(model.getCache()));
    }

    static class TransactHolder extends RecyclerView.ViewHolder {

        TextView timestamp;
        TextView account;
        TextView payment;
        TextView accountCache;

        public TransactHolder(View itemView) {
            super(itemView);

            timestamp = itemView.findViewById(R.id.textViewTimestamp);
            account = itemView.findViewById(R.id.transactAccount);
            payment = itemView.findViewById(R.id.transactCache);
            accountCache = itemView.findViewById(R.id.accountCache);
        }
    }
}
