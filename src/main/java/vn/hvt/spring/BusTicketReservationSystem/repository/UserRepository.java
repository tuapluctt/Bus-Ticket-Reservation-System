package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByFullName(String fullName);
    public User findByPhoneNumber(String phoneNumber);
}
