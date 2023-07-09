package ru.endienasg.foxworld.models.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String username;
    private String uuid;
    private String token;
}
