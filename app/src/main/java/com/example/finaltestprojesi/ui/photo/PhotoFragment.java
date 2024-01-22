package com.example.finaltestprojesi.ui.photo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finaltestprojesi.Login;
import com.example.finaltestprojesi.R;
import com.example.finaltestprojesi.databinding.FragmentAboutBinding;
import com.example.finaltestprojesi.databinding.FragmentPhotoBinding;
import com.example.finaltestprojesi.ui.gallery.GalleryFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PhotoFragment extends Fragment {
    private FragmentPhotoBinding fragmentPhotoBinding;

    private int Images = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPhotoBinding = FragmentPhotoBinding.inflate(inflater, container, false);
        View rootAddress = fragmentPhotoBinding.getRoot();

        Button chooseBtn = fragmentPhotoBinding.chooseBtn;

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                startActivityForResult(intent, Images);
            }
        });

        return rootAddress;
    }

    @Override
    public void onActivityResult(int code_request, int result, Intent data){
        super.onActivityResult(code_request, result, data);
        if(code_request == Images && result == RESULT_OK && data != null){
            Uri choose_file = data.getData();
            String [] filePaths = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(choose_file, filePaths, null, null, null);

            cursor.moveToFirst();

            int column = cursor.getColumnIndex(filePaths[0]);
            String image_path = cursor.getString(column);
            cursor.close();

            ImageView imageView = fragmentPhotoBinding.imageView2;

            imageView.setImageBitmap(BitmapFactory.decodeFile(image_path));

            Button shareBtn = fragmentPhotoBinding.shareBtn;

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference firebaseStorageReferance = firebaseStorage.getReference();
                    StorageReference imageReferance = firebaseStorageReferance.child("files/" + UUID.randomUUID().toString());

                    UploadTask upload = imageReferance.putFile(choose_file);

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    upload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isSuccessful());
                            Uri downloadFileUrl = uri.getResult();

                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                            DocumentReference userReferance = firebaseFirestore.collection("users")
                                    .document(auth.getUid());

                            userReferance.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String firstname = documentSnapshot.getString("firstname");

                                    Map<String , Object> data = new HashMap<>();
                                    data.put("fileUrl",downloadFileUrl.toString());
                                    data.put("firstname", firstname);

                                    List<String> tag = new ArrayList<>();
                                    if (fragmentPhotoBinding.nature.isChecked()){
                                        tag.add("Doğa");
                                    }
                                    if (fragmentPhotoBinding.sea.isChecked()){
                                        tag.add("Deniz");
                                    }
                                    if (fragmentPhotoBinding.robot.isChecked()){
                                        tag.add("Robot");
                                    }
                                    if (fragmentPhotoBinding.spor.isChecked()){
                                        tag.add("Spor");
                                    }


                                    data.put("tag", tag);

                                    firebaseFirestore.collection("posts").document().set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getActivity(),"Başarılı", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText( getActivity(),"Başarısız", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentPhotoBinding = null;
    }

}