package com.touristagency.controller;

import com.touristagency.dto.TourDTO;
import com.touristagency.entity.Tour;
import com.touristagency.entity.enums.HotelType;
import com.touristagency.entity.enums.TourType;
import com.touristagency.service.OrderService;
import com.touristagency.service.TourService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Log4j2
public class TourController {
    private final OrderService orderService;
    private final TourService tourService;

    public TourController(OrderService orderService, TourService tourService) {
        this.orderService = orderService;
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public String getToursPage(Model model,
                               @PageableDefault(sort = {"isHot"},
                                       direction = Sort.Direction.DESC,
                                       size = 5) Pageable pageable) {
        model.addAttribute("tours", tourService.findAllToursPageable(pageable));
        return "tours";
    }

    @GetMapping("/tours/add")
    public String getAddTourPage(Model model,
                                     @ModelAttribute("tour")
                                             TourDTO tourDTO) {
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        return "tour-add";
    }

    @PostMapping("/tours/add")
    public String addTour(@ModelAttribute("tour")
                                  @Valid TourDTO tourDTO,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tourTypes", TourType.values());
            model.addAttribute("hotelTypes", HotelType.values());
            return "tour-add";
        }
        tourService.createTour(tourDTO);
        return "redirect:/tours";
    }

    @GetMapping("/tours/update/{id}")
    public String getTourUpdatePage(@PathVariable("id") long id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        return "tour-update";
    }

    @PostMapping("/tours/update/{id}")
    public String updateTour(@PathVariable("id") long id,
                             @ModelAttribute("tour") @Valid TourDTO tourDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tourTypes", TourType.values());
            model.addAttribute("hotelTypes", HotelType.values());
            return "tour-update";
        }
        tourService.updateTour(id, tourDTO);
        return "redirect:/tours";
    }

    @GetMapping("/tours/delete/{id}")
    public String deleteTour(@PathVariable("id") long id,
                             @RequestParam(value = "page", required = false) Long pageId,
                             @RequestParam(value = "size", required = false) Long size,
                             @RequestParam(value = "sort", required = false) String sort,
                             RedirectAttributes redirectAttributes) {
        tourService.deleteTour(id);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours";
    }

    @GetMapping("/tours/make-hot/{id}")
    public String makeHotTour(@PathVariable("id") long id,
                              @RequestParam(value = "page", required = false) Long pageId,
                              @RequestParam(value = "size", required = false) Long size,
                              @RequestParam(value = "sort", required = false) String sort,
                              RedirectAttributes redirectAttributes) {
        tourService.makeHotTour(id);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "error/404";
    }
}
