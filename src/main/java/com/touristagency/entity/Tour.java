package com.touristagency.entity;

import com.touristagency.entity.enums.HotelType;
import com.touristagency.entity.enums.TourType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"orders"})
@EqualsAndHashCode(exclude = {"orders"})
@Entity
@Table(name = "tours")
public class Tour {
    @Id
    @SequenceGenerator(name = "tourIdSeq", sequenceName = "tours_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tourIdSeq")
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private TourType type;

    @Column
    private double price;

    @Column
    private int groupSize;

    @Enumerated(value = EnumType.STRING)
    private HotelType hotel;

    @Column(length = 500)
    private String description;

    @Column
    private boolean isHot;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Order> orders;

    public void switchHot() {
        isHot = !isHot();
    }
}
