/*
 * Copyright (c) 2018 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jalotsav.aalayamnavjivan;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;

/**
 * Created by Jalotsav on 1/17/2018.
 */

public class ActvtyBookView extends AppCompatActivity {

    @BindView(R.id.cordntrlyot_actvty_bookview_main) CoordinatorLayout mCrdntrlyot;
    @BindView(R.id.pdfvwpgr_actvty_bookview) PDFViewPager mPdfVwPgr;

    @BindString(R.string.no_intrnt_cnctn) String mNoInternetConnMsg;
    @BindString(R.string.server_problem_sml) String mServerPrblmMsg;
    @BindString(R.string.internal_problem_sml) String mInternalPrblmMsg;
    @BindString(R.string.allow_permtn_readbook) String mAllowPermsnMsg;
    @BindString(R.string.plzwait_weare_getting_book) String mWaitGettingBook;
    @BindString(R.string.book_not_found) String mBookNotFound;

    MenuItem mMenuItemShare;
    StorageReference mStorageRef;
    String mRequestedBookName, mBookPathLocal;
    Dialog mDialogPrgrs;
    BasePDFPagerAdapter mPDFAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_actvty_book_view);
        ButterKnife.bind(this);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        // for "Uri.fromFile()" in Share book
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String selectedLanguage = getIntent().getStringExtra(AppConstants.PUT_EXTRA_LANGUAGE_NAME);
        if(selectedLanguage.equalsIgnoreCase(AppConstants.LANGUAGE_SHORTCODE_GUJARATI)) {
            mRequestedBookName = AppConstants.BOOKNAME_NAVJIVAN_GU;
            if (mActionBar != null)
                mActionBar.setTitle(getString(R.string.app_name).concat(" - ").concat(getString(R.string.gujarati_sml)));
        } else {
            mRequestedBookName = AppConstants.BOOKNAME_NAVJIVAN_EN;
            if (mActionBar != null)
                mActionBar.setTitle(getString(R.string.app_name).concat(" - ").concat(getString(R.string.english_sml)));
        }

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

        mBookPathLocal = AppConstants.PATH_BOOKS_NAVJIVAN.concat(File.separator + mRequestedBookName);
        File mBookFilePathLocal = new File(mBookPathLocal);
        if(mBookFilePathLocal.exists()) {

            mPDFAdapter = new PDFPagerAdapter(this, mBookPathLocal);
            mPdfVwPgr.setAdapter(mPDFAdapter);
            if(mMenuItemShare != null)
                mMenuItemShare.setVisible(true);
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
        File bookFilePathLocalName = new File(bookFilePathLocal, mRequestedBookName);
        if(bookFilePathLocalName.exists()) bookFilePathLocalName.delete();

        StorageReference booksNavjivanRef = mStorageRef.child(AppConstants.PATH_FIRESTORAGE_BOOK_NAVJIVAN + mRequestedBookName);
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

    // Create chooser for share book
    private void shareBook() {
        try {

            File mBookFilePathLocal = new File(mBookPathLocal);
            if(mBookFilePathLocal.exists()) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mBookFilePathLocal));
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_appname_book_3dots, getString(R.string.app_name))));
            } else
                Snackbar.make(mCrdntrlyot, mBookNotFound, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(mCrdntrlyot, mBookNotFound, Snackbar.LENGTH_LONG).show();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        mMenuItemShare = menu.findItem(R.id.action_share);
        mMenuItemShare.setVisible(false);

        try {

            File mBookFilePathLocal = new File(mBookPathLocal);
            if(mBookFilePathLocal.exists())
                mMenuItemShare.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                break;
            case R.id.action_share:

                shareBook();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPDFAdapter != null) {
            mPDFAdapter.close();
            mPDFAdapter = null;
        }
    }
}
