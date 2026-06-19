import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL;

interface Event {
    id: number;
    name: string;
    description: string;
    location: string;
    eventDate: string;
    active: boolean;
}

export default function StaffEventsPage() {

    const [events, setEvents] = useState<Event[]>([]);

    

    useEffect(() => {
        const fetchActiveEvents = async () => {
            const token = localStorage.getItem("accessToken");

            const response = await fetch(`${API_URL}/api/events/active`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            const data = await response.json();

            if (Array.isArray(data)) {
                setEvents(data);
            } else {
                setEvents([]);
            }
        };

        fetchActiveEvents();
    }, []);

    return (
        <div>
            <h1>Événements actifs</h1>

            {events.map((event) => (
                <div key={event.id}>
                    <h2>{event.name}</h2>

                    <p>{event.location}</p>

                    <p>
                        {new Date(event.eventDate).toLocaleString("fr-FR")}
                    </p>
                    
                    <Link to={`/staff/events/${event.id}`}>
                        Voir les statistiques
                    </Link>
                
                </div>
            ))}
        </div>
    );
}