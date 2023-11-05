package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParentProfile extends Fragment {
    private String gmail, userid;
    private FirebaseFirestore db;
    private String age, name, address, email, contact, imageLink;
    TextView textage, textname, textaddress, textemail, textcontact;

    CircleImageView imageParent;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Initialize the permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                    } else {
                        Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_parent_profile, container, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
        textage = view.findViewById(R.id.textParentProfileAge);
        textname = view.findViewById(R.id.textParentProfileName);
        textaddress = view.findViewById(R.id.textParentProfileAddress);
        textemail = view.findViewById(R.id.textParentProfileEmail);
        textcontact = view.findViewById(R.id.textParentProfileContact);
        imageParent = view.findViewById(R.id.imageParent);

        gmail = ((ParentActivity)getActivity()).email;
        userid = ((ParentActivity)getActivity()).userid;
        if(!userid.isEmpty()) {
            ProfileUtils.getProfile(db, userid, getContext(), textage, textname, textaddress,
                    textemail, textcontact, imageParent);
        }
        imageParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.dialog_options);
                Button uploadImageBtn = dialog.findViewById(R.id.uploadImageBtn);
                Button takePhotoBtn = dialog.findViewById(R.id.takePhotoBtn);
                Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request camera permission
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                }
                uploadImageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle upload image option
                        // TODO: Implement your logic here
                        openFileChooser();
                        dialog.dismiss();
                    }
                });takePhotoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle take photo option
                        // TODO: Implement your logic here
                        Intent intent = new Intent (requireContext(), CameraTests.class);
                        intent.putExtra("email", gmail);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle cancel option
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                Uri imageUri = data.getData();
                                uploadImage(imageUri);
                            }
                        }
                    }
                });

        return view;
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private void uploadImage(Uri imageUri) {
        // Create a unique filename for the image (optional)
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + fileName);

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                if (email != null) {
                                    Query query = db.collection("users").whereEqualTo("email", email);
                                    ImageQueryUpload(query, imageUrl);
                                } else {
                                    // Handle the case where the user ID is null
                                    Toast.makeText(requireContext(), "User ID is null", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle unsuccessful uploads
                    }
                });
    }
    private void ImageQueryUpload(Query query, String imageUrl){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Update the "imageUrl" field of each matching document with the download URL
                        db.collection("users").document(document.getId()).update("imageUrl", imageUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Image URL updated successfully in the matching documents
                                        Toast.makeText(requireContext(), "Image URL updated in user documents", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failures
                                        Toast.makeText(requireContext(), "Failed to update image URL in user documents", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // Handle errors while fetching documents
                    Toast.makeText(requireContext(), "Error fetching documents", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}