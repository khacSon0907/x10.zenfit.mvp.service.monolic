package x10.zenfit.mvp.service.service.locationService.service;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceDetailResponse {

    private Integer code;

    private String name;

    private List<DistrictResponse> districts;
}