package com.example.moneytree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.moneytree.Base.FireBaseHelper;
import com.example.moneytree.Base.ImageLoadTask;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity {
    ImageView imageView;
    TextView textView, textView1, textView2,textView3,textView4;
    EditText editText;
    UserAccounts userAccounts = UserAccounts.getUserObject();
    FloatingActionButton floatingActionButton;
    FireBaseHelper fireBaseHelper=FireBaseHelper.getFireBaseHelper();
    Button b1;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textView = findViewById(R.id.uid);
        textView1 = findViewById(R.id.uphNo);
        textView2 = findViewById(R.id.ucreditScore);
        textView3 = findViewById(R.id.nCreated);
        textView4 = findViewById(R.id.nJoined);
        editText = findViewById(R.id.uname);
        imageView = findViewById(R.id.profieImage);

        floatingActionButton = findViewById(R.id.image_fab_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(Profile.this);
            }
        });
        b1=findViewById(R.id.update_btn);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(Profile.this);
                progressDialog.show();
                update();
            }
        });
        textView.setText(userAccounts.getUid());
        textView1.setText(userAccounts.getUph());
        textView2.setText("" + userAccounts.getCreditscore());
        textView3.setText("" + userAccounts.getNumOfNetworkscreated());
        textView4.setText("" + userAccounts.getNumOfJoinedNetwork());

        if (userAccounts.getUname() != null) {
            editText.setText(userAccounts.getUname());
        }
        if (userAccounts.getImageurl() != null) {
            //load image
            //new ImageLoadTask(userAccounts.getImageurl(),imageView);
//            Picasso.get()
//                    .load(userAccounts.getImageurl())
//                    .resize(100,100)
//                    .into(imageView);
            Glide.with(Profile.this).load(userAccounts.getImageurl()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Log.d("GlideLoader:","Image loading task failed");
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    Log.d("GlideLoader:","Resource ready");
                    return false;
                }
            }).into(imageView);
        }


//        new  AsynchClassGetProfile().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        imageView.setImageURI(selectedImage);
                    }


                    break;
            }
        }
    }

//    class AsynchClassGetProfile extends AsyncTask<Void,Void,Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            FireBaseHelper.getFireBaseHelper().getUserDocument(Profile.this,FirebaseAuth.getInstance().getCurrentUser().getUid());
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//           textView.setText(userAccounts.getUid());
//           textView1.setText(userAccounts.getUph());
//           textView2.setText(""+userAccounts.getCreditscore());
//           if(userAccounts.getUname()!=null){
//               editText.setText(userAccounts.getUname());
//           }
//           if(userAccounts.getImageurl()!=null){
//               //load image
//           }
//        }
//    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void update() {
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        fireBaseHelper.uploadImage(Profile.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),data);
    }


}

