package framework.models.reviews;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewsModel {
    private String uuid;
    private String user_uuid;
    private int score;
    private String title;
    private String body;
    private String created_at;
    private String updated_at;
}
