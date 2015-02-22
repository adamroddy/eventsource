package com.thirdchannel.eventsource

import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * @author Steve Pember
 */
@CompileStatic
@ToString
abstract class AbstractAggregate implements Aggregate {


    void markEventsAsCommitted() {
        uncommittedEvents.clear()
    }

    void loadFromPastEvents(List<Event> events) {
        for (Event event : events) {
            runEvent(event, false)
        }
    }

    void applyChange(Event event) {
        runEvent(event, true)
    }

    private void runEvent(Event event, boolean newEvent) {
        //mark 'ownership' of the event the moment it's run
        event.aggregateId = this.id
        event.process this
        if (newEvent) {
            uncommittedEvents.add event
        }
    }
}
