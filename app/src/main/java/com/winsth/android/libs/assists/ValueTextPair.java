package com.winsth.android.libs.assists;

/**
 * Created by aaron.zhao on 2016/4/3.
 */
public class ValueTextPair {
    private String _value = "";
    private String _text = "";

    public ValueTextPair() {
        this._value = "";
        this._text = "";
    }

    public ValueTextPair(String value, String text) {
        this._value = value;
        this._text = text;
    }

    public String getValue() {
        return this._value;
    }

    public String getText() {
        return this._text;
    }

    @Override
    public String toString() {
        return this._text;
    }
}
