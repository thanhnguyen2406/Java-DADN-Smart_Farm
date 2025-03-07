package dadn_SmartHome.service.interf;

import dadn_SmartHome.dto.DataInfo.DataDTO;
import dadn_SmartHome.dto.Response;
import dadn_SmartHome.model.DataInfo;

import java.util.List;

public interface IDataInfoService {
    Response getAllData (String url);
    Response getLatestData (String url);
    Response sendData (DataDTO dataDTO);
}
