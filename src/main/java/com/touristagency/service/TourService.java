package com.touristagency.service;

import com.touristagency.dto.TourDTO;
import com.touristagency.entity.Order;
import com.touristagency.entity.Tour;
import com.touristagency.entity.enums.OrderStatus;
import com.touristagency.repository.TourRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class TourService {
    private final TourRepository tourRepo;

    public TourService(TourRepository tourRepo) {
        this.tourRepo = tourRepo;
    }

    public Page<Tour> findAllToursPageable(Pageable pageable) {
        return tourRepo.findAll(pageable);
    }

    public void createTour(TourDTO tourDTO) {
        Tour tour = Tour.builder()
                .name(tourDTO.getName())
                .type(tourDTO.getType())
                .price(tourDTO.getPrice())
                .groupSize(tourDTO.getGroupSize())
                .hotel(tourDTO.getHotel())
                .description(tourDTO.getDescription())
                .isHot(false)
                .build();
        tourRepo.save(tour);
        log.info("New tour " + tour);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateTour(long id, TourDTO tourDTO) {
        System.out.println(tourDTO.toString());
        Tour tour = getTourById(id);
        tour.setName(tourDTO.getName());
        tour.setType(tourDTO.getType());
        if (tour.getPrice() != tourDTO.getPrice()) {
            double newPrice = tour.getPrice();
            tour.setPrice(newPrice);
            tour.getOrders().stream()
                    .filter((x) -> x.getStatus() == OrderStatus.PENDING)
                    .forEach((x) -> {
                        x.setPrice(newPrice);
                        x.setFixedPrice(Order.fixPrice(newPrice, x.getDiscount()));
                    });
        }
        tour.setGroupSize(tourDTO.getGroupSize());
        tour.setHotel(tourDTO.getHotel());
        tour.setDescription(tourDTO.getDescription());
        tourRepo.save(tour);
        log.info("Update tour " + tour);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void makeHotTour(Long id) {
        Tour tour = getTourById(id);
        tour.switchHot();
        tourRepo.save(tour);
        log.info("Switch hot tour " + tour);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteTour(Long id) {
        Tour tour = getTourById(id);
        tourRepo.delete(tour);
        log.info("Delete tour " + tour);
    }

    public Tour getTourById(Long id) {
        return tourRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid tour id: " + id));
    }
}
