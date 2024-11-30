package framework.models.carts;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CartModel {
    private String user_uuid;
    private int total_price;
    private List<CartItemModel> items;
}
