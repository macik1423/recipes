package recipes.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.config.IAuthenticationFacade;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    public Recipe getRecipe(long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipe.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String createRecipe(Recipe newRecipe) {
        newRecipe.setOwner(getLoggedUser());
        recipeRepository.save(newRecipe);
        return "{\"id\":" + newRecipe.getId() + "}";
    }

    private User getLoggedUser() {
        Object auth = authenticationFacade.getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Object loggedUser = authenticationFacade.getAuthentication().getPrincipal();
        return userRepository.findById(((User) loggedUser).getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Void> deleteRecipe(long id) {
        var recipe = recipeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (recipe.getOwner().equals(getLoggedUser())) {
            recipeRepository.deleteById(recipe.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<Void> putRecipe(Recipe updatedRecipe, long id) {
        var recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (recipe.getOwner().equals(getLoggedUser())) {
            recipe.setDate(LocalDateTime.now());
            recipe.setCategory(updatedRecipe.getCategory());
            recipe.setDescription(updatedRecipe.getDescription());
            recipe.setName(updatedRecipe.getName());
            recipe.setIngredients(updatedRecipe.getIngredients());
            recipe.setDirections(updatedRecipe.getDirections());
            recipeRepository.save(recipe);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public Object searchRecipe(String category, String name) {
        if (category != null && name != null || category == null && name == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (category != null) {
                return recipeRepository.findByCategoryIgnoreCase(category).stream().sorted(Comparator.comparing(Recipe::getDate).reversed());
            }
            return recipeRepository.findByNameContainingIgnoreCase(name).stream().sorted(Comparator.comparing(Recipe::getDate).reversed());
        }
    }
}
