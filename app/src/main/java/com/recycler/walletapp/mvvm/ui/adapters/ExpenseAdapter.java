package com.recycler.walletapp.mvvm.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.recycler.walletapp.R;
import com.recycler.walletapp.mvvm.repository.listeners.ItemClickListener;
import com.recycler.walletapp.mvvm.repository.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder>
        implements Filterable {

    private List<Expense> ExpenseList;
    private List<Expense> ExpenseListFull;
    Context mContext;
    ItemClickListener itemClickListener;

    public void SetItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ExpenseAdapter(Context mContext, List<Expense> ExpenseList) {
        this.ExpenseList = ExpenseList;
        ExpenseListFull = new ArrayList<>(ExpenseList);
        this.mContext = mContext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Category,Price,DateAndTime;


        public MyViewHolder(View view,ItemClickListener itemClickListener) {
            super(view);
            Category = view.findViewById(R.id.ExpenseCategory);
            Price = view.findViewById(R.id.ExpensePrice);
            DateAndTime = view.findViewById(R.id.ExpenseDateAndTime);
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        itemClickListener.onClick(v,position);
                    }
                }
            });

        }

    }

    public Expense GetExpenseAt(int position){
        return ExpenseList.get(position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.expense_recycler_single_row, viewGroup, false);
        return new MyViewHolder(itemView,itemClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        Expense Expense = ExpenseList.get(i);
        if (Expense!=null) {
            myViewHolder.Category.setText(Expense.getCategory());
            myViewHolder.Price.setText(String.valueOf(Expense.getPrice()));
            myViewHolder.DateAndTime.setText(Expense.getDate() + " " + Expense.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return ExpenseList.size();
    }

    public void removeItem(int position) {
        ExpenseList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Expense> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ExpenseListFull);
            } else {
                String filterPatern = constraint.toString().toLowerCase().trim();
                for (Expense user : ExpenseListFull) {
                    if (user.getCategory().toLowerCase().contains(filterPatern)
                    || String.valueOf(user.getPrice()).toLowerCase().contains(filterPatern)) {
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ExpenseList.clear();
            ExpenseList.addAll((ArrayList<Expense>) results.values);
            notifyDataSetChanged();
        }
    };

}

