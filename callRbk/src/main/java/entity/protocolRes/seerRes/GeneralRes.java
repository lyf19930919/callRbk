package entity.protocolRes.seerRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralRes {
    public Integer ret_code;
    public String err_msg;
}
