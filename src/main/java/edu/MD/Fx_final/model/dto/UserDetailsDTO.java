package edu.MD.Fx_final.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private String NIC;
    private String name;
    private LocalDate dob;
    private String email;
    private String phoneNumber;
    private String address;
}
