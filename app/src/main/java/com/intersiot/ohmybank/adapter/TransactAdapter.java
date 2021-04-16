package com.intersiot.ohmybank.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.intersiot.ohmybank.R;
import com.intersiot.ohmybank.model.TransactDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactAdapter
        extends FirestoreRecyclerAdapter<TransactDTO,
        TransactAdapter.TransactHolder> {

    public TransactAdapter(FirestoreRecyclerOptions<TransactDTO> options) {
        super(options);
    } // TransactAdapter

    @Override
    protected void onBindViewHolder(@NonNull TransactHolder transactHolder,
                                    int i,
                                    @NonNull TransactDTO transactDTO) {
        // timestamp Long -> String으로 타입변환
        Date date = new Date(transactDTO.getTimestamp());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        transactHolder.timestamp.setText(format.format(date));
        transactHolder.account.setText(transactDTO.getAccount());
        transactHolder.accountCache.setText(Integer.toString(transactDTO.getCache()));
        // 입금 금액이 0이 아니고 이체금액이 0이라면 입금액이 나오도록
        // 반대로 이체금액이 0이 아니고 입금액이 0이라면 이체금액이 나오도록 함.
        if (transactDTO.getDeposit() != 0 && transactDTO.getWithdraw() == 0) {
            transactHolder.payment.setText(Integer.toString(+transactDTO.getDeposit()));
        } else if (transactDTO.getWithdraw() != 0 && transactDTO.getDeposit() == 0) {
            transactHolder.payment.setText(Integer.toString(-transactDTO.getWithdraw()));
        }
    } // onBindViewHolder

    @NonNull
    @Override
    public TransactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactHolder(view);
    } // onCreateViewHolder

    public static class TransactHolder extends RecyclerView.ViewHolder {

        TextView timestamp, account, payment, accountCache;

        public TransactHolder(@NonNull View itemView) {
            super(itemView);

            timestamp = itemView.findViewById(R.id.textViewTimestamp);
            account = itemView.findViewById(R.id.transactAccount);
            payment = itemView.findViewById(R.id.transactCache);
            accountCache = itemView.findViewById(R.id.accountCache);
        }
    } // end TransactHolder

} // class TransactAdapter
