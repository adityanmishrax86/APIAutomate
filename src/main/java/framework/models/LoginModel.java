package framework.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {
    private String username;
    private String password;

    public static LoginModel createLoginUser(String username, String password) {
        return LoginModel.builder()
                .username(username)
                .password(password)
                .build();
    }


}


