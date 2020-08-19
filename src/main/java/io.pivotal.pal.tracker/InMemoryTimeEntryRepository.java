package io.pivotal.pal.tracker;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    Map<Long, TimeEntry> timeEntryHashMap  = new HashMap<Long, TimeEntry>();
    private static Long idNum = 1L;
    public InMemoryTimeEntryRepository() {
        this.idNum= 1L;
    }

    public TimeEntry create(TimeEntry timeEntry) {
        if(timeEntry.getId() == 0L) {
            timeEntry.setId(idNum++);
        }
        timeEntryHashMap.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        if(timeEntryHashMap.containsKey(id)){
            return timeEntryHashMap.get(id);
        } else {
            return null;
        }
    }

    public List<TimeEntry> list() {
        if(timeEntryHashMap == null || timeEntryHashMap.isEmpty()) {
            return new ArrayList<>();
        } else {
            return timeEntryHashMap.values().stream().collect(Collectors.toList());
        }
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(timeEntryHashMap.containsKey(id)) {
            if(timeEntry.getId() == 0L) {
                timeEntry.setId(id);
            }
            timeEntryHashMap.put(id, timeEntry);
            return timeEntry;
        } else {
            return null;
        }
    }

    public void delete(long id) {
        timeEntryHashMap.remove(id);
    }
}
