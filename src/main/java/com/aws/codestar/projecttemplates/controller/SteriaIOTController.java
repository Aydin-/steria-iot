package com.aws.codestar.projecttemplates.controller;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;

/**
 * Basic Spring web service controller that handles all GET requests.
 */
@RestController
@RequestMapping("/")
public class SteriaIOTController {

    private static Date lastUpdate = Calendar.getInstance().getTime();
    private static boolean isTouched = false;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getIstouched(String test) {
        return ResponseEntity.ok(createResponse("" + isTouched));
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity setIsTouched(@RequestBody Map<String, Object> payload) throws ParseException {
        System.out.println(payload.get("timestamp"));
        String timestamp = "" + payload.get("timestamp");

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        Date date = format.parse(timestamp);

        if (date.after(lastUpdate)) {
            isTouched = true;

            System.out.println("IS TOUCHED AND " + date + " is after " + lastUpdate);

            lastUpdate = date;
        } else {
            System.out.println("Ignored event because event timestamp " + date + " is before last update time: " + lastUpdate);
        }

        return ResponseEntity.ok(createResponse("true"));
    }

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(sequence -> "" + isTouched)
                .doOnNext(a -> isTouched = false);
    }

    @PostMapping(path = "/temp1", produces = "application/json")
    public ResponseEntity setTemp1(@RequestBody Map<String, Object> payload) {
        System.out.println("got temp POST");
        System.out.println(payload);
        System.out.println(payload.get("temperature"));
        
        return ResponseEntity.ok(createResponse("" + isTouched));
    }

    private String createResponse(String name) {
        return new JSONObject().put("touched", name).toString();
    }
}