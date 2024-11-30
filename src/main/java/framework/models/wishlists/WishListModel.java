package framework.models.wishlists;

import com.fasterxml.jackson.annotation.JsonInclude;
import framework.models.games.GamesModel;
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
public class WishListModel {
    private List<GamesModel> items;
    private String user_uuid;
}
