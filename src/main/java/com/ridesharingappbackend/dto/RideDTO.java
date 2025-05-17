package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {
    private int id;
    private Integer passengerId;
    private Integer driverId;
    private String pickupLocation;
    private String dropOffLocation;
    private double distance;
    private double estimatedFare;
    private RideStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private double fare;
    private LocalDateTime pickupTime;
    private Integer companyId;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public RideStatus getStatus() { return status; }
        public void setStatus(RideStatus status) { this.status=status; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropOffLocation() { return dropOffLocation; }
    public void setDropOffLocation(String dropOffLocation) { this.dropOffLocation = dropOffLocation; }

    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }

}
