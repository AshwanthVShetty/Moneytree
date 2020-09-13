package com.example.moneytree.Base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneytree.Model.NetWorkClass;
import com.example.moneytree.R;
import com.example.moneytree.ShowNetworkDetails;
import com.example.moneytree.UserAccounts;

import java.util.ArrayList;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyViewHolder> {

    UserAccounts userAccounts=UserAccounts.getUserObject();

    ArrayList<String> searchResullt=userAccounts.searchResult;
    ArrayMap<String, NetWorkClass> availableNetworks=userAccounts.availableNetworks;



    //test arraylist

    ArrayList<String> testList=new ArrayList<>();
     static public Context context;


    public CustomAdaptor(Context context){
        this.context=context;
    }

    public interface OnItemClicked{
        void onClicked(int position);
    }
    OnItemClicked onItemClicked;

    public void setOnClick(OnItemClicked onItemClicked){
        this.onItemClicked=onItemClicked;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view,onItemClicked);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv.setText(searchResullt.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResullt.size();
    }



    public static class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv;
        OnItemClicked onItemClicked;


        public MyViewHolder(View itemView,OnItemClicked onItemClicked) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.tv=(TextView)itemView.findViewById(R.id.recy_tv);
            this.onItemClicked=onItemClicked;
        }

        @Override
        public void onClick(View v) {
//            Data data=Data.getData();
//            data.setPosition(this.getLayoutPosition());
//            Toast.makeText(data.getContext(),""+data.getPosition(), Toast.LENGTH_SHORT).show();
            onItemClicked.onClicked(getLayoutPosition());

        }
    }
    public void get(){

    }



}
