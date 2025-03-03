package com.mukesh.Splitwise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mukesh.Splitwise.model.Response;
import com.mukesh.Splitwise.service.OTPService;

@RestController
@RequestMapping("splitwise")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/otp/{mobileNumber}")
    public ResponseEntity generateOTP(@PathVariable(name ="mobileNumber" ) String mobileNumber) {
        Response response = otpService.generateOTP(mobileNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/otp/{mobileNumber}")
    public ResponseEntity getOTP(@PathVariable(name ="mobileNumber" ) String mobileNumber) throws Exception {
        Response response = otpService.getOTP(mobileNumber);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/otp")
    public ResponseEntity getOTPByStatusAndMobileNumber(@RequestParam(required = false) String mobileNumber,@RequestParam(required = false) Boolean isExpired) throws Exception {
        Response response = otpService.getOTPByStatusAndMobileNumber(mobileNumber,isExpired);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
