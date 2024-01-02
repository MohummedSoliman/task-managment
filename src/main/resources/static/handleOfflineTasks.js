<script src="Event.js"/>
<script src="EventDateTime.js"/>

document.getElementById("eventForm").addEventListener("submit", function(event) {
    event.preventDefault();

    // Create an Start Object
    let start = new EventDateTime(
        dateTime: document.getElementById("startDateTime").value
    );
    // Create End Object
    let end = new EventDateTime(
        dateTime: document.getElementById("endDateTime").value
    );
    // Create Event Object
    let event = new Event(
    document.querySelector('input[name="summary"]').value,
    description: document.querySelector('input[name="description"]').value,
    start,
    end
    )

    // Check if offline
    if (!navigator.onLine) {
        let offlineEvents = JSON.parse(localStorage.getItem("offlineEvents")) || [];
        offlineEvents.push(event);
        localStorage.setItem("offlineEvents", JSON.stringify(offlineEvents));
    } else {
        // Send POST request to Spring Boot API for online mode
        console.log(eventData)
        fetch("/save", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(event),
        })
    }
});