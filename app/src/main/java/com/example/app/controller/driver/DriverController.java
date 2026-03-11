package com.example.app.controller.driver;


import com.example.model.Role;
import com.example.model.TripStatus;
import com.example.model.UserStatus;
import com.example.model.entitiy.Car;
import com.example.model.entitiy.Trip;
import com.example.model.entitiy.User;
import com.example.repository.BrandRepository;
import com.example.repository.CarRepository;
import com.example.repository.RegionRepository;
import com.example.service.CarPhotoService;
import com.example.service.TripService;
import com.example.service.UserService;
import com.example.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DriverController {
    private  final UserService userService;
    private final TripService tripService;
    private final RegionRepository regionRepository;
    private final BrandRepository brandRepository;
    private final CarRepository carRepository;
    private final CarPhotoService carPhotoService;

    @Value("${app.upload.license-path}")
    private String licenseUploadPath;

    @GetMapping("driver/home")
    public String driverHome(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if(springUser != null){
            modelMap.addAttribute("user",springUser.getUser() );
        }
        return "driverPackage/driverHome";
    }

    @GetMapping("/register/driver")
    public String registerDriver(@RequestParam(required = false)String msg, ModelMap modelMap) {
        modelMap.addAttribute("msg",msg);
        modelMap.addAttribute("regions",regionRepository.findAll());
        modelMap.addAttribute("brands", brandRepository.findAll());
        return "driverPackage/registerDriver";
    }

    @PostMapping("/register/driver")
    public String registerDriver(@ModelAttribute User user,
                                 @RequestParam Integer regionId,
                                 @RequestParam String numberPlate,
                                 @RequestParam String carYear,
                                 @RequestParam Integer brandId,
                                 @RequestParam("licensePhoto") MultipartFile licensePhoto){
        if(userService.findByEmail(user.getEmail()).isPresent()){
            return "redirect:/register/driver?msg=Email already exists!";
        }

        if (!licensePhoto.isEmpty()) {
            try {
                Path dir = Paths.get(licenseUploadPath);
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                }
                String filename = UUID.randomUUID() + "_" + licensePhoto.getOriginalFilename();
                Files.copy(licensePhoto.getInputStream(), dir.resolve(filename));
                user.setDriverLicensePhoto(filename);
            } catch (IOException e) {
                return "redirect:/register/driver?msg=License photo upload failed!";
            }
        }

        user.setRole(Role.DRIVER);
        user.setBlocked(false);
        user.setRatingAverage(BigDecimal.ZERO);
        user.setStatus(UserStatus.ACCEPTED);
        user.setRegion(regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found!")));
        userService.save(user);

        Car car = new Car();
        car.setNumberPlate(numberPlate);
        car.setYear(LocalDate.parse(carYear + "-01-01"));
        car.setBrand(brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found!")));
        car.setDriver(user);
        carRepository.save(car);

        return "redirect:/loginPage?msg=Registration Successful! Please login and upload your car photos.";
    }

    @GetMapping("/driver/uploadCarPhotos")
    public String uploadCarPhotosPage(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        User user = springUser.getUser();
        Car car = carRepository.findByDriverId(user.getId())
                .orElseThrow(() -> new RuntimeException("Car not found!"));
        modelMap.addAttribute("car", car);
        return "carPackage/uploadCarPhotos";
    }

    @PostMapping("/driver/uploadCarPhotos")
    public String uploadCarPhotos(@AuthenticationPrincipal SpringUser springUser,
                                  @RequestParam("photos") List<MultipartFile> photos,
                                  @RequestParam(value = "mainPhotoIndex", defaultValue = "0") Integer mainPhotoIndex) {
        User user = springUser.getUser();
        Car car = carRepository.findByDriverId(user.getId())
                .orElseThrow(() -> new RuntimeException("Car not found!"));
        try {
            carPhotoService.savePhotos(car.getId(), photos, mainPhotoIndex);
        } catch (Exception e) {
            return "redirect:/driver/uploadCarPhotos?msg=Upload failed: " + e.getMessage();
        }
        return "redirect:/driver/home?msg=Photos uploaded! Waiting for admin approval.";
    }

    @GetMapping("/driver/trips")
    public String driverTrips(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        User user = springUser.getUser();
        modelMap.addAttribute("trips", tripService.findByDriver(user));
        return "driverPackage/driverTrips";
    }

    @GetMapping("/driver/addTrip")
    public String addTripPage(ModelMap modelMap) {
    modelMap.addAttribute("trip",new Trip());
        return "driverPackage/addTrip";
    }

    @PostMapping("/driver/addTrip")
    public String addTrip(@ModelAttribute Trip trip,
                          @AuthenticationPrincipal SpringUser springUser){

        User user = springUser.getUser();

        if (user.getStatus().equals(UserStatus.ACCEPTED)) {
        trip.setDriver(user);
        trip.setAvailableSeats(trip.getTotalSeats());
        trip.setStatus(TripStatus.UPCOMING);
        trip.setCreatedAt(LocalDateTime.now());
        tripService.save(trip);
        return "redirect:/driver/home?msg=Trip added successfully!";
        } else return "redirect:/driver/home?msg=Your account is not accepted yet!";
    }
}
