package framework.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String avatar_url;  // Field renamed to follow camelCase convention
    private String uuid;

    public static UserModel createTestUser() {
        return UserModel.builder()
                .email("test.user2@gmail.com")
                .password("securePassword!123")
                .name("Test User12")
                .nickname("Tester12")
                .build();
    }
}
