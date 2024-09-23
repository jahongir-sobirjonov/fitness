package uni.project.fitness.servise.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.project.fitness.dto.request.TrainingRequestDTO;
import uni.project.fitness.dto.response.*;
import uni.project.fitness.entity.Course;
import uni.project.fitness.entity.Teacher;
import uni.project.fitness.entity.Training;
import uni.project.fitness.exception.DataNotFoundException;
import uni.project.fitness.config.mapper.MyConverter;
import uni.project.fitness.repository.CourseRepository;
import uni.project.fitness.repository.TeacherRepository;
import uni.project.fitness.repository.TrainingRepository;
import uni.project.fitness.servise.interfaces.TrainingService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository; // Make sure to inject this
    private final MyConverter converter;

    @Override
    public TrainingResponseDTO createTraining(TrainingRequestDTO requestDTO) {
        Training training = Training.builder()
                .title(requestDTO.getTitle())
                .teacher(teacherRepository.findById(requestDTO.getTeacherId())
                        .orElseThrow(() -> new DataNotFoundException("Teacher not found")))
                .course(courseRepository.findById(requestDTO.getCourseId())
                        .orElseThrow(() -> new DataNotFoundException("Course not found")))
                .description(requestDTO.getDescription())
                .shortDescription(requestDTO.getShortDescription())
                .image(requestDTO.getImage())
                .video(requestDTO.getVideo())
                .importantInfo(requestDTO.getImportantInfo())
                .equipments(requestDTO.getEquipments())
                .musclesInvolved(requestDTO.getMusclesInvolved())
                .results(requestDTO.getResults())
                .build();
        training = trainingRepository.save(training);
        return converter.convertToResponseDTO(training);
    }

    @Override
    public TrainingResponseDTO updateTraining(UUID id, TrainingRequestDTO requestDTO) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Training not found"));
        training.setTitle(requestDTO.getTitle());
        training.setTeacher(teacherRepository.findById(requestDTO.getTeacherId())
                .orElseThrow(() -> new DataNotFoundException("Teacher not found")));
        training.setCourse(courseRepository.findById(requestDTO.getCourseId())
                .orElseThrow(() -> new DataNotFoundException("Course not found")));
        training.setDescription(requestDTO.getDescription());
        training.setShortDescription(requestDTO.getShortDescription());
        training.setImage(requestDTO.getImage());
        training.setVideo(requestDTO.getVideo());
        training.setImportantInfo(requestDTO.getImportantInfo());
        training.setEquipments(requestDTO.getEquipments());
        training.setMusclesInvolved(requestDTO.getMusclesInvolved());
        training.setResults(requestDTO.getResults());
        training = trainingRepository.save(training);
        return converter.convertToResponseDTO(training);
    }

    @Override
    public void deleteTraining(UUID id) {
        trainingRepository.deleteById(id);
    }

    @Override
    public TrainingResponseDTO getTrainingById(UUID id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Training not found"));
        return converter.convertToResponseDTO(training);
    }

    @Override
    public List<TrainingResponseDTO> getAllTrainings() {
        List<Training> trainings = trainingRepository.findAll();
        return trainings.stream()
                .map(converter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingResponseDTO> getTrainingsByCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DataNotFoundException("Course not found with id: " + courseId));
        return trainingRepository.findByCourse(course).stream()
                .map(converter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingResponseDTO> getTrainingsByTeacher(UUID teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new DataNotFoundException("Teacher not found with id: " + teacherId));
        return trainingRepository.findByTeacher(teacher).stream()
                .map(converter::convertToResponseDTO)
                .collect(Collectors.toList());
    }
}

