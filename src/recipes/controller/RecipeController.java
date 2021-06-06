package recipes.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.service.RecipeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;


    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping(value = "/recipe/new", consumes = "application/json")
    public String createRecipe(@RequestBody @Valid Recipe newRecipe) {
        return recipeService.createRecipe(newRecipe);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id) {
        return recipeService.deleteRecipe(id);
    }

    @PutMapping(value = "/recipe/{id}")
    public ResponseEntity<Void> putRecipe(@RequestBody @Valid Recipe updatedRecipe, @PathVariable long id) {
        return recipeService.putRecipe(updatedRecipe, id);
    }

    @GetMapping(value = "/recipe/search/")
    public Object searchRecipe(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        return recipeService.searchRecipe(category, name);
    }
}
