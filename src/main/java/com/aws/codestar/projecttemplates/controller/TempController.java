package com.aws.codestar.projecttemplates.controller;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Basic Spring web service controller that handles all GET requests.
 */
@RestController
@RequestMapping("/temp")
public class TempController {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity setIsTouched(@RequestBody Map<String, Object> payload) throws ParseException {
        System.out.println(payload);
        System.out.println(payload.get("value"));

        return ResponseEntity.ok(createResponse("true"));
    }
    private String createResponse(String name) {
        return new JSONObject().put("touched", name).toString();
    }
}