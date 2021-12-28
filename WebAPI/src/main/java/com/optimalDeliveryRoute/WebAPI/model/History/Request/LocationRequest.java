package com.optimalDeliveryRoute.WebAPI.model.History.Request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    @NotNull(message=" indexlocation cannot be missing or empty")
    @Min(value = 0,message = "value of lenthg must greater than or equal to 0")
    private int distance;
    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private double latitude;
    @NotNull
    @Min(value = -180)
    @Max(value = 1800)
    private double longitude;
    @NotNull(message ="namelocation cannot be missing or empty" )
    private String namelocation;
}
