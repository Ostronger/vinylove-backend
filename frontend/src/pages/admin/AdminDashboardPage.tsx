import { useEffect, useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;

interface AdminStats {
    totalEvents: number;
    totalTables: number;
    totalCapacity: number;
    totalScans: number;
    remainingEntries: number;
    fillRate: number;
}

interface EventStats {
    eventId: number;
    eventName: string;
    capacity: number;
    scans: number;
    remaining: number;
    fillRate: number;
}

export default function AdminDashboardPage() {
    const [stats, setStats] = useState<AdminStats | null>(null);
    const [eventStats, setEventStats] = useState<EventStats[]>([]);

    useEffect(() => {
        const fetchStats = async () => {
            const token = localStorage.getItem("accessToken");

            const response = await fetch(
                `${API_URL}/api/admin/stats`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const data = await response.json();

            setStats(data);

            const eventResponse = await fetch(
                `${API_URL}/api/admin/stats/events`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const eventData = await eventResponse.json();

            if (Array.isArray(eventData)) {
                setEventStats(eventData);
            }
        };

        fetchStats();
    }, []);

    return (
        <div>
            <h1>Dashboard Admin</h1>

            {stats && (
                <>
                    <div>
                        <h3>Événements</h3>
                        <p>{stats.totalEvents}</p>
                    </div>

                    <div>
                        <h3>Tables</h3>
                        <p>{stats.totalTables}</p>
                    </div>

                    <div>
                        <h3>Capacité totale</h3>
                        <p>{stats.totalCapacity}</p>
                    </div>

                    <div>
                        <h3>Entrées validées</h3>
                        <p>{stats.totalScans}</p>
                    </div>

                    <div>
                        <h3>Places restantes</h3>
                        <p>{stats.remainingEntries}</p>
                    </div>

                    <div>
                        <h3>Taux de remplissage</h3>
                        <p>{stats.fillRate}%</p>
                    </div>

                    <h2>Statistiques par événement</h2>

                    {eventStats.map((event) => (
                        <div key={event.eventId}>
                            <h3>{event.eventName}</h3>

                            <p>Capacité : {event.capacity}</p>

                            <p>Entrées : {event.scans}</p>

                            <p>Places restantes : {event.remaining}</p>

                            <p>Taux de remplissage : {event.fillRate}%</p>
                        </div>
                    ))}
                    
                </>
            )}
        </div>
    );
}