package edu.MD.Fx_final.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookOrderDTO {
    private String orderId;
    private String userNic;
    private String userName;
    private String bookTitle;
    private String bookAuthor;
    private String orderedAt;
    private String status;
    private long createdAtMillis;
}
