package gift.entity;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class User extends AbstractEntity {
    private Long id;
    private String email;
    private String password;
    private Set<UserRole> roles;

    public User(Long id, String email, String password) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = null;
    }

    public User(Long id, String email, String password, Set<UserRole> roles) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(Long id, String email, String password, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = null;
    }

    public User(Long id, String email, String password, Set<UserRole> roles, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", updatedAt=" + getUpdatedAt() +
                ", createdAt=" + getCreatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }
}