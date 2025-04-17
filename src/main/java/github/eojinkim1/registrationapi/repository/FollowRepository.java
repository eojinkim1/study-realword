package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Follow;
import github.eojinkim1.registrationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);

    @Query("SELECT f.following FROM Follow f WHERE f.follower = :viewer")
    List<User> findFollowedUsers(@Param("viewer") User viewer);
}
