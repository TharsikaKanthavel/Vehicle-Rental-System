package com.vehiclerental.inspection;

import com.vehiclerental.inspection.dto.InspectionRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VehicleInspectionServiceImpl implements VehicleInspectionService {

    private final VehicleInspectionRepository repo;
    private final FileStorageService storage;

    @Override
    @Transactional
    public VehicleInspection create(Long inspectorUserId, InspectionRequest r,
                                    MultipartFile front, MultipartFile rear,
                                    MultipartFile left, MultipartFile right,
                                    MultipartFile interior, MultipartFile dashboard) {

        if (repo.findByDeliveryId(r.deliveryId).isPresent())
            throw new ValidationException("An inspection for this delivery already exists.");

        // TODO (optional, non-breaking): verify the delivery exists and is COMPLETED via DeliveryRepository

        VehicleInspection vi = new VehicleInspection();
        vi.setDeliveryId(r.deliveryId);
        vi.setVehicleId(r.vehicleId);
        vi.setInspectedByUserId(inspectorUserId);
        vi.setOdometer(r.odometer);
        vi.setFuelLevel(r.fuelLevel);
        vi.setCleanliness(r.cleanliness);
        vi.setNotes(r.notes);

        try {
            vi.setPhotoFrontPath(storage.save(front, r.deliveryId, "front"));
            vi.setPhotoRearPath(storage.save(rear, r.deliveryId, "rear"));
            vi.setPhotoLeftPath(storage.save(left, r.deliveryId, "left"));
            vi.setPhotoRightPath(storage.save(right, r.deliveryId, "right"));
            vi.setPhotoInteriorPath(storage.save(interior, r.deliveryId, "interior"));
            vi.setPhotoDashboardPath(storage.save(dashboard, r.deliveryId, "dashboard"));
        } catch (Exception ex) {
            throw new ValidationException(ex.getMessage());
        }

        // TODO (optional): update Vehicle mileage in your VehicleService (monotonic check)
        return repo.save(vi);
    }
}
