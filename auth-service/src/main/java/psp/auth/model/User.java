package psp.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String username;
    @Column
    private byte[] password;
    @Column
    private Role role;

    public User(String username, byte[] password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
