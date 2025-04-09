package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.DataInfo.DataDTO;
import dadn_SmartFarm.dto.Response;

public interface IDataInfoService {
    Response getAllData (String url);
    Response getLatestData (String url);
    Response sendData (DataDTO dataDTO);
}
