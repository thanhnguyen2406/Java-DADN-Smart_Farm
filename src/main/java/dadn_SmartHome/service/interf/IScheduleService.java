package dadn_SmartHome.service.interf;

import dadn_SmartHome.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.CreateScheduleResponse;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.DeleteScheduleResponse;
import dadn_SmartHome.dto.ScheduleDTO.ScheduleResponse.GetScheduleResponse;

public interface IScheduleService {
    GetScheduleResponse GetListWork(int month, int year, long idRoom);

    CreateScheduleResponse CreateSchedule(CreateScheduleRequest request);

    DeleteScheduleResponse DeleteSchedule(long id);
}
