package com.mosi.update.model;

import java.io.File;

/**
 * @content: Author: gjt66888
 * Description:
 * Time: 2021/1/28
 */
public class UpdateEvent {

    private int code;

    private File file;

    private String url;

    public UpdateEvent(int code, File file) {
        this.code = code;
        this.file = file;
    }

    public UpdateEvent(int code, String url) {
        this.code = code;
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
