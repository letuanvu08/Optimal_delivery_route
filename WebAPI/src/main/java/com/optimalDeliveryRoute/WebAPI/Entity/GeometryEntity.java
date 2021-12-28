package com.optimalDeliveryRoute.WebAPI.Entity;

import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@Data
@AllArgsConstructor
public class GeometryEntity {
    private long path_id;
    private String coordinates;
    private String type;
}
