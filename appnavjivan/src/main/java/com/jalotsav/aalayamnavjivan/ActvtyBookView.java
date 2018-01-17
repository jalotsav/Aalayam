package com.jalotsav.aalayamnavjivan;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.jalotsav.aalayamnavjivan.common.AppConstants;
import com.jalotsav.aalayamnavjivan.common.GeneralFunctions;

import java.io.File;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jalotsav on 1/17/2018.
 */

public class ActvtyBookView extends AppCompatActivity {

    @BindView(R.id.cordntrlyot_actvty_bookview_main) CoordinatorLayout mCrdntrlyot;
    @BindView(R.id.pdfvw_actvty_bookview) PDFView mPdfVw;

    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.internal_problem_sml) String mInternalPrblmMsg;
    @BindString(R.string.allow_permtn_readbook) String mAllowPermsnMsg;
    @BindString(R.string.plzwait_weare_getting_book) String mWaitGettingBook;

    StorageReference mStorageRef;
    String mBookPathLocal;
    Dialog mDialogPrgrs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_book_view);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        checkAppPermission();
    }

    // Check Storage permission for use
    public void checkAppPermission() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!isCheckSelfPermission())
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.REQUEST_APP_PERMISSION);
            else {
                getBookFromLocalStorage();
            }
        } else {
            getBookFromLocalStorage();
        }
    }

    private boolean isCheckSelfPermission(){

        int selfPermsnStorage = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return  selfPermsnStorage == PackageManager.PERMISSION_GRANTED;
    }

    // Get Book from local storage and update on UI
    private void getBookFromLocalStorage() {

        mBookPathLocal = AppConstants.PATH_BOOKS_NAVJIVAN.concat(File.separator + AppConstants.BOOKNAME_NAVJIVAN_GU);
        File mBookFilePathLocal = new File(mBookPathLocal);
        if(mBookFilePathLocal.exists()) {
            mPdfVw.fromFile(mBookFilePathLocal)
                    .swipeHorizontal(true)
                    .enableSwipe(true)
                    .load();
        } else {

            if (GeneralFunctions.isNetConnected(this))
                getBookFromFirebaseStorage();
            else Snackbar.make(mCrdntrlyot, mNoInternetConnMsg, Snackbar.LENGTH_LONG).show();
        }
    }

    // Get book from Firebase storage
    private void getBookFromFirebaseStorage() {

        mDialogPrgrs = new Dialog(this);
        mDialogPrgrs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogPrgrs.setContentView(R.layout.lo_dialog_progress);
        Window window = mDialogPrgrs.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialogPrgrs.show();

        TextView tvDialogMessage = mDialogPrgrs.findViewById(R.id.tv_dialog_progress_message);
        final TextView tvDialogPrcntg = mDialogPrgrs.findViewById(R.id.tv_dialog_progress_prcntg);
        tvDialogMessage.setText(mWaitGettingBook);
        tvDialogPrcntg.setText(String.format(Locale.getDefault(), "%1$d%%", 0));

        File bookFilePathLocal = new File(AppConstants.PATH_BOOKS_NAVJIVAN);
        bookFilePathLocal.mkdirs();
        File bookFilePathLocalName = new File(bookFilePathLocal, AppConstants.BOOKNAME_NAVJIVAN_GU);
        if(bookFilePathLocalName.exists()) bookFilePathLocalName.delete();

        StorageReference booksNavjivanRef = mStorageRef.child(AppConstants.PATH_FIRESTORAGE_BOOK_NAVJIVAN + AppConstants.BOOKNAME_NAVJIVAN_GU);
        booksNavjivanRef.getFile(bookFilePathLocalName)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        mDialogPrgrs.dismiss();
                        getBookFromLocalStorage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        mDialogPrgrs.dismiss();
                        Snackbar.make(mCrdntrlyot, mServerPrblmMsg, Snackbar.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        tvDialogPrcntg.setText(String.format(Locale.getDefault(), "%1$d%%", (int) progress));
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == AppConstants.REQUEST_APP_PERMISSION) {

            if(grantResults.length > 0) {

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getBookFromLocalStorage();
                } else
                    Snackbar.make(mCrdntrlyot, mAllowPermsnMsg, Snackbar.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
