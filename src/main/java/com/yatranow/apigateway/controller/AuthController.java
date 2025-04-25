package com.yatranow.apigateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yatranow.apigateway.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Generate a JWT token", description = "Generates a JWT token for the provided mobile number.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token generated successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "{\"token\": \"<JWT_TOKEN>\"}"))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(example = "{\"error\": \"Mobile number is required\"}")))
    })
    @PostMapping("/generate/token")
    public ResponseEntity<?> generateToken(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Request body containing the mobile number", required = true,
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"mobile\": \"1234567890\"}")))
        @RequestBody Map<String, String> request) {
        try {
            if (request.get("mobile") == null || request.get("mobile").isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Mobile number is required"));
            }
            String token = jwtUtil.generateToken(request.get("mobile"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
