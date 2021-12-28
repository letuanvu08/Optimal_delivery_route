package com.optimalDeliveryRoute.WebAPI.Entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class HistoryPath implements Serializable {

    private long id;
    private long user_id;
    private double duration;
    private double distance;
    private LocalDateTime createdtime;
}
