package es.museotrapo.trapo.security.jwt;

import es.museotrapo.trapo.dto.UserDTO;

public class RegisterRequest {

    private UserDTO userDTO;
    private String password;

    public RegisterRequest() {
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

