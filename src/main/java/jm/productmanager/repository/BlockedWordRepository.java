package jm.productmanager.repository;

import jm.productmanager.model.BlockedWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockedWordRepository extends JpaRepository<BlockedWord, UUID> {
    @Query("SELECT EXISTS (SELECT 1 FROM BlockedWord bw WHERE :productName ILIKE '%' || bw.name || '%')")
    boolean containsBlockedWord(@Param("productName") String productName);
}
