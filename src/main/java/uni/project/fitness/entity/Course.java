package uni.project.fitness.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import uni.project.fitness.entity.icons.IconDescription;
import java.util.List;

@Entity(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Course extends BaseEntity {
    private String title;
    private String subTitle;
    private String description;
    private String image;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Training> trainings;

    private String trailerVideo;

    @ElementCollection(targetClass = IconDescription.class)
    @Enumerated(EnumType.STRING)
    private List<IconDescription> whatYouWillGet;  // Updated to use IconDescription enum

    @ElementCollection
    private List<String> whatToExpects;

    private String purpose;

    @ElementCollection
    private List<String> results;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    public boolean isAccessibleForUser(UserEntity user) {
        return user.hasActiveSubscriptionForCourse(this);  // Check if user has an active subscription for this course
    }
}
