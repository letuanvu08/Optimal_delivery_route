package com.optimalDeliveryRoute.WebAPI.model.History;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

//    private Geometry geometry;
    private double duration;
    private double distance;

}
