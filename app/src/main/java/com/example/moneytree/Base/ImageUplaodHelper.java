package com.example.moneytree.Base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ImageUplaodHelper {

    private static ImageUplaodHelper imageUplaodHelper=null;
    static ImageUplaodHelper getImageUplaodHelper(){
        if(imageUplaodHelper==null){
            imageUplaodHelper=new ImageUplaodHelper();
        }
        return imageUplaodHelper;
    }


//    private void selectImage(Context context) {
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//        final Context context1=context;
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Choose your profile picture");
//
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (options[item].equals("Take Photo")) {
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    context1.startActivityForResult(takePicture, 0);
//
//                } else if (options[item].equals("Choose from Gallery")) {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto , 1);
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }


}
