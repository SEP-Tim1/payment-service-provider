package psp.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    @Pattern(regexp = "^[A-Za-z0-9]{8,50}$", message = "Username must contain between 8 and 50 characters and can contain only letters and digits")
    private String username;
    @Column
    @Pattern(regexp = "^\\$2[ayb]\\$.{56}$", message = "Password format invalid")
    private String password;
    @Column
    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
