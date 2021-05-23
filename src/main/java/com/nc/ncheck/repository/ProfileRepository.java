package com.nc.ncheck.repository;

import com.nc.ncheck.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUsername(String username);

    boolean existsByUsername(String username);
}

