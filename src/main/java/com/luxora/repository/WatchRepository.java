package com.luxora.repository;

import com.luxora.model.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByCategoryIgnoreCase(String category);

    List<Watch> findByBrandIgnoreCase(String brand);

    List<Watch> findByFeaturedTrue();

    List<Watch> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String brand);
}
