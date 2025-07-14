package gift.controller.api;


import gift.common.aop.annotation.PreAuthorize;
import gift.common.model.CustomAuth;
import gift.dto.user.UserCreateRequest;
import gift.dto.user.UserAdminResponse;
import gift.dto.user.UserDefaultResponse;
import gift.dto.user.UserUpdateRequest;
import gift.entity.User;
import gift.common.model.CustomPage;
import gift.entity.UserRole;
import gift.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    @PreAuthorize(UserRole.ROLE_ADMIN)
    public ResponseEntity<CustomPage<UserAdminResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size
    ) {
        var pageResponse =  CustomPage.convert(
            userService.getBy(page, size), UserAdminResponse::from
        );
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_ADMIN)
    public ResponseEntity<UserAdminResponse> getUserById(
            @NotNull(message = "사용자 ID는 필수입니다.")
            @PathVariable Long id
    ) {
        var user = userService.getById(id);
        return new ResponseEntity<>(UserAdminResponse.from(user), HttpStatus.OK);
    }

    @GetMapping("/me")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<UserDefaultResponse> getCurrentUser(
            @RequestAttribute("auth")CustomAuth auth
        ) {
        var user = userService.getById(auth.userId());
        return new ResponseEntity<>(UserDefaultResponse.from(user), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize(UserRole.ROLE_ADMIN)
    public ResponseEntity<UserAdminResponse> createUser(
            @Valid  @RequestBody UserCreateRequest request
    ) {
        log.info("사용자 생성 요청: {}", request);


        var user = userService.create(request.toEntity());
        log.info("사용자 생성 완료: {}", user);
        return new ResponseEntity<>(UserAdminResponse.from(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_ADMIN)
    public ResponseEntity<UserAdminResponse> updateUser(
            @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        log.info("사용자 업데이트 요청: {}", request);
        User updateRequest = request.toEntity();
        updateRequest.setId(id);
        var user = userService.update(updateRequest);
        log.info("사용자 업데이트 완료: {}", user);
        return new ResponseEntity<>(UserAdminResponse.from(user), HttpStatus.OK);
    }

    @PutMapping("/me")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<UserDefaultResponse> updateCurrentUser(
            @RequestAttribute("auth") CustomAuth auth,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        log.info("현재 사용자 업데이트 요청: {}", request);
        User updateRequest = request.toEntity();
        updateRequest.setId(auth.userId());
        var user = userService.update(updateRequest);
        log.info("현재 사용자 업데이트 완료: {}", user);
        return new ResponseEntity<>(UserDefaultResponse.from(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(UserRole.ROLE_ADMIN)
    public ResponseEntity<Void> deleteUser(
            @PathVariable @NotNull(message = "사용자 ID는 필수입니다.") Long id
    ) {
        log.info("사용자 삭제 요청: ID={}", id);
        userService.deleteById(id);
        log.info("사용자 삭제 완료: ID={}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/me")
    @PreAuthorize(UserRole.ROLE_USER)
    public ResponseEntity<Void> deleteCurrentUser(
            @RequestAttribute("auth") CustomAuth auth
    ) {
        log.info("현재 사용자 삭제 요청: ID={}", auth.userId());
        userService.deleteById(auth.userId());
        log.info("현재 사용자 삭제 완료: ID={}", auth.userId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
