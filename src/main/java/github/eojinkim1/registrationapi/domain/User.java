package github.eojinkim1.registrationapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String username;
    private String bio;
    private String image;

    public static User registration(String email, String password, String username) {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .build();
    }
}
