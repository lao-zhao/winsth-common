package com.winsth.android.libs.assists;

import android.text.method.ReplacementTransformationMethod;

/**
 * Created by aaron.zhao on 2016/4/3.
 */
public class MyReplacementTransformationMethod extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] original = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return original;
    }

    @Override
    protected char[] getReplacement() {
        char[] replacement = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
                'Z'};
        return replacement;
    }
}
