package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Recipe implements Serializable {
    @Getter(onMethod = @__( @JsonIgnore ))
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotEmpty
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> ingredients;

    @NotEmpty
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> directions;

    @NotBlank
    private String category;

    @NotNull
    private LocalDateTime date = LocalDateTime.now();

    @Getter(onMethod = @__( @JsonIgnore ))
    @Setter
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    private User owner;

}
