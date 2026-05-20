import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./AdminEventsPage.css";

type Event = {
    id: number;
    name: string;
    description: string;
    location: string;
    eventDate: string;
    bannerImageUrl?: string;
};

export default function AdminEventsPage() {
    const API_URL = import.meta.env.VITE_API_URL;
    const navigate = useNavigate();

    const [events, setEvents] = useState<Event[]>([]);

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [location, setLocation] = useState("");
    const [eventDate, setEventDate] = useState("");
    const [bannerImageUrl, setBannerImageUrl] = useState("");

    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const fetchEvents = async () => {
            const token = localStorage.getItem("accessToken");

            const response = await fetch(`${API_URL}/api/events`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.status === 401) {
                localStorage.clear();
                navigate("/");
                return;
            }

            const data = await response.json();

            if (Array.isArray(data)) {
                setEvents(data);
            } else {
                setEvents([]);
            }
        };

        fetchEvents();
    }, [API_URL, navigate]);

    const handleCreateEvent = async (e: React.SyntheticEvent) => {
        e.preventDefault();

        setSuccessMessage("");
        setErrorMessage("");
        setIsLoading(true);

        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/events`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                name,
                description,
                location,
                eventDate,
                bannerImageUrl,
            }),
        });

        if (response.status === 401) {
            localStorage.clear();
            navigate("/");
            return;
        }

        if (response.ok) {
            const newEvent = await response.json();

            setEvents((previousEvents) => [...previousEvents, newEvent]);

            setName("");
            setDescription("");
            setLocation("");
            setEventDate("");
            setBannerImageUrl("");

            setSuccessMessage("Événement créé avec succès !");
        } else {
            setErrorMessage("Erreur lors de la création de l'événement.");
        }

        setIsLoading(false);
    };

    return (
        <div className="events-page">
            <div className="events-header">
                <h1>Événements</h1>
                <p>Créez et gérez les événements.</p>
            </div>

            {successMessage && <p className="message-success">{successMessage}</p>}
            {errorMessage && <p className="message-error">{errorMessage}</p>}

            <form className="events-form" onSubmit={handleCreateEvent}>
                <input
                    type="text"
                    placeholder="Nom de l’événement"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />

                <input
                    type="text"
                    placeholder="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />

                <input
                    type="text"
                    placeholder="Lieu"
                    value={location}
                    onChange={(e) => setLocation(e.target.value)}
                />

                <input
                    type="datetime-local"
                    value={eventDate}
                    onChange={(e) => setEventDate(e.target.value)}
                />

                <input
                    type="text"
                    placeholder="URL de la bannière"
                    value={bannerImageUrl}
                    onChange={(e) => setBannerImageUrl(e.target.value)}
                />

                <button type="submit" disabled={isLoading}>
                    {isLoading ? "Création en cours..." : "Créer l’événement"}
                </button>
            </form>

            {events.length === 0 ? (
                <div className="empty-state">
                    <h2>Aucun événement créé</h2>
                    <p>Créez votre premier événement pour commencer à gérer les invitations.</p>
                </div>
            ) : (
                <div className="events-list">
                    {events.map((event) => (
                        <div className="event-card" key={event.id}>
                            {event.bannerImageUrl && (
                                <img src={event.bannerImageUrl} alt={event.name} />
                            )}

                            <div className="event-card-content">
                                <h2>{event.name}</h2>
                                <p>{event.location}</p>
                                <p>{new Date(event.eventDate).toLocaleString("fr-FR")}</p>

                                <Link to={`/admin/events/${event.id}/invitation-tables`}>
                                    Gérer les tables
                                </Link>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}