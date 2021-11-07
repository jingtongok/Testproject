package com.mosi.http;

/**
 * @content: Author: gjt66888
 * Description:
 * Time: 2019/12/19
 */
public class NetworkChangeEvent {

    private boolean isType;

    public NetworkChangeEvent(boolean isType) {
        this.isType = isType;
    }

    public boolean isType() {
        return isType;
    }

    public void setType(boolean type) {
        isType = type;
    }
}
