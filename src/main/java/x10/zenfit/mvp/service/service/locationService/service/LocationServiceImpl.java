package x10.zenfit.mvp.service.service.locationService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import x10.zenfit.mvp.service.config.location.LocationProperties;
import x10.zenfit.mvp.service.service.locationService.ILocationService;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements ILocationService {

    private final RestTemplate restTemplate;

    private final LocationProperties locationProperties;

    @Override
    public List<ProvinceResponse> getProvinces() {

        String url = locationProperties.getBaseUrl() + "/p/";

        ResponseEntity<ProvinceResponse[]> response =
                restTemplate.getForEntity(
                        url,
                        ProvinceResponse[].class
                );

        return Arrays.asList(response.getBody());
    }

    @Override
    public List<DistrictResponse> getDistricts(String provinceCode) {

        String url =
                locationProperties.getBaseUrl()
                        + "/p/"
                        + provinceCode
                        + "?depth=2";

        ProvinceDetailResponse response =
                restTemplate.getForObject(
                        url,
                        ProvinceDetailResponse.class
                );

        return response.getDistricts();
    }

    @Override
    public List<WardResponse> getWards(String districtCode) {

        String url =
                locationProperties.getBaseUrl()
                        + "/d/"
                        + districtCode
                        + "?depth=2";

        DistrictDetailResponse response =
                restTemplate.getForObject(
                        url,
                        DistrictDetailResponse.class
                );

        return response.getWards();
    }
}