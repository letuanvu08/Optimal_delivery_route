package com.optimalDeliveryRoute.WebAPI.model.History.Response;
import com.optimalDeliveryRoute.WebAPI.model.History.Request.LocationRequest;
import com.optimalDeliveryRoute.WebAPI.model.History.WayPoint;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPathResponse {
    private long id;
    private double duration;
    private double distance;
    private LocalDateTime createdtime;
    private List<String> locations;

}
