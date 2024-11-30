package framework.models.payments;

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
public class PaymentsModel {
    private String order_uuid;
    private String user_uuid;
    private String uuid;
    private String payment_method;
    private String status;
    private String created_at;
    private String updated_at;
    private int amount;
}
