package com.example.moneytree.BottomNavFragments;

import android.animation.Animator;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneytree.Base.FireBaseHelper;
import com.example.moneytree.Base.LayoutLaunchAnimator;
import com.example.moneytree.R;
import com.google.firebase.auth.FirebaseAuth;

public class CreateFragment extends Fragment {

    EditText editText,editText1;
    Button button;
    FireBaseHelper fireBaseHelper = FireBaseHelper.getFireBaseHelper();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.frg_create,container,false);
        RelativeLayout relativeLayout=view.findViewById(R.id.createLayout);
        LayoutLaunchAnimator.startAnim(view,relativeLayout,0);

        editText = view.findViewById(R.id.network_name);
        editText1 = view.findViewById(R.id.maxamnt);
        button = view.findViewById(R.id.createNetworkButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireBaseHelper.createNetwork(getActivity(),FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        editText.getText().toString(),Double.parseDouble(editText1.getText().toString()));
            }
        });
        return  view;
    }
}
