package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    UserRole toUserRole() {
        return UserRole.valueOf(name);
    }

    static Role fromUserRole(UserRole userRole) {
        return new Role(userRole.name());
    }
}
