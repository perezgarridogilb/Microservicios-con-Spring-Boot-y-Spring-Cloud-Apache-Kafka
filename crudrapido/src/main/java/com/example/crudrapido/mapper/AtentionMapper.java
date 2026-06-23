package com.example.crudrapido.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.crudrapido.dto.request.AtentionRequestDto;
import com.example.crudrapido.dto.response.AtentionResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.Atention;

@Mapper(
     config = MapperConfiguration.class,
     uses = {StudentMapper.class, EmployeeMapper.class}) // Anidación de Recursos: 'student' => new StudentResource(...)
public interface AtentionMapper {
// source = "fecha": Es el campo que viene del Request (el DTO).
    @Mapping(source = "fecha", target = "date")
    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "patientId", target = "patient.id")

    // La función que sí llama a los otros mappers en tu configuración es toResponse(Atention entity).
    Atention toEntity(AtentionRequestDto dto);
    /*
       Equivale en Laravel a preparar un modelo nuevo con sus relaciones:
       
       $atencion = new Atencion();
       $atencion->motivo = $request->input('motivo'); // Automático por coincidencia de nombre
       
       $employee = new Employee();
       $employee->id = $request->input('employeeId'); // source = "employeeId"
       $atencion->employee()->associate($employee);   // target = "employee.id"
       
       $patient = new Patient();
       $patient->id = $request->input('patientId');   // source = "patientId"
       $atencion->patient()->associate($patient);     // target = "patient.id"
       
       return $atencion;
    */

   //  @Mapping(source = "patient.student", target = "student")
    AtentionResponseDTO toResponse(Atention entity);
    /*
       Equivale en Laravel a un API Resource:
       
       return [
           'id' => $this->id,
           'date' => $this->date,
           'atention' => $this->atention,
           'status' => $this->status,
           'student' => new StudentResource($this->patient.student), // Resuelto por el "uses = StudentMapper.class"
           'employee' => new EmployeeResource($this->employee) // Resuelto por el "uses = EmployeeMapper.class"
       ];
    */

    @Mapping(source = "fecha", target = "date")
    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(source = "employeeId", target = "employee.id")
    void updateEntity(@MappingTarget Atention entity, AtentionRequestDto dto);
    /*
       Equivale en Laravel a una actualización parcial / Mass Assignment:
       
       $atencion = Atencion::findOrFail($id); // Se le pasa por parámetro como @MappingTarget
       
       $atencion->fill($request->only(['atention', 'status'])); // Copia los campos planos que coinciden
       
       // Actualiza los pivotes/relaciones de las llaves foráneas:
       $patient = new Patient();
       $patient->id = $request->input('patientId');
       $atencion->patient()->associate($patient);
       
       $employee = new Employee();
       $employee->id = $request->input('employeeId');
       $atencion->employee()->associate($employee);
    */

}