package delivery.models.mapper;

import delivery.models.Appointment;
import delivery.models.dto.AppointmentDto;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {


    public AppointmentDto mapToDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .clinicId(appointment.getClinic().getId())
                .doctorId(appointment.getDoctor().getId())
                .customerId(appointment.getCustomer().getId())
                .timetableId(appointment.getTimetable().getId())
                .start(appointment.getTimetable().getStart())
                .doctorName(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName())
                .build();
    }
}
