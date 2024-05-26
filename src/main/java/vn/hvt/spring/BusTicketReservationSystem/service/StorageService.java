package vn.hvt.spring.BusTicketReservationSystem.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    void store(MultipartFile file) throws IOException;
}
