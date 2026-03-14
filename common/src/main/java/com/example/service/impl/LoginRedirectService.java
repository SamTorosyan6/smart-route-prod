package com.example.service.impl;


import com.example.model.UserStatus;
import com.example.repository.CarPhotoRepository;
import com.example.repository.CarRepository;
import com.example.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginRedirectService {
    private final CarRepository carRepository;
    private  final CarPhotoRepository carPhotoRepository;

    public String getRedirectUrl(SpringUser springUser) {
        if (springUser == null || springUser.getUser().getStatus() != UserStatus.ACCEPTED) {
            return "redirect:/login?error=account_not_active";
        }

         switch (springUser.getUser().getRole()) {
             case ADMIN:
                 return "redirect:/admin/home";
             case PASSENGER:
                 return "redirect:/passenger/home";
             case DRIVER:
                 return resolveDriverRedirect(springUser.getUser().getId());
             default:
                 return "redirect:/";
        }
    }

    private String resolveDriverRedirect(int driverId) {
        return carRepository.findByDriverId(driverId)
                    .map(car -> {
                        boolean hasPhotos = !carPhotoRepository.findByCarId(car.getId()).isEmpty();
                        if (!hasPhotos) {
                            return "redirect:/driver/uploadCarPhotos";
                        }
                        return "redirect:/driver/home";
                    })
                    .orElse("redirect:/driver/uploadCarPhotos");

    }
}
