package framework.models.games;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GamesModel {
    private String title;
    private String uuid;
    private int price;
    private List<String> category_uuids;

}
