package greenjangtanji.yeosuro.plan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @GetMapping("/byBody")
    public ResponseEntity<?> getPlan(@RequestBody(required = false) String body) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getPlan2(@RequestParam(required = false) String body) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
