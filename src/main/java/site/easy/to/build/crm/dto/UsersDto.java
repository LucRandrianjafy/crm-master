package site.easy.to.build.crm.dto;

public class UsersDto {

    private Integer id;
    private String email;
    private String password;
    private String username;
    private String status;
    private String token;
    private Boolean isPasswordSet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsPasswordSet() {
        return isPasswordSet;
    }

    public void setIsPasswordSet(Boolean isPasswordSet) {
        this.isPasswordSet = isPasswordSet;
    }
}
