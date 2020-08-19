package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.TooManyListenersException;

@RestController
//@ResponseBody
//@RequestMapping
public class TimeEntryController {

//    @Autowired
    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        System.out.println("PostMapping1");
        this.actionCounter.increment();
        timeEntrySummary.record(this.timeEntryRepository.list().size());
        return new ResponseEntity(this.timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntryToCreate) {
        System.out.println("PutMapping");
        TimeEntry timeEntry = this.timeEntryRepository.update(timeEntryId, timeEntryToCreate);
        if(timeEntry != null) {
            this.actionCounter.increment();
            return new ResponseEntity(timeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity(timeEntryId, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        System.out.println("GetMappingID");
        TimeEntry timeEntry = this.timeEntryRepository.find(timeEntryId);
        if(timeEntry != null) {
            this.actionCounter.increment();
            return new ResponseEntity(timeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity("NOT FOUND", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        System.out.println("GetMapping");
        this.actionCounter.increment();
        return new ResponseEntity(this.timeEntryRepository.list(), HttpStatus.OK);
    }
    
    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        System.out.println("DeleteMapping");
        actionCounter.increment();
        timeEntrySummary.record(this.timeEntryRepository.list().size());
        this.timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity("Delete sucessfully", HttpStatus.NO_CONTENT);
    }
}
