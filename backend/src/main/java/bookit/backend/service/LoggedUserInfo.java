package bookit.backend.service;

import bookit.backend.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserInfo {
    private Long id;
    private UserRole role;

    public boolean isNotAdmin() {
        return role != UserRole.ADMIN;
    }
    public boolean isNotBusinessOwner() {
        return role != UserRole.BUSINESS_OWNER;
    }
    public boolean isWorker() {
        return role == UserRole.WORKER;
    }
    public boolean isClient() {
        return role == UserRole.CLIENT;
    }
}
