package com.optimalDeliveryRoute.WebAPI.Service;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimalDeliveryRoute.WebAPI.Entity.GeometryEntity;
import com.optimalDeliveryRoute.WebAPI.Entity.HistoryPath;
import com.optimalDeliveryRoute.WebAPI.Entity.Location;
import com.optimalDeliveryRoute.WebAPI.Entity.User;

import com.optimalDeliveryRoute.WebAPI.Repository.HistoryRepository;
import com.optimalDeliveryRoute.WebAPI.model.History.Geometry;
import com.optimalDeliveryRoute.WebAPI.model.History.Request.HistoryPathRequest;
import com.optimalDeliveryRoute.WebAPI.model.History.Response.HistoryPathResponse;

import com.optimalDeliveryRoute.WebAPI.model.History.Trip;
import com.optimalDeliveryRoute.WebAPI.model.History.WayPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Repository
@Transactional
public class HistoryService {
    @Autowired
    public HistoryService(
            HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    private HistoryRepository historyRepository;

    public List<HistoryPathResponse> GetHistoryPaths(
            String username,
            Integer page,
            Integer size,
            String keyword,
            String sort) {
        if (removeUnicode(sort).equals("length")) {
            sort = "time";
        }
        return this.historyRepository.findAllHistoryPath(
                username, removeUnicode(keyword), size, (page - 1) * size, sort);
    }

//    public Trip GetTrip(long path_id) {
//        var GeometryEntity = this.historyRepository.GetGeometryByPathId(path_id);
//        var trip = this.historyRepository.GetTripByPathId(path_id);
//        var geometryEntity = this.historyRepository.GetGeometryByPathId(path_id);
//        var geometry = new Geometry();
//        geometry.setType(geometryEntity.getType());
//            Gson gson = new Gson();
//            List<List<Double>> coordinates = gson.fromJson(
//                    geometryEntity.getCoordinates(),new TypeToken<List<List<Double>>>(){}.getType());
//            geometry.setCoordinates(coordinates);
//            trip.setGeometry(geometry);
//        return trip;
//    }

    public boolean SaveHistoryPart(User user, HistoryPathRequest historyPathRequest) {
        var historypath = new HistoryPath();
        historypath.setUser_id(user.getId());
        historypath.setDuration(historyPathRequest.getTrips().get(0).getDuration());
        historypath.setDistance(historyPathRequest.getTrips().get(0).getDistance());
        historypath.setCreatedtime(LocalDateTime.now());
        this.historyRepository.saveHistoryPart(historypath);
        historyPathRequest.getWaypoints().forEach(waypoint -> {
            this.saveLocation(waypoint, historypath.getId());
        });
//        this.saveGeometry(historyPathRequest.getTrips().get(0).getGeometry(), historypath.getId());
        return true;
    }

    private void saveGeometry(Geometry geometry, long path_id) {
            var geometryEntity = new GeometryEntity();
            Gson gson = new Gson();
            String json = gson.toJson(geometry.getCoordinates());
            geometryEntity.setPath_id(path_id);
            geometryEntity.setCoordinates(json);
            geometryEntity.setType(geometry.getType());
            this.historyRepository.saveGeometry(geometryEntity);
    }

    private void saveLocation(WayPoint waypoint, long path_id) {
        var location = new Location();
        location.setIndexlocation(waypoint.getWaypoint_index());
        location.setPath_id(path_id);
        location.setLatitude(waypoint.getLocation().get(0));
        location.setLongitude(waypoint.getLocation().get(1));
        location.setNamelocation(waypoint.getName());
        if(location.getNamelocation().isEmpty()){
            location.setNamelocation(String.valueOf(List.of(location.getLatitude(),location.getLongitude())));
        }
        location.setNosignnamelocation(removeUnicode(waypoint.getName()));
        this.historyRepository.saveLocation(location);
    }

    public static String removeUnicode(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        str = str.toLowerCase().trim();
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = temp.replaceAll("đ", "d");
        return pattern.matcher(temp).replaceAll("").trim();
    }
}
