package com.optimalDeliveryRoute.WebAPI.model.History;


import com.optimalDeliveryRoute.WebAPI.Entity.Location;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class WayPoint {

    private double distance;
    private List<Double> location;
    private long waypoint_index;
    private String name;
}
