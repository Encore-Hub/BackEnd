package com.team6.backend.favorite.pfmc.repository;

import com.team6.backend.favorite.pfmc.entity.FavoritePfmc;
import com.team6.backend.member.entity.Member;
import com.team6.backend.pfmc.entity.Pfmc;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoritePfmcRepository extends CrudRepository<FavoritePfmc, Long> {
    List<FavoritePfmc> findByMember(Member member);

    List<FavoritePfmc> findByMemberAndPfmc(Member member, Pfmc pfmc);
}
