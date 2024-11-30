package framework.models.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import framework.models.generic.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseModel {
    private Meta meta;
    private List<UserModel> users;

}
