package framework.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Geolocation {
        private String lat;
        @JsonProperty("long")  // Since 'long' is a reserved word in Java
        private String longitude;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private Geolocation geolocation;
        private String city;
        private String street;
        private int number;
        private String zipcode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Name {
        private String firstname;
        private String lastname;
    }

    private Address address;
    private int id;
    private String email;
    private String username;
    private String password;
    private Name name;
    private String phone;
    @JsonProperty("__v")
    private int version;

    // Factory method for creating test data
    public static UserModel createTestUser() {
        return UserModel.builder()
                .id(5)
                .username("derek")
                .email("derek@gmail.com")
                .password("jklg*_56")
                .phone("1-956-001-1945")
                .name(Name.builder()
                        .firstname("derek")
                        .lastname("powell")
                        .build())
                .address(Address.builder()
                        .city("san Antonio")
                        .street("adams St")
                        .number(245)
                        .zipcode("80796-1234")
                        .geolocation(Geolocation.builder()
                                .lat("40.3467")
                                .longitude("-40.1310")
                                .build())
                        .build())
                .version(0)
                .build();
    }
}