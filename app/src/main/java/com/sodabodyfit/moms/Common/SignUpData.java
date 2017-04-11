package com.sodabodyfit.moms.Common;

/**
 * Created by Mars on 3/5/2017.
 */

public class SignUpData {
    public String name = "";
    public String email= "";
    public String password= "";
    public String password_confirmation= "";
    public String birthday = "";
    public String gender = "";
    public String weight= "";
    public String height = "";
    public String training_experience = "";

    public SignUpData(String strName, String strEmail, String strPassword, String strBirthday, String strGender, String strWeight, String strHeight, String strExperience) {
        name = strName;
        email = strEmail;
        password = strPassword;
        password_confirmation = strPassword;
        birthday = strBirthday;
        gender = strGender;
        weight = strWeight;
        height = strHeight;
        training_experience = strExperience;
    }
}
