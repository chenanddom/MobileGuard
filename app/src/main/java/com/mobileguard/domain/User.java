package com.mobileguard.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 用户类，封装用户的各个信息
 *
 * @author chendom
 */
public class User implements Parcelable {
    private String userId;
    private String userName;
    private String userPassword;
    private int userAge;
    private String userGender;
    private int userClass;
    private int versionNumber;
    private String userDescription;

    public User() {
    }

    public User(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userPassword = user.getUserPassword();
        this.userAge = user.getUserAge();
        this.userGender = user.getUserGender();
        this.userClass = user.getUserClass();
        this.versionNumber = user.getVersionNumber();
        this.userDescription = user.getUserDescription();


    }

    protected User(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userPassword = in.readString();
        userAge = in.readInt();
        userGender = in.readString();
        userClass = in.readInt();
        versionNumber = in.readInt();
        userDescription = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public int getUserClass() {
        return userClass;
    }

    public void setUserClass(int userClass) {
        this.userClass = userClass;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", userPassword=" + userPassword + ", userAge="
                + userAge + ", userGender=" + userGender + ", userClass=" + userClass + ", versionNumber=" + versionNumber
                + ", userDescription=" + userDescription + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userPassword);
        dest.writeInt(userAge);
        dest.writeString(userGender);
        dest.writeInt(userClass);
        dest.writeInt(versionNumber);
        dest.writeString(userDescription);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
