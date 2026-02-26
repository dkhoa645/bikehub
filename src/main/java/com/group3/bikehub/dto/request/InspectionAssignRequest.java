package com.group3.bikehub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InspectionAssignRequest {
    UUID inspectionId;
    UUID inspectorId;
}
