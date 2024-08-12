package com.team6.backend.refreshToken.repository;

import com.team6.backend.refreshToken.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
