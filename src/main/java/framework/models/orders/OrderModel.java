package framework.models.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import framework.models.carts.CartItemModel;
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
public class OrderModel {
    private String uuid;
    private String user_uuid;
    private String status;
    private int total_price;
    private String created_at;
    private String updated_at;
    private List<CartItemModel> items;
}
