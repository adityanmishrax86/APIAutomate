package framework.models.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {
    private String email;
    private String password;

    public static LoginModel createLoginUser(String email, String password) {
        return LoginModel.builder()
                .email(email)
                .password(password)
                .build();
    }


}


