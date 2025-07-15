package dev.research_assistant.controller;

import dev.research_assistant.request.ResearchRequest;
import dev.research_assistant.service.ResearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/research")
@CrossOrigin("*")
@AllArgsConstructor
public class ResearchController {

    @Autowired
    private ResearchService researchService;

    @PostMapping("/process")
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest request){
      String result = researchService.processContent(request);
      return ResponseEntity.ok(result);
    }
}
