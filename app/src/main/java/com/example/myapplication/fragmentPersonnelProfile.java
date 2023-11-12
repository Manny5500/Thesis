package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.MotionEvent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import android.Manifest;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragmentPersonnelProfile extends Fragment {
    View view;


    private String userid, gmail;
    Button btnDelete;
    TextView textage, textname, textaddress, textemail, textcontact;
    private FirebaseFirestore db;

    FirebaseUser user;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    CircleImageView imagePersonnel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted, proceed with capturing image
                    } else {
                        Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personnel_profile, container, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
        textage = view.findViewById(R.id.textAge);
        textname = view.findViewById(R.id.textName);
        textaddress = view.findViewById(R.id.textAddress);
        textemail = view.findViewById(R.id.textGmail);
        textcontact = view.findViewById(R.id.textContact);
        imagePersonnel = view.findViewById(R.id.imagePersonnel);
        btnDelete = view.findViewById(R.id.btnDeleteAccount);
        user = ((PersonnelActivity)getActivity()).user;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYesNoDialog();
            }
        });



        imagePersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.dialog_options);
                Button uploadImageBtn = dialog.findViewById(R.id.uploadImageBtn);
                Button takePhotoBtn = dialog.findViewById(R.id.takePhotoBtn);
                Button cancelBtn = dialog.findViewById(R.id.cancelBtn);

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
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
                });

                takePhotoBtn.setOnClickListener(new View.OnClickListener() {
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

        gmail = ((PersonnelActivity)getActivity()).email;

        userid = ((PersonnelActivity)getActivity()).userid;
        if(!userid.isEmpty()) {
           ProfileUtils.getProfile(db, userid, getContext(), textage, textname, textaddress,
                   textemail, textcontact, imagePersonnel);
        }

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
                                if (gmail != null) {
                                    Query query = db.collection("users").whereEqualTo("email", gmail);
                                    ImageQueryUpload(imageUrl);
                                } else {
                                    Toast.makeText(requireContext(), "User ID is null", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    private void ImageQueryUpload(String imageUrl){
        db.collection("users").document(userid).update("imageUrl", imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Image URL updated successfully in the matching documents
                        Toast.makeText(requireContext(), "Image URL updated in user documents", Toast.LENGTH_SHORT).show();
                        ProfileUtils.getProfile(db, userid, getContext(), textage, textname, textaddress,
                                textemail, textcontact, imagePersonnel);
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


    private void showYesNoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to delete your account?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "User account is deleted", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    requireActivity().finish();
                                }
                            }
                        });
                deleteFirestoreData();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteFirestoreData(){
        db.collection("users").document(userid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireContext(), "User deleted sucessfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ProfileUtils.getProfile(db, userid, getContext(), textage, textname, textaddress,
                textemail, textcontact, imagePersonnel);
    }
}