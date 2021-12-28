package com.optimalDeliveryRoute.WebAPI.Repository;

import com.optimalDeliveryRoute.WebAPI.Entity.GeometryEntity;
import com.optimalDeliveryRoute.WebAPI.Entity.HistoryPath;
import com.optimalDeliveryRoute.WebAPI.Entity.Location;
import com.optimalDeliveryRoute.WebAPI.model.History.Response.HistoryPathResponse;
import com.optimalDeliveryRoute.WebAPI.model.History.Trip;
import com.optimalDeliveryRoute.WebAPI.model.History.WayPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Repository
public class HistoryRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static  final String HistoryPathTableName ="HistoryPath";
    private static  final String HistoryGeometryTableName = "Geometry";
    private static final String SQL_GetHistoryPathMathchingOrderByTime= "Call GetHistoryPathMathchingOrderByTime(:username,:keyword,:size,:offset)";
    private static final String SQL_GetHistoryPathMathchingOrderByLength= "Call GetHistoryPathMathchingOrderByLength(:username,:keyword,:size,:offset)";
    private static final String SQL_GET_LOCATIONBYIDPATH ="Call GetListLocations(:id)";
    private static final String SQL_GET_LOCATIONSNAME ="SELECT Location.namelocation from Location where Location.path_id= :path_id\n" +
            "order by Location.indexlocation;";
    private static final String Insert_History_Path= "Insert into HistoryPath(id,User_Id,duration,distance, createdTime) VALUES(:id,:User_Id,:duration,:distance,:createdTime)";
    private static final String Insert_Location="insert into Location(indexlocation,path_id,nosignnamelocation,namelocation,latitude,longitude,distance) " +
                                                        " VALUES (:indexlocation,:path_id,:nosignnamelocation,:namelocation,:latitude,:longitude,:distance);";
    private static final String SQL_GetAUTO_IncrementPath ="SELECT `AUTO_INCREMENT`\n" +
            "FROM  INFORMATION_SCHEMA.TABLES\n" +
            "WHERE TABLE_SCHEMA = 'sql6427351'\n" +
            "AND   TABLE_NAME   = :name";
    private static final String Insert_Geometry = "insert into Geometry( path_id, coordinates, type) VALUES (:path_id,:coordinates,:type);";
    private static final String SQL_GET_GEOMETRY = "Select path_id,coordinates,type from Geometry WHERE path_id=:path_id;";
    private static final String SQL_GET_Trip_Duration_Distance = "Select duration, distance from HistoryPath WHERE id =:path_id;";
    public List<HistoryPathResponse> findAllHistoryPath(String username, String keyword, int size, int offset, String sort ){
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("username",username);
        mapSqlParameterSource.addValue("keyword",keyword);
        mapSqlParameterSource.addValue("size",size);
        mapSqlParameterSource.addValue("offset",offset);
        var sql=SQL_GetHistoryPathMathchingOrderByTime;
        if(sort.equals("length")){
            sql=SQL_GetHistoryPathMathchingOrderByLength;
        }
        return namedParameterJdbcTemplate.query(sql,
                mapSqlParameterSource,
                (rs, rowNum)->new HistoryPathResponse(
                        rs.getInt("id"),
                        rs.getDouble("duration"),
                        rs.getDouble("distance"),
                        rs.getTimestamp("createdtime").toLocalDateTime(),
                        this.GetLocationName(rs.getInt("id"))
                ));
    }


    public List<WayPoint> GetLocationByPathId(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id",id);
        return namedParameterJdbcTemplate.query(SQL_GET_LOCATIONBYIDPATH,
                mapSqlParameterSource,
                (rs, rowNum)->new WayPoint(rs.getDouble("distance"),
                        List.of(rs.getDouble("latitude"),
                        rs.getDouble("longitude")),
                        rs.getInt("indexlocation"),
                         rs.getString("namelocation")));
    }
    public List<String > GetLocationName(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("path_id",id);
        return namedParameterJdbcTemplate.query(SQL_GET_LOCATIONSNAME,
                mapSqlParameterSource,
                (rs, rowNum)-> rs.getString("namelocation"));
    }


    public void saveHistoryPart(HistoryPath historyPath) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("User_Id",historyPath.getUser_id());
        mapSqlParameterSource.addValue("duration",historyPath.getDuration());
        mapSqlParameterSource.addValue("distance",historyPath.getDistance());
        mapSqlParameterSource.addValue("createdTime",historyPath.getCreatedtime());
        var id =this.GetAUTO_IncrementPath(HistoryPathTableName);
        mapSqlParameterSource.addValue("id",id);
        historyPath.setId(id);
        this.namedParameterJdbcTemplate.update(Insert_History_Path,mapSqlParameterSource);
    }
    public long GetAUTO_IncrementPath(String nameTable){
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name",nameTable);
    return this.namedParameterJdbcTemplate.queryForObject(SQL_GetAUTO_IncrementPath,mapSqlParameterSource,Integer.class);
    }
    public void saveLocation(Location location){
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("indexlocation",location.getIndexlocation());
        mapSqlParameterSource.addValue("path_id",location.getPath_id());
        mapSqlParameterSource.addValue("distance",location.getDistance());
        mapSqlParameterSource.addValue("nosignnamelocation",location.getNosignnamelocation());
        mapSqlParameterSource.addValue("namelocation",location.getNamelocation());
        mapSqlParameterSource.addValue("latitude",location.getLatitude());
        mapSqlParameterSource.addValue("longitude",location.getLongitude());
        this.namedParameterJdbcTemplate.update(Insert_Location,mapSqlParameterSource);
    }
    public void saveGeometry(GeometryEntity geometry){
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("path_id",geometry.getPath_id());
        mapSqlParameterSource.addValue("type",geometry.getType());
        mapSqlParameterSource.addValue("coordinates",geometry.getCoordinates());

        this.namedParameterJdbcTemplate.update(Insert_Geometry,mapSqlParameterSource);
    }
    public GeometryEntity GetGeometryByPathId(long path_id){
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("path_id",path_id);
        return namedParameterJdbcTemplate.queryForObject(SQL_GET_GEOMETRY,
                mapSqlParameterSource,
                (rs, rowNum)-> new GeometryEntity(rs.getInt("path_id"),
                        rs.getString("coordinates"),
                        rs.getString("type")));
    }


    public Trip GetTripByPathId(long path_id){
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("path_id",path_id);
        return namedParameterJdbcTemplate.queryForObject(SQL_GET_Trip_Duration_Distance,
                mapSqlParameterSource,
                (rs,rowNum)-> new Trip(rs.getDouble("duration"),
                                        rs.getDouble("distance")));
    }
}
