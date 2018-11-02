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
