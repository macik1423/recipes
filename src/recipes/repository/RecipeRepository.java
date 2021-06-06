package recipes.repository;

import org.springframework.data.repository.Repository;
import recipes.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends Repository<Recipe, Long> {
    List<Recipe> findByCategoryIgnoreCase(String category);
    List<Recipe> findByNameContainingIgnoreCase(String name);
    Optional<Recipe> findById(long id);
    <S extends Recipe> void save(S entity);
    void deleteById(long id);
}
