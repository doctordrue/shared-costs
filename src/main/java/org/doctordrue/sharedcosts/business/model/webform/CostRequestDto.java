package org.doctordrue.sharedcosts.business.model.webform;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CostRequestDto implements Serializable {
    private Long id;
    private String name;
    private Double total;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datetime;
    private Long groupId;
    private Long currencyId;
}
