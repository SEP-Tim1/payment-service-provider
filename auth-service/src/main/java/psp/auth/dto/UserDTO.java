package psp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    @Pattern(regexp = "^[A-Za-z0-9]{8,50}$", message = "Username must contain between 8 and 50 characters and can contain only letters and digits")
    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,50}$", message = "Password must contain between 8 and 50 characters, it must include lower-case and upper-case letters, digits and special characters (@#$%^&+=)")
    private String password;
}
