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
    active: boolean;
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
    const [editingEventId, setEditingEventId] = useState<number | null>(null);
    
    const [editName, setEditName] = useState("");
    const [editDescription, setEditDescription] = useState("");
    const [editLocation, setEditLocation] = useState("");
    const [editEventDate, setEditEventDate] = useState("");
    const [editBannerImageUrl, setEditBannerImageUrl] = useState("");
    

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

    const handleUpdateEvent = async () => {
        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/events/${editingEventId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                name: editName,
                description: editDescription,
                location: editLocation,
                eventDate: editEventDate,
                bannerImageUrl: editBannerImageUrl,
            }),
        });

        if (response.ok) {
            const updatedEvent = await response.json();

            setEvents((previousEvents) =>
                previousEvents.map((event) =>
                    event.id === updatedEvent.id ? updatedEvent : event
                )
            );

            setEditingEventId(null);
            setSuccessMessage("Événement mis à jour avec succès !");
        } else {
            setErrorMessage("Erreur lors de la mise à jour de l'événement.");
        }
    };

    const handleDeleteEvent = async (eventId: number) => {
        const confirlDelete = window.confirm("Êtes-vous sûr de vouloir supprimer cet événement ?");
        if (!confirlDelete) return;

        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/events/${eventId}`, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (response.status === 401) {
            localStorage.clear();
            navigate("/");
            return;
        }

        if (response.ok) {
            setEvents((previousEvents) =>
                previousEvents.filter((event) => event.id !== eventId)
            );
            setSuccessMessage("Événement supprimé avec succès !");
        } else {
            setErrorMessage("Erreur lors de la suppression de l'événement.");
        }
    };

    const toggleEventActive = async (event: Event) => {
        const token = localStorage.getItem("accessToken");

        const endpoint = event.active ? "deactivate" : "activate";

        const response = await fetch(`${API_URL}/api/events/${event.id}/${endpoint}`, {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (response.status === 401) {
            localStorage.clear();
            navigate("/");
            return;
        }

        if (response.ok) {
            const updatedEvent = await response.json();

            setEvents((previousEvents) =>
                previousEvents.map((currentEvent) =>
                    currentEvent.id === updatedEvent.id ? updatedEvent : currentEvent
                )
            );

            setSuccessMessage(
                updatedEvent.active
                    ? "Événement activé avec succès !"
                    : "Événement désactivé avec succès !"
            );
        } else {
            setErrorMessage("Erreur lors de la mise à jour de l'état de l'événement.");
        }
    };

    const startEditing = (event: Event) => {
        setEditingEventId(event.id);
        setEditName(event.name);
        setEditDescription(event.description);
        setEditLocation(event.location);
        setEditEventDate(event.eventDate);
        setEditBannerImageUrl(event.bannerImageUrl || "");
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
                                {editingEventId === event.id ? (
                                    <>
                                        <input
                                            type="text"
                                            value={editName}
                                            onChange={(e) => setEditName(e.target.value)}
                                        />

                                        <input
                                            type="text"
                                            value={editLocation}
                                            onChange={(e) => setEditLocation(e.target.value)}
                                        />

                                        <input
                                            type="text"
                                            value={editDescription}
                                            onChange={(e) => setEditDescription(e.target.value)}
                                        />

                                        <input
                                            type="datetime-local"
                                            value={editEventDate}
                                            onChange={(e) => setEditEventDate(e.target.value)}
                                        />

                                        <input
                                            type="text"
                                            value={editBannerImageUrl}
                                            onChange={(e) => setEditBannerImageUrl(e.target.value)}
                                        />

                                        <button onClick={handleUpdateEvent}>
                                            Sauvegarder
                                        </button>

                                        <button onClick={() => setEditingEventId(null)}>
                                            Annuler
                                        </button>
                                    </>
                                ) : (
                                   <>
                                        <h2>{event.name}</h2>
                                        <p>{event.location}</p>
                                        <p>{new Date(event.eventDate).toLocaleString("fr-FR")}</p>

                                        <div className="event-actions">
                                            <button onClick={() => startEditing(event)}>
                                                Modifier
                                            </button>
                                       

                                            <Link to={`/admin/events/${event.id}/invitation-tables`}>
                                                Gérer les tables
                                            </Link>

                                            <button onClick={() => handleDeleteEvent(event.id)}>
                                                Supprimer
                                            </button>

                                            <button onClick={() => toggleEventActive(event)}>
                                                {event.active ? "Désactiver" : "Activer"}
                                            </button>
                                        </div>
                                    </>
                                )}
                            </div>
                        </div>
                    ))}

                </div>
            )}
        </div>
    );
}