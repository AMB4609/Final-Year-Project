package com.lambdacode.creditscoreanalysiswebapplication.Controller;
import com.lambdacode.creditscoreanalysiswebapplication.Request.EditRequest;

import com.lambdacode.creditscoreanalysiswebapplication.Request.RoleChangeRequest;
import com.lambdacode.creditscoreanalysiswebapplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @DeleteMapping("/deleteUser/{userEmail}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Object> deleteUser(
            @PathVariable String userEmail,
            @RequestParam(name = "performedByEmail") String performedByEmail) {

        Object deletedUser = userService.deleteUser(userEmail, performedByEmail);
        return ResponseEntity.ok(deletedUser);
    }

    @GetMapping("/getUserById/{userEmail}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('STAFF')")
    public ResponseEntity<Object> getUserById(@PathVariable String userEmail) {
        Object user = userService.getUserByEmail(userEmail); // Pass userEmail directly to the service
        return  ResponseEntity.ok(user);
    }
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')or hasRole('STAFF')")
    public ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Object users = userService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAllStaff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllStaff(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Object staff = userService.getAllStaff(page, size);
        return ResponseEntity.ok(staff);
    }

    @PostMapping(value = "/editUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')or hasRole('STAFF')")
    public ResponseEntity<Object> editUser(@ModelAttribute EditRequest editRequest) throws IOException {
        Object updatedUser = userService.editUser(editRequest);
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/changeUserRole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> changeUserRole(@RequestBody RoleChangeRequest roleChangeRequest) throws IOException {
        Object response = userService.changeUserRole(roleChangeRequest.getUserEmail(), roleChangeRequest.getNewRole());
        return ResponseEntity.ok(response);
    }

}
