package dadn_SmartFarm.service.interf;

import dadn_SmartFarm.dto.ScheduleDTO.ScheduleRequestDTO.CreateScheduleRequest;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.CreateScheduleResponse;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.DeleteScheduleResponse;
import dadn_SmartFarm.dto.ScheduleDTO.ScheduleResponse.GetScheduleResponse;

public interface IScheduleService {
    GetScheduleResponse GetListWork(int month, int year, long idRoom);

    CreateScheduleResponse CreateSchedule(CreateScheduleRequest request);

    DeleteScheduleResponse DeleteSchedule(long id);
}
