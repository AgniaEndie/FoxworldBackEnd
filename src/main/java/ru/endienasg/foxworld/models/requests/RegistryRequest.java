package ru.endienasg.foxworld.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistryRequest {
       private String username;
       private String email;
       private String password;
}
