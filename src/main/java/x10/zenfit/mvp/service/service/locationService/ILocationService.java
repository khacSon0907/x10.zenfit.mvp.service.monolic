package x10.zenfit.mvp.service.service.locationService;

import x10.zenfit.mvp.service.service.locationService.service.DistrictResponse;
import x10.zenfit.mvp.service.service.locationService.service.ProvinceResponse;
import x10.zenfit.mvp.service.service.locationService.service.WardResponse;

import java.util.List;

public interface ILocationService {

        List<ProvinceResponse> getProvinces();

        List<DistrictResponse> getDistricts(String provinceCode);

        List<WardResponse> getWards(String districtCode);
}
