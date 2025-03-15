package jm.productmanager.repository;

import jm.productmanager.model.BlockedWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockedWordRepository extends JpaRepository<BlockedWord, UUID> {
    boolean existsByName(String name);
}
