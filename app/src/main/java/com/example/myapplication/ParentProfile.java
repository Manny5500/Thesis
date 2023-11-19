package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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
    FirebaseUser user;
    private String age, name, address, email, contact, imageLink;
    TextView textage, textname, textaddress, textemail, textcontact;
    Button btnDelete;

    CircleImageView imageParent;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    Dialog dialog2;
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
        btnDelete = view.findViewById(R.id.btnDeleteAccount);

        gmail = ((ParentActivity)getActivity()).email;
        userid = ((ParentActivity)getActivity()).userid;
        user = ((ParentActivity)getActivity()).user;

        dialog2 = new Dialog(requireContext());
        dialog2.setContentView(R.layout.dialog_loader);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);


        if(!userid.isEmpty()) {
            ProfileUtils.getProfile(db, userid, getContext(), textage, textname, textaddress,
                    textemail, textcontact, imageParent);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYesNoDialog();

            }
        });
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
                        Intent intent = new Intent (requireContext(), CameraTests.class);
                        intent.putExtra("email", gmail);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + fileName);
        dialog2.show();
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
                    }
                });
    }
    private void ImageQueryUpload(Query query, String imageUrl){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("users").document(document.getId()).update("imageUrl", imageUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog2.dismiss();
                                        ProfileUtils.getProfile(db, userid, getContext(), textage, textname, textaddress,
                                                textemail, textcontact, imageParent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(requireContext(), "Failed to update image URL in user documents", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(requireContext(), "Error fetching documents", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        db.collection("children").whereEqualTo("gmail", gmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                db.collection("children").document(documentSnapshot.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                        } else {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Error querying database", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onResume() {
        super.onResume();
        ProfileUtils.getProfile(db, userid, getContext(), textage, textname, textaddress,
                textemail, textcontact, imageParent);
    }
}