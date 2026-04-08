package com.vehiclerental.inspection;

import com.vehiclerental.inspection.dto.InspectionRequest;
import org.springframework.web.multipart.MultipartFile;

public interface VehicleInspectionService {
    VehicleInspection create(Long inspectorUserId,
                             InspectionRequest req,
                             MultipartFile front, MultipartFile rear,
                             MultipartFile left, MultipartFile right,
                             MultipartFile interior, MultipartFile dashboard);
}
