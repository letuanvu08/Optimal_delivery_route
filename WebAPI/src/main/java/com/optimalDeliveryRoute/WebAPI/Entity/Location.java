package com.optimalDeliveryRoute.WebAPI.Entity;


import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location implements Serializable {
    private long indexlocation;
    private  long path_id;
    private double latitude;
    private double longitude;
    private String  nosignnamelocation;
    private String namelocation;
    private double distance;
}
