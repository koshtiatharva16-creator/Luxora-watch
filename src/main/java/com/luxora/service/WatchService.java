package com.luxora.service;

import com.luxora.model.Watch;
import com.luxora.repository.WatchRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WatchService {
    private final WatchRepository watches;

    public WatchService(WatchRepository watches) {
        this.watches = watches;
    }

    public List<Watch> all() {
        return watches.findAll();
    }

    public List<Watch> featured() {
        return watches.findByFeaturedTrue();
    }

    public List<Watch> category(String category) {
        return category == null || category.isBlank() ? all() : watches.findByCategoryIgnoreCase(category);
    }

    public List<Watch> search(String q) {
        return q == null || q.isBlank() ? all()
                : watches.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(q, q);
    }

    public Watch get(Long id) {
        return watches.findById(id).orElseThrow(() -> new IllegalArgumentException("Watch not found: " + id));
    }

    public Watch save(Watch watch) {
        return watches.save(watch);
    }

    public void delete(Long id) {
        watches.deleteById(id);
    }

    public long count() {
        return watches.count();
    }
}
