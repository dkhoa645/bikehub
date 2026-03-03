package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.InspectorCreationRequest;
import com.group3.bikehub.dto.request.UserCreationRequest;
import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUser(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @PostMapping("/create-inspector")
    ApiResponse<UserResponse> createInspector(@RequestBody InspectorCreationRequest inspectorCreationRequest){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createInspector(inspectorCreationRequest))
                .build();
    }

    @PostMapping()
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest userCreationRequest){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .message("Deleted User")
                .build();
    }

}
