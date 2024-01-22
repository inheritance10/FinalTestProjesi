package com.example.finaltestprojesi.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.finaltestprojesi.ViewActivity;
import com.example.finaltestprojesi.databinding.FragmentLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding fragmentLogoutBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLogoutBinding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = fragmentLogoutBinding.getRoot();

        // Çıkış butonuna tıklama işlemi
        fragmentLogoutBinding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                navigateToLoginActivity();
            }
        });

        return root;
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(getActivity(), ViewActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentLogoutBinding = null;
    }
}
