package com.optimalDeliveryRoute.WebAPI.Controller;

import com.optimalDeliveryRoute.WebAPI.Service.HistoryService;
import com.optimalDeliveryRoute.WebAPI.Service.UserService;
import com.optimalDeliveryRoute.WebAPI.Util.jwtUserSupport;
import com.optimalDeliveryRoute.WebAPI.model.History.Request.HistoryPathRequest;
import com.optimalDeliveryRoute.WebAPI.model.History.Response.HistoryPathResponse;
import com.optimalDeliveryRoute.WebAPI.model.History.Trip;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/history")
public class HistoryController {
    public HistoryController(UserService userService, com.optimalDeliveryRoute.WebAPI.Util.jwtUserSupport jwtUserSupport, HistoryService historyService) {
        this.userService = userService;
        this.jwtUserSupport = jwtUserSupport;
        this.historyService = historyService;
    }
    private HistoryService historyService;
    private  UserService userService;
    private  jwtUserSupport jwtUserSupport;

    @GetMapping("/get")
    public ResponseEntity<List<HistoryPathResponse>> GetHistoryPath(
            @Valid @RequestParam(name = "page", required = false, defaultValue = "1")@Min(1) Integer page,
            @Valid @RequestParam(name = "size", required = false, defaultValue = "20")@Min(1) Integer size,
           @Valid @RequestParam(name = "keyword", required= false, defaultValue = "") String keyword,
           @Valid @RequestParam(name = "sort", required= false, defaultValue = "") String sort,
            HttpServletRequest request){
        var username = jwtUserSupport.getUserNameInJwt(request);
        return ResponseEntity.ok(this.historyService.GetHistoryPaths(username,page,size,keyword,sort));

    }
//    @GetMapping("/get/trip")
//    public ResponseEntity<Trip> GetTrips(
//            @Valid @RequestParam(name = "path_id") long path_id,
//            HttpServletRequest request){
//        return ResponseEntity.ok(this.historyService.GetTrip(path_id));
//    }

    @PostMapping("/save")
    public ResponseEntity<Boolean> SaveHistoryPart(
             @RequestBody  HistoryPathRequest  historyPathRequest,
            HttpServletRequest request){
        var username = jwtUserSupport.getUserNameInJwt(request);
        var user =userService.GetUserByName(username);
        return ResponseEntity.ok(this.historyService.SaveHistoryPart(user, historyPathRequest));
    }

}
