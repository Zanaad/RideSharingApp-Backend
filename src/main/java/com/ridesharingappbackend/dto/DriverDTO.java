package com.ridesharingappbackend.dto;

import com.ridesharingappbackend.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {

    private Integer id;
    private Integer userId;
    private float rating;
    private DriverStatus status;
    private Integer companyId;

  /*  public DriverDTO(Integer id, Integer userId, float rating, DriverStatus status, Integer companyId) {
        this.id = id;
        this.userId = userId;
        this.rating = rating;
        this.status=status;
        this.companyId=companyId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) { this.status = status; }


    public float getCompanyId() { return companyId; }
    public void setCompanyId(Integer companyId) { this.companyId = companyId; }
*/

}
