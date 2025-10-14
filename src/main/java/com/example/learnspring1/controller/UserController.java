package com.example.learnspring1.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.learnspring1.domain.User;
import com.example.learnspring1.service.UserService;
import com.example.learnspring1.domain.APIResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Quản lý người dùng")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }


    @Operation(summary = "Tạo mới user", description = "Tạo mới một user với thông tin hợp lệ.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tạo user thành công",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ",
            content = @Content(schema = @Schema(implementation = APIResponse.class)))
    })
    @PostMapping
    public User createNewUser(@Valid @RequestBody User input) {
        User user = this.userService.createUser(input, encoder);
        return user;
    }


    @Operation(summary = "Lấy tất cả user", description = "Trả về danh sách tất cả user trong hệ thống.")
    @ApiResponse(responseCode = "200", description = "Thành công",
        content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @Operation(summary = "Lấy user phân trang", description = "Trả về danh sách user theo phân trang.")
    @ApiResponse(responseCode = "200", description = "Thành công",
        content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping("/page")
    public Page<User> getUsersPage(
            @Parameter(description = "Trang hiện tại", example = "1") @RequestParam(name = "page", defaultValue = "1") int page,
            @Parameter(description = "Số lượng mỗi trang", example = "10") @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userService.getUsersPage(pageable);
    }


    @Operation(summary = "Lấy user theo ID", description = "Trả về thông tin user theo id.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thành công",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy user",
            content = @Content(schema = @Schema(implementation = APIResponse.class)))
    })
    @GetMapping("/{id}")
    public User getUserById(@Parameter(description = "ID của user", example = "1") @PathVariable("id") Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }


    @Operation(summary = "Tìm user theo tên", description = "Tìm kiếm user theo tên gần đúng (không phân biệt hoa thường).")
    @ApiResponse(responseCode = "200", description = "Thành công",
        content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping("/search")
    public List<User> getUsersByName(@Parameter(description = "Tên user muốn tìm", example = "giang") @RequestParam("name") String name) {
        return userService.getUsersByName(name);
    }


    @Operation(summary = "Cập nhật user", description = "Cập nhật thông tin user theo id.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy user",
            content = @Content(schema = @Schema(implementation = APIResponse.class)))
    })
    @PutMapping("/{id}")
    public User updateUser(
            @Parameter(description = "ID của user", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody User input) {
        return userService.updateUser(id, input);
    }


    @Operation(summary = "Xóa user", description = "Xóa user theo id.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy user",
            content = @Content(schema = @Schema(implementation = APIResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID của user", example = "1") @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Đổi mật khẩu", description = "Đổi mật khẩu cho user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy user",
            content = @Content(schema = @Schema(implementation = APIResponse.class)))
    })
    @PatchMapping("/{id}/change-password")
    public User changePassword(
            @Parameter(description = "ID của user", example = "1") @PathVariable("id") Long id,
            @RequestBody PasswordChangeRequest request) {
        return userService.changePassword(id, request.getNewPassword(), encoder);
    }

    // DTO for password change request
    public static class PasswordChangeRequest {
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
