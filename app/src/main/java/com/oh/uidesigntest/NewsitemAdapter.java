package com.oh.uidesigntest;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsitemAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<News_item>items;

    public NewsitemAdapter(Context context, ArrayList<News_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_news_item,parent,false);
        VH holder= new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder;
        News_item item=items.get(position);
        vh.tv_title.setText(item.getTitle());
        vh.tv_desc.setText(item.getDesc());
        vh.date.setText(item.getDate());
        if (item.getImgUrl()==null){
            vh.iv.setVisibility(View.GONE);
        }else {
            vh.iv.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getImgUrl()).into(vh.iv);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class VH extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_desc;
        TextView date;
        ImageView iv;
        public VH(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.news_title);
            tv_desc=itemView.findViewById(R.id.news_desc);
            date=itemView.findViewById(R.id.news_date);
            iv=itemView.findViewById(R.id.news_iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link=items.get(getLayoutPosition()).getLink();
                    Intent intent=new Intent(context,News_item_Activity.class);
                    intent.putExtra("Link",link);
                    context.startActivity(intent);
                }
            });
        }
    }
}
