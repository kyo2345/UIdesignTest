package com.oh.uidesigntest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recycler_Adapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Item>items;


    public Recycler_Adapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.recycler_adapter,parent,false);
        VH holder=new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH) holder;
        Item item=items.get(position);
        vh.nickname.setText(item.nickname);
        vh.acc.setText(item.account);
        vh.neyong.setText(item.neyong);
        Glide.with(context).load(item.profile_ic).into(vh.circleImageView);
        Glide.with(context).load(item.img).into(vh.iv);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView nickname,acc,neyong;
        ImageView iv;
        public VH(View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.profileic);
            nickname=itemView.findViewById(R.id.nickname2);
            acc=itemView.findViewById(R.id.acc);
            iv=itemView.findViewById(R.id.iv);
            neyong=itemView.findViewById(R.id.neyong1);




        }
    }
}
