package app.backend.Repository;

import app.backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String userName);
    Boolean existsByEmail(String email);

    Optional<User> findByUserNameOrEmail(String userName,String email);

    Boolean existsByUserName(String userName);


}
