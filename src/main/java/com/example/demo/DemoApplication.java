package com.example.demo;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;
import com.example.demo.apartments.model.ApartmentEntity;
import com.example.demo.apartments.service.ApartmentService;
import com.example.demo.geolocations.model.GeolocationEntity;
import com.example.demo.geolocations.service.GeolocationService;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.model.UserRole;
import com.example.demo.users.service.UserService;
import com.example.demo.apartments.api.PropertyStatus;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    private final TypeService typeService;
    private final ApartmentService apartmentService;
    private final GeolocationService geolocationService;
    private final UserService userService;

    public DemoApplication(TypeService typeService, ApartmentService apartmentService,
            GeolocationService geolocationService, UserService userService) {
        this.typeService = typeService;
        this.apartmentService = apartmentService;
        this.geolocationService = geolocationService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && Objects.equals("--populate", args[0])) {
            log.info("Create default types values");
            final var type1 = typeService.create(new TypeEntity("Однокомнатная"));
            final var type2 = typeService.create(new TypeEntity("Двухкомнатная"));
            final var type3 = typeService.create(new TypeEntity("Трехкомнатная"));

            log.info("Create default geolocations values");
            final var geolocation1 = geolocationService.create(new GeolocationEntity("Ульяновск"));
            final var geolocation2 = geolocationService.create(new GeolocationEntity("Москва"));
            final var geolocation3 = geolocationService.create(new GeolocationEntity("Санкт-Петербург"));

            log.info("Create default items values");
            apartmentService.create(new ApartmentEntity(type1, PropertyStatus.SALE, false, 122423.00,
                    "sasas", "опр", geolocation1, true, 3));
            apartmentService.create(new ApartmentEntity(type2, PropertyStatus.PURCHASE, true, 122423.00,
                    "dfdsfds sdf", "опр", geolocation2, true, 3));
            apartmentService.create(new ApartmentEntity(type3, PropertyStatus.SALE, true, 122423.00,
                    "dfdsfds sdf", "опр", geolocation3, true, 3));

            log.info("Create default users");
            final var user1 = userService.create(new UserEntity("login", "pass"));
            userService.updateUserRole(user1, UserRole.ADMIN);
            final var user2 = userService.create(new UserEntity("user1", "password"));
        }
    }
}
