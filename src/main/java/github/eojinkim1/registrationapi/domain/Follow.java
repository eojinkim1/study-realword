package github.eojinkim1.registrationapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follows", uniqueConstraints = @UniqueConstraint (columnNames = {"follower_id", "following_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false ,fetch = FetchType.LAZY)
    private User follower;

    @ManyToOne(optional = false ,fetch = FetchType.LAZY)
    private User following;

    public static Follow of(User follower, User following) {
        return new Follow(null, follower, following);
    }
}
