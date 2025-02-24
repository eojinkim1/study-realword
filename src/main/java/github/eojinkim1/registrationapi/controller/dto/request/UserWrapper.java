package github.eojinkim1.registrationapi.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper<T> { //<> <- 제네릭 타입은 실행할 때 타입을 결정함
    private T user;
}
