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

package com.jalotsav.aalayamnavjivan.models;

/**
 * Created by Jalotsav on 1/17/2018.
 */

public class MdlBooks {

    String name, url, langShortCode;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public MdlBooks() {
    }

    public MdlBooks(String name, String url, String langShortCode) {
        this.name = name;
        this.url = url;
        this.langShortCode = langShortCode;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getLangShortCode() {
        return langShortCode;
    }
}
