package ro.ub.fmi.xChat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import ro.ub.fmi.xChat.ro.ub.fmi.xChat.Utils.Constants;

public class SettingsActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;


    private CircleImageView circleImageView;
    private TextView tv_displayName;
    private TextView tv_grupa;
    private TextView tv_an;
    private Button btn_changeAvatar;
    private TextView tv_facultate;



    private ProgressDialog progressDialog;

    private StorageReference mStorageRef;



    private  void initComponents(){
        tv_displayName = findViewById(R.id.settings_name);
        tv_grupa = findViewById(R.id.settings_grupa);
        tv_an = findViewById(R.id.settings_an);
        tv_facultate = findViewById(R.id.settings_facultate);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);

        btn_changeAvatar = findViewById(R.id.settings_changeAvatar);
        progressDialog = new ProgressDialog(SettingsActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);


    }




    private void initDialog(){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.txt_loading));
        progressDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        initComponents();

        initDialog();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUid = currentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        mDatabase.keepSynced(true);


        mStorageRef = FirebaseStorage.getInstance().getReference();


        getDataFromDb();


        btn_changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeImg();


            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child("online").setValue("true");

    }

    private void changeImg() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,getString(R.string.txt_chooseAvatar)),
                Constants.AVATAR_RESULT_CODE);
    }

    private void getDataFromDb() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image =  dataSnapshot.child("image").getValue().toString();
                String grupa   = dataSnapshot.child("grupa").getValue().toString();
                String an = dataSnapshot.child("an").getValue().toString();
                final String thumb_image =  dataSnapshot.child("thumb_image").getValue().toString();
                String facultate = dataSnapshot.child("facultate").getValue().toString();


                tv_displayName.setText(name);
                tv_grupa.setText(grupa);
                tv_an.setText(an);
                tv_facultate.setText(facultate);

                if(!(thumb_image.equals("default"))){
                    Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_user).into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(thumb_image).placeholder(R.drawable.default_user).into(circleImageView);
                        }
                    });
                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if(requestCode == Constants.AVATAR_RESULT_CODE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setAspectRatio(1,1).start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String current_userId = currentUser.getUid();
                final String imageName = current_userId + ".jpg";
                final StorageReference filePath  =  mStorageRef.child("Avatars").child(imageName);
                final StorageReference thumb_filePath = mStorageRef.child("Avatars/thumb_image").child(imageName);

                progressDialog.show();
                progressDialog.setTitle(getString(R.string.txt_uploadImg));
                progressDialog.setMessage(getString(R.string.txt_uploadImgWait));

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    mDatabase.child("image").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            }else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),getString(R.string.txt_error),Toast.LENGTH_LONG);
                                            }
                                        }
                                    });
                                };
                            });
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),getString(R.string.txt_errorUploadImage),Toast.LENGTH_LONG).show();
                        }
                    }
                });

                File file_thumbFile = new File(resultUri.getPath());
                Bitmap thumbBitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] thumb_byte;
                try {
                    thumbBitmap = new Compressor(SettingsActivity.this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(file_thumbFile);
                    thumbBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    thumb_byte = baos.toByteArray();
                    UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                thumb_filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUrl = uri;
                                        mDatabase.child("thumb_image").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    if(progressDialog.isShowing())
                                                        progressDialog.dismiss();
                                                }else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),getString(R.string.txt_error),Toast.LENGTH_LONG);
                                                }
                                            }
                                        });
                                    };
                                });
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }





            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
