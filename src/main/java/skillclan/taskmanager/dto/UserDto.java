package skillclan.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;

public class UserDto {
    @Null(message = "Id must be null")
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message = "Incorrect email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Pattern(regexp = "^380(\\d){9}$",
             message = "Mobile number must contain 380 and 9 digits")
    private String phoneNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
