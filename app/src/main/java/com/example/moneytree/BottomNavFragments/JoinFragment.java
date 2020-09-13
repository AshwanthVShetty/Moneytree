package com.example.moneytree.BottomNavFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneytree.Base.CustomAdaptor;
import com.example.moneytree.Base.FireBaseHelper;
import com.example.moneytree.Base.LayoutLaunchAnimator;
import com.example.moneytree.Model.NetWorkClass;
import com.example.moneytree.R;
import com.example.moneytree.ShowNetworkDetails;
import com.example.moneytree.UserAccounts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class JoinFragment extends Fragment implements CustomAdaptor.OnItemClicked{
    EditText editText;
    ImageButton imageButton;
    RecyclerView recyclerView;
    CustomAdaptor adapter;
    TextView tv1,tv2,tv3;
    RelativeLayout relativeLayout;
    Button b1;



    UserAccounts userAccounts=UserAccounts.getUserObject();

    ArrayList<String> searchResullt=userAccounts.searchResult;
    ArrayMap<String, NetWorkClass> availableNetworks=userAccounts.availableNetworks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frg_join,container,false);

        editText=view.findViewById(R.id.join_phn);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reset();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv1=view.findViewById(R.id.name);
        tv2=view.findViewById(R.id.newid);
        tv3=view.findViewById(R.id.maxamnt);
        relativeLayout=view.findViewById(R.id.detailsHolder);

        imageButton=view.findViewById(R.id.join_search);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNetworks();
            }
        });
        recyclerView=view.findViewById(R.id.recyclerview_join);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new CustomAdaptor(getActivity());
        adapter.setOnClick(this);
        recyclerView.setAdapter(adapter);

        b1=view.findViewById(R.id.joinrequest_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });



        RelativeLayout relativeLayout=view.findViewById(R.id.joinLayout);
        LayoutLaunchAnimator.startAnim(view,relativeLayout,1);
        reset();
        return view;

    }
    public void showNetworks(){
        String phn=editText.getText().toString();
        if(phn.equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString())){
           // Toast.makeText(getActivity(), "Can't join ur own network", Toast.LENGTH_SHORT).show();
            new GetAvailableNetworks(getActivity(),phn).execute();
        }else{
            new GetAvailableNetworks(getActivity(),phn).execute();
        }
    }



    public void loadDatatoRecyclerView(){

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.Adapter adapter=new CustomAdaptor(getActivity());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClicked(int position) {
        NetWorkClass netWorkClass=availableNetworks.get(searchResullt.get(position));
        tv1.setText(searchResullt.get(position));
         tv2.setText(netWorkClass.getTotalPeopleInMyNetwork().toString());
        tv3.setText(netWorkClass.getMaxAmount().toString());
        relativeLayout.setVisibility(View.VISIBLE);
        b1.setVisibility(View.VISIBLE);
    }

    public void reset(){
        userAccounts.clearSearchList();;
        userAccounts.clearAvailableNetworks();
        adapter.notifyDataSetChanged();
        relativeLayout.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.INVISIBLE);
    }


    class GetAvailableNetworks extends AsyncTask<Void,Void,Void> {

        String phn;
        Context context;
        GetAvailableNetworks(Context context,String phn){
            this.phn=phn;
            this.context=context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FireBaseHelper.flag=false;
            FireBaseHelper.getFireBaseHelper().getUIDFromPhoneNumber(context,phn);
            while(!FireBaseHelper.flag){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("PostExecute:","postExecuteMethodCalled");
            int l= UserAccounts.getUserObject().searchResult.size();
            Log.d("PostExecute","Size"+l);
            adapter.notifyDataSetChanged();
        }


    }
//    public  void loadFragment(){
//        showNetworkDetails=new ShowNetworkDetails();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentManager.beginTransaction()
//                .replace(R.id.networkDetailHolder,showNetworkDetails,"newFragment")
//                .addToBackStack(null)
//                .commit();
//    }


    public void sendRequest(){


    }
}

