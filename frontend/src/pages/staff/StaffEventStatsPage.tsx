import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL;

interface EventStats {
    eventId: number;
    eventName: string;
    capacity: number;
    scans: number;
    remaining: number;
    fillRate: number;
}

interface TableStats {
    tableId: number;
    label: string;
    capacity: number;
    scans: number;
    remaining: number;
    fillRate: number;
}

export default function StaffEventStatsPage() {
    const { eventId } = useParams();

    const [stats, setStats] = useState<EventStats | null>(null);
    const [tableStats, setTableStats] = useState<TableStats[]>([]);

    useEffect(() => {
        const fetchStats = async () => {
            const token = localStorage.getItem("accessToken");

            const response = await fetch(
                `${API_URL}/api/events/${eventId}/stats`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const data = await response.json();

            setStats(data);

            const tableResponse = await fetch(
                `${API_URL}/api/events/${eventId}/tables/stats`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            const tableData = await tableResponse.json();

            if (Array.isArray(tableData)) {
                setTableStats(tableData);
            }
        };

        fetchStats();
    }, [eventId]);

    if (!stats) {
        return <p>Chargement...</p>;
    }

    return (
        <div>
            <h1>{stats.eventName}</h1>

            <p>Capacité : {stats.capacity}</p>

            <p>Entrées : {stats.scans}</p>

            <p>Places restantes : {stats.remaining}</p>

            <p>Taux de remplissage : {stats.fillRate}%</p>


            <h2>Tables</h2>

            {tableStats.map((table) => (
                <div key={table.tableId}>
                    <h3>{table.label}</h3>

                    <p>
                        Entrées : {table.scans} / {table.capacity}
                    </p>

                    <p>
                        Places restantes : {table.remaining}
                    </p>

                    <p>
                        Taux de remplissage : {table.fillRate}%
                    </p>
                </div>
            ))}
        </div>
    );
}