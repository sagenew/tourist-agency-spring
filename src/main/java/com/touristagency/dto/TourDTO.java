package com.touristagency.dto;

import com.touristagency.entity.enums.HotelType;
import com.touristagency.entity.enums.TourType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class TourDTO {
    @NotBlank(message = "{validation.tour.name.not_blank}")
    @Size(min = 5, max = 100, message = "{validation.tour.name.size}")
    private String name;
    @NotNull(message = "{validation.tour.type.not_null}")
    private TourType type;
    @Positive(message = "{validation.tour.price.positive}")
    private double price;
    @Positive(message = "{validation.tour.group_size.positive}")
    private int groupSize;
    @NotNull(message = "{validation.tour.hotel.not_null}")
    private HotelType hotel;
    @Size(max = 200, message = "{validation.tour.description.size}")
    private String description;
    private boolean isHot;
}
