package com.example.firebaseemailaccount;

// 사용자 계정 정보 모델 클래스
public class UserAccount {
    private String idToken;     // Firebase Uid (고유 토큰정보, 고유 key값)
    private String emailId;     // 이메일아이디
    private String password;    // 비밀번호
    private String name;        // 사용자 닉네임
    private String introduce;   // 사용자 소개 문구

    // Firebase Realtime DB를 사용할 때 빈 생성자가 필요 (안 그럼 Error)
    public UserAccount() { }

    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIntroduce() { return introduce; }
    public void setIntroduce(String introduce) { this.introduce = introduce; }
}
