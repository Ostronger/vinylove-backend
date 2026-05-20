import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import "./InvitationTablesPage.css";

type InvitationTable = {
    id: number;
    label: string;
    guestText: string;
    capacity: number;
    scanCount: number;
    qrCode: string;
    eventId: number;
    eventName?: string;
};

export default function InvitationTablesPage() {
    const API_URL = import.meta.env.VITE_API_URL;

    const [label, setLabel] = useState("");
    const [guestText, setGuestText] = useState("");
    const [capacity, setCapacity] = useState(1);
    const [successMessage, setSuccessMessage] = useState("");
    const [tables, setTables] = useState<InvitationTable[]>([]);
    const [eventName, setEventName] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const { eventId } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTables = async () => {
            const token = localStorage.getItem("accessToken");

            const response = await fetch(`${API_URL}/api/events/${eventId}/invitation-tables`, {
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
                setTables(data);

                if (data.length > 0) {
                    setEventName(data[0].eventName || "");
                }
            } else {
                setTables([]);
            }
        };

        fetchTables();
    }, [API_URL, eventId, navigate]);

    const handleSubmit = async (e: React.SyntheticEvent) => {
        e.preventDefault();

        setSuccessMessage("");
        setIsLoading(true);

        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/events/${eventId}/invitation-tables`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                label,
                guestText,
                capacity,
            }),
        });

        if (response.status === 401) {
            localStorage.clear();
            navigate("/");
            return;
        }

        if (response.ok) {
            const data = await response.json();

            setTables((previousTables) => [...previousTables, data]);
            setSuccessMessage("Table créée avec succès !");

            setLabel("");
            setGuestText("");
            setCapacity(1);
        }

        setIsLoading(false);
    };

    const downloadPdf = async (tableId: number) => {
        const token = localStorage.getItem("accessToken");

        const response = await fetch(`${API_URL}/api/events/${eventId}/invitation-tables/${tableId}/pdf`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });

        if (response.status === 401) {
            localStorage.clear();
            navigate("/");
            return;
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);

        window.open(url, "_blank");
    };

    return (
        <div className="tables-page">
            <div className="tables-header">
                <h1>Tables d’invitation {eventName && ` - ${eventName}`}</h1>
                <p>Créez et gérez les tables d’invitation pour cet événement.</p>

                <Link to="/admin/events" className="back-link">
                    ← Retour aux événements
                </Link>
            </div>

            {successMessage && <p className="message-success">{successMessage}</p>}

            <form className="tables-form" onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Nom de la table"
                    value={label}
                    onChange={(e) => setLabel(e.target.value)}
                />

                <textarea
                    placeholder="Invités / Notes"
                    value={guestText}
                    onChange={(e) => setGuestText(e.target.value)}
                />

                <input
                    type="number"
                    placeholder="Capacité"
                    value={capacity}
                    onChange={(e) => setCapacity(Number(e.target.value))}
                />

                <button type="submit" disabled={isLoading}>
                    {isLoading ? "Création en cours..." : "Créer la table"}
                </button>
            </form>

            {tables.length === 0 ? (
                <div className="empty-state">
                    <h2>Aucune table créée</h2>
                    <p>Créez une table, un couple ou un groupe pour générer une invitation PDF.</p>
                </div>
            ) : (
                <div className="tables-list">
                    {tables.map((table) => (
                        <div className="table-card" key={table.id}>
                            <h3>{table.label}</h3>
                            <p>{table.guestText}</p>
                            <p>Capacité : {table.capacity}</p>
                            <p>Scans : {table.scanCount}</p>

                            <button onClick={() => downloadPdf(table.id)}>
                                Télécharger PDF
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}