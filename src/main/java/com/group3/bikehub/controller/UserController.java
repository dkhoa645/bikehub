package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.InspectorCreationRequest;
import com.group3.bikehub.dto.request.PasswordChangeRequest;
import com.group3.bikehub.dto.request.UserCreationRequest;
import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<UserResponse>> getAllUser(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @PostMapping("/create-inspector")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> createInspector(
            @RequestBody @Valid InspectorCreationRequest inspectorCreationRequest){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createInspector(inspectorCreationRequest))
                .build();
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest userCreationRequest){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .message("Deleted User")
                .build();
    }

    @PutMapping("/password")
    ApiResponse<Void> changePassword(@RequestBody @Valid PasswordChangeRequest passwordChangeRequest){
        userService.changePass(passwordChangeRequest);
        return ApiResponse.<Void>builder()
                .message("Changed Password Successfully!")
                .build();
    }

}
