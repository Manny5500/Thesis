package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(int position, String action);
    }

    private List<ComponentModel> items;
    private List<ComponentModel> filteredItems;
    private ItemClickListener itemClickListener;
    private Context context;

    public ChildAdapter(Context context, List<ComponentModel> items, ItemClickListener itemClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public View umcontainer;
        public ImageButton umEdit, umDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            umcontainer = itemView.findViewById(R.id.umcontainer);
            textView = itemView.findViewById(R.id.textumName);
            umEdit = itemView.findViewById(R.id.umEdit);
            umDelete = itemView.findViewById(R.id.umDelete);

            umEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition(), "edit");
                }
            });

            umDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition(), "delete");
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.md_component, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComponentModel item = items.get(position);
        holder.textView.setText(item.getText());
        holder.textView.setTextColor(ContextCompat.getColor(context, item.getTextColorResId()));
        holder.umcontainer.setBackgroundColor(ContextCompat.getColor(context, item.getBackgroundColorResId()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ComponentModel getItem(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    public void setComponents(List<ComponentModel> components) {
        this.items = components;
        notifyDataSetChanged();
    }
}
