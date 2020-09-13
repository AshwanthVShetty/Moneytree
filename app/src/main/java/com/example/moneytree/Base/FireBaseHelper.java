package com.example.moneytree.Base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;

import com.example.moneytree.Profile;
import com.example.moneytree.UserAccounts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FireBaseHelper {

    FirebaseFirestore db;
    static public boolean flag;
    FirebaseStorage firebaseStorage;
    final String TAG="FirebaseHelper";

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    static private FireBaseHelper fireBaseHelper=null;
    static public String uIDobtainedUsingPhn=null;

    private UserAccounts userAccounts=UserAccounts.getUserObject();
    static public FireBaseHelper getFireBaseHelper(){
        if(fireBaseHelper==null){
            fireBaseHelper=new FireBaseHelper();
        }
        return fireBaseHelper;
    }

    FireBaseHelper(){

        //setup for firestore
        setup();

    }
    public void setup() {
        // [START get_firestore_instance]
        db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]

        //instantiate FirebaseStorage object
        firebaseStorage=FirebaseStorage.getInstance();
    }

    public void addUserDoc(final String uid, final String pno){


        Map<String,Object> user=new HashMap<>();
        user.put("uname","");
        user.put("imageURL",null);
        user.put("creditScore",0);
        user.put("phoneNumber",pno);
        user.put("#createdNetworks",0);
        user.put("#joinedNetworks",0);

        db.collection("Users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                addPhoneDoc(uid, pno);
                setFlag(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
                setFlag(false);
            }
        });

    }

    public void  getUserDocument(final Context context, String uid){
        userAccounts.setUid(uid);
        final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();
        final DocumentReference documentReference = db.collection("Users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   DocumentSnapshot documentSnapshot = task.getResult();
                   if(documentSnapshot.exists()){
                       userAccounts.setUname(documentSnapshot.get("uname")!=null?documentSnapshot.get("uname").toString():"");

                       userAccounts.setImageurl(documentSnapshot.get("imageURL")!=null?documentSnapshot.get("imageURL").toString():"");
                       userAccounts.setCreditscore(Integer.parseInt(documentSnapshot.get("creditScore").toString()));
                       userAccounts.setUph(documentSnapshot.get("phoneNumber").toString());
                       userAccounts.setNumOfNetworkscreated(Integer.parseInt(documentSnapshot.get("#createdNetworks").toString()));
                       userAccounts.setNumOfJoinedNetwork(Integer.parseInt(documentSnapshot.get("#joinedNetworks").toString()));


                       if(Integer.parseInt(documentSnapshot.get("#createdNetworks").toString())!=0)
                       loadNetworkdetails(documentReference);

//                       context.startActivity(new Intent(context, Profile.class));
                       progressDialog.dismiss();
                   }
                   else {
                       Log.d(TAG, "No such document");
                   }
               }
               else {
                   Log.d(TAG, "get failed with ", task.getException());
               }
            }
        });

    }
    public void uploadImage(Context context, String uid, byte[] data){
        final String userID=uid;
        final Context context1=context;
        final ProgressDialog progressDialog=showProgressDialougue(context);
        String img_file_name= FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference=firebaseStorage.getReference();
        final StorageReference imageReference=storageReference.child("profile_images/"+img_file_name);
        UploadTask uploadTask = imageReference.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("error",exception.toString());
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
//                Task<Uri> downloadUri=taskSnapshot.getMetadata().getReference().getDownloadUrl();
//                if(downloadUri.isSuccessful()){
//                    String downlaodurl=downloadUri.getResult().toString();
//                    fireBaseHelper.upadateUserProfileImageUrl(context1,userID,downlaodurl);
//                }else{
//                    Log.d("Url Fetch:","failed");
//                }

                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downlaodURL=null;
                        downlaodURL=uri.toString();
                        if(downlaodURL!=null){
                            fireBaseHelper.upadateUserProfileImageUrl(context1,userID,downlaodURL);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Url Fetch:",e.toString());
                    }
                });
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                Log.d("Upload Staus:","Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
                Log.d("Upload Staus:","Paused");
            }
        });

    }

    public void upadateUserProfileImageUrl(Context context,String uid,String imageUrl){
        userAccounts.setImageurl(imageUrl);
        final ProgressDialog progressDialog=showProgressDialougue(context);
        DocumentReference documentReference=db.collection("Users").document(uid);
        documentReference.update("imageURL",imageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("imageURL Upddate:","successfully updated image url");
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("imageURL Upddate:",e.toString());
                progressDialog.dismiss();
            }
        });
        userAccounts.setImageurl(imageUrl);
    }
    public void updateUserProfile(String uid,String key,int value){
        DocumentReference documentReference=db.collection("Users").document(uid);
        documentReference.update(key,value).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "UserProfile Updated successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }

    public void addPhoneDoc(String uid, String ph){
        DocumentReference documentReference = db.collection("PhoneNumberCollection").document(ph);
        Map<String,Object> phoneNumber = new HashMap<>();
        phoneNumber.put("UID",uid);
        documentReference.set(phoneNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Phone number mapped to UID successfully!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document : Phone number mapping to UID", e);
            }
        });

    }


    public void createNetwork(final Context context, final String uid, final String network_name, final double maxamount){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        DocumentReference documentReference=db.collection("NetworkCollection")
                .document(uid).collection("MyNetworkCollection").document(network_name);
        final Map<String,Object> data=new HashMap<>();
        data.put("maxAmount",maxamount);
        data.put("#usersJoined",0);

        documentReference.set(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Network created successfully in the NetworkCollection");
                DocumentReference documentReference1=db.collection("Users").document(uid).
                        collection("MyCreatedNetworks").document(network_name);
                documentReference1.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Network created successfully in the USER profile");
                        userAccounts.addNetwork(network_name,maxamount,0);
                        updateUserProfile(uid,"#createdNetworks",userAccounts.getNumOfNetworkscreated()+1);
                        userAccounts.setNumOfNetworkscreated(userAccounts.getNumOfNetworkscreated()+1);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,e.toString());
                    }
                });
            }
        });

    }


    public void loadNetworkdetails(DocumentReference documentReference){
        CollectionReference cref=documentReference.collection("MyCreatedNetworks");
        cref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document:task.getResult()) {
                         userAccounts.addNetwork(document.getId().toString(),Double.parseDouble(document.get("maxAmount").toString()),Integer.parseInt(document.get("#usersJoined").toString()));
                    }
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"Loaded network details successfully to userAccounts");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
            }
        });
    }


    public void getAvailableNetworks(Context context){
        final ProgressDialog progressDialog=new ProgressDialog(context);

        userAccounts.clearSearchList();
        userAccounts.clearAvailableNetworks();
        CollectionReference collectionReference=db.collection("Users").document(FireBaseHelper.uIDobtainedUsingPhn)
                .collection("MyCreatedNetworks");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()) {
                        userAccounts.addToSearchList(queryDocumentSnapshot.getId());
                        userAccounts.addToAvailableNetworks(queryDocumentSnapshot.getId()
                                ,Double.parseDouble(queryDocumentSnapshot.get("maxAmount").toString())
                        ,Integer.parseInt(queryDocumentSnapshot.get("#usersJoined").toString()));
                        Log.d(TAG,"Successfully Updated the Search Results to UserAccounts");
                        FireBaseHelper.flag=true;
                    }
                }

                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
                progressDialog.dismiss();
            }
        });

    }



    public void getUIDFromPhoneNumber(final Context context, String ph){
        final ProgressDialog progressDialog=new ProgressDialog(context);
        FireBaseHelper.uIDobtainedUsingPhn=null;
        DocumentReference documentReference = db.collection("PhoneNumberCollection").document(ph);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()){
                        FireBaseHelper.uIDobtainedUsingPhn=documentSnapshot.get("UID").toString();
                        progressDialog.dismiss();
                        getAvailableNetworks(context);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,e.toString());
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG,"Phone document retrieved successfully");
            }
        });
    }


    public ProgressDialog showProgressDialougue(Context context){
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();
        return progressDialog;
    }






}
