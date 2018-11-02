package com.jalotsav.aalayamnavjivan.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by Jalotsav on 1/16/2018.
 */

public interface AppConstants {

    String PATH_FIRESTORAGE_BOOK_NAVJIVAN = "books/navjivan/";
    String PATH_FIREDATABASE_BOOK_NAVJIVAN = "books/navjivan";

    String BOOKNAME_NAVJIVAN_GU = "book_navjivan_gu.pdf";
    String BOOKNAME_NAVJIVAN_EN = "book_navjivan_en.pdf";

    // Book Language short code
    String LANGUAGE_SHORTCODE_GUJARATI = "gu";
    String LANGUAGE_SHORTCODE_ENGLISH = "en";

    // Request Keys
    int REQUEST_APP_PERMISSION = 101;

    // Directory name
    String DIR_BOOKS_SML = "books";
    String DIR_NAVJIVAN_SML ="navjivan";

    // File & Path of External storage
    String AALAYAM = "Aalayam";
    String EXTRNL_STORAGE_PATH_STRING = Environment.getExternalStorageDirectory().getAbsolutePath();
    String PATH_BOOKS = EXTRNL_STORAGE_PATH_STRING + File.separator + AALAYAM + File.separator + DIR_BOOKS_SML;
    String PATH_BOOKS_NAVJIVAN = PATH_BOOKS.concat(File.separator + DIR_NAVJIVAN_SML);

    // PutExtra Keys
    String PUT_EXTRA_LANGUAGE_NAME = "comeFrom";
}
