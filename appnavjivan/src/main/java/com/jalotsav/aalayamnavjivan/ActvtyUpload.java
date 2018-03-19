package com.jalotsav.aalayamnavjivan;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jalotsav.aalayamnavjivan.common.AppConstants;
import com.jalotsav.aalayamnavjivan.models.MdlBooks;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jalotsav on 1/17/2018.
 */

public class ActvtyUpload extends AppCompatActivity {

    private static final String TAG = ActvtyUpload.class.getSimpleName();

    @BindView(R.id.btn_actvty_upload) Button mBtnUpload;
    @BindView(R.id.prgrsbr_actvty_upload) ProgressBar mPrgrsbrMain;

    StorageReference mStorageRef;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_upload);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(AppConstants.PATH_FIREDATABASE_BOOK_NAVJIVAN);

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadFile();
            }
        });
    }

    private void uploadFile() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        Uri file = Uri.fromFile(new File("/storage/emulated/0/bluetooth/book_navjivan_gu.pdf"));
        StorageReference booksNavjivanRef = mStorageRef.child(AppConstants.PATH_FIRESTORAGE_BOOK_NAVJIVAN + AppConstants.BOOKNAME_NAVJIVAN_GU);

        booksNavjivanRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mPrgrsbrMain.setVisibility(View.GONE);
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.e(TAG, "onSuccess: DownloadURL: " + downloadUrl);

                        // Insert Book details in to Firebase Database
                        MdlBooks objMdlBooks = new MdlBooks(
                                AppConstants.BOOKNAME_NAVJIVAN_GU,
                                downloadUrl != null ? downloadUrl.toString() : "",
                                AppConstants.LANGUAGE_SHORTCODE_GUJARATI);
                        mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(objMdlBooks);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        mPrgrsbrMain.setVisibility(View.GONE);
                    }
                });
    }

    /*private void downloadFile() {

        File localFile = File.createTempFile("images", "jpg");
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }*/
}
