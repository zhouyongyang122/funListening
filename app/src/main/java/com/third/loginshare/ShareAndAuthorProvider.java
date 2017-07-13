package com.third.loginshare;

import com.third.loginshare.interf.IAuthor;
import com.third.loginshare.interf.IShare;

import java.util.HashSet;

/**
 * Created by weilong zhou on 2015/9/9.
 * Email:zhouwlong@gmail.com
 */

abstract class ShareAndAuthorProvider implements IShare, IAuthor {
    private HashSet<Integer> mShareTypes;
    private int mAuthorType = -1;

    public ShareAndAuthorProvider() {
        mShareTypes = new HashSet<Integer>();
    }

    public void addShareType(int shareType) {
        mShareTypes.add(shareType);
    }

    public void setAuthorType(int authorType) {
        mAuthorType = authorType;
    }

    public boolean checkIfContainShareType(int shareType) {
        return mShareTypes.contains(shareType);
    }

    public boolean checkAuthorTypeEqual(int authorType) {
        return authorType == mAuthorType;
    }
}