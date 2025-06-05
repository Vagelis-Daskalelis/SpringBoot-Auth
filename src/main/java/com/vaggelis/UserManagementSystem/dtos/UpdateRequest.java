package com.vaggelis.UserManagementSystem.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequest {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 3, max = 16)
    private String uname;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Wrong email address")
    private String email;
    @NotNull
    @Size(min = 3, max = 16)
    private String password;
}
