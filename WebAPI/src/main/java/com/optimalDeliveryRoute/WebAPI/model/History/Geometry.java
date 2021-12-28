package com.optimalDeliveryRoute.WebAPI.model.History;


import lombok.*;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Geometry {

    private List<List<Double>> coordinates;
    private String type;
}
