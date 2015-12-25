package com.example.lenovo.materialdesign;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 12/26/2015.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {
    private final LayoutInflater inflater;
    private Context context;
    List<Information> informationList = Collections.emptyList();

    public InfoAdapter(Context context, List<Information> informationList) {
        inflater = LayoutInflater.from(context);
        this.informationList = informationList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = informationList.get(position);
        holder.textView.setText(current.title);
        holder.imageView.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public void delete(int position) {
        informationList.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.listIcon);
            textView = (TextView) itemView.findViewById(R.id.listText);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Item Deleted - " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            delete(getAdapterPosition());
        }
    }
}