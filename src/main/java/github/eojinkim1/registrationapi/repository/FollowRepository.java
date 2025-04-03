package github.eojinkim1.registrationapi.repository;

import github.eojinkim1.registrationapi.domain.Follow;
import github.eojinkim1.registrationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);

}
