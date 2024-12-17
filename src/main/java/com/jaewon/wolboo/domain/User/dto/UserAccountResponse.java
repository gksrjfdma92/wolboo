package com.jaewon.wolboo.domain.User.dto;

import com.jaewon.wolboo.domain.User.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponse {
    private String userName;
    private String email;
    private String phoneNumber;

    public UserAccountResponse (UserAccount userAccount) {
        this.userName = userAccount.getUserName();
        this.email  = userAccount.getEmailAddress();
        this.phoneNumber = userAccount.getPhoneNumber();
    }

}
