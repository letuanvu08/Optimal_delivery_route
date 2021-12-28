package com.optimalDeliveryRoute.WebAPI.model.History.Request;

import com.optimalDeliveryRoute.WebAPI.model.History.Trip;
import com.optimalDeliveryRoute.WebAPI.model.History.WayPoint;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPathRequest {

    @NotEmpty(message="The field waypoint cannot be missing or empty ")
    private List<WayPoint> waypoints;
    @NotEmpty(message="The field waypoint cannot be missing or empty ")
    private List<Trip> trips;


}