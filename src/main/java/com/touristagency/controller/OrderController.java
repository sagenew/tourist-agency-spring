package com.touristagency.controller;

import com.touristagency.dto.DiscountDTO;
import com.touristagency.dto.RegisterUserDTO;
import com.touristagency.dto.UpdateUserDTO;
import com.touristagency.entity.Discount;
import com.touristagency.entity.User;
import com.touristagency.entity.enums.Authority;
import com.touristagency.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Log4j2
public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/tours/orders/add/{id}")
    public String orderTour(@AuthenticationPrincipal User user,
                            @PathVariable("id") long tourId,
                            @RequestParam(value = "page", required = false) Long pageId,
                            @RequestParam(value = "size", required = false) Long size,
                            @RequestParam(value = "sort", required = false) String sort,
                            RedirectAttributes redirectAttributes) {
        orderService.orderTour(user.getId(), tourId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours";
    }

    @GetMapping("/tours/orders")
    public String getOrdersPage(Model model,
                                @PageableDefault(sort = {"id"},
                                        direction = Sort.Direction.DESC,
                                        size = 5) Pageable pageable) {
        if(!model.containsAttribute("discount")) {
            model.addAttribute("discount", orderService.getDiscount());
        }
        model.addAttribute("orders", orderService.findAllOrdersPageable(pageable));
        return "orders";
    }

    @PostMapping("/tours/orders/set-discount")
    public String setDiscount(@ModelAttribute("discount") @Valid DiscountDTO discountDTO,
                             BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("discount", discountDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.discount", bindingResult);
            return "redirect:/tours/orders";
        }
        orderService.setDiscount(discountDTO.getStep(), discountDTO.getThreshold());
        return "redirect:/tours/orders";
    }

    @GetMapping("/tours/orders/mark-paid/{id}")
    public String markOrderAsPaid(@PathVariable("id") long orderId,
                                  @RequestParam(value = "page", required = false) Long pageId,
                                  @RequestParam(value = "size", required = false) Long size,
                                  @RequestParam(value = "sort", required = false) String sort,
                                  RedirectAttributes redirectAttributes) {
        orderService.markOrderAsPaid(orderId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours/orders";
    }

    @GetMapping("/tours/orders/mark-denied/{id}")
    public String markOrderAsDenied(@PathVariable("id") long orderId,
                                    @RequestParam(value = "page", required = false) Long pageId,
                                    @RequestParam(value = "size", required = false) Long size,
                                    @RequestParam(value = "sort", required = false) String sort,
                                    RedirectAttributes redirectAttributes) {
        orderService.markOrderAsDenied(orderId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours/orders";
    }

    @GetMapping("/tours/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") long orderId,
                              @RequestParam(value = "page", required = false) Long pageId,
                              @RequestParam(value = "size", required = false) Long size,
                              @RequestParam(value = "sort", required = false) String sort,
                              RedirectAttributes redirectAttributes) {
        orderService.deleteOrder(orderId);
        redirectAttributes.addAttribute("page", pageId);
        redirectAttributes.addAttribute("size", size);
        redirectAttributes.addAttribute("sort", sort);
        return "redirect:/tours/orders";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return "error/404";
    }
}
