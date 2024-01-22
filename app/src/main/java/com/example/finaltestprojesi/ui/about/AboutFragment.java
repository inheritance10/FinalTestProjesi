package com.example.finaltestprojesi.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.finaltestprojesi.R;
import com.example.finaltestprojesi.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding fragmentAboutBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAboutBinding = FragmentAboutBinding.inflate(inflater, container, false);
        View rootAddress = fragmentAboutBinding.getRoot();

        Button github_btn = fragmentAboutBinding.githubBtn;
        github_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGitHubPage();
            }
        });

        return rootAddress;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentAboutBinding = null;
    }

    private void openGitHubPage() {
        String githubUrl = "https://github.com/inheritance10/";

        // Intent oluştur ve tarayıcıyı başlat
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(githubUrl));
        startActivity(intent);
    }
}
