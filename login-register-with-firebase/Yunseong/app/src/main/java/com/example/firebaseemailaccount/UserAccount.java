package com.example.firebaseemailaccount;

//사용자 계성 정보 모델 클래스

public class UserAccount {
    private String email;
    private String password;
    private String idToken;
    private String name;
    private String introduce;

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public UserAccount() { } //빈 생성자가 필요 (firebase 관련)

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
