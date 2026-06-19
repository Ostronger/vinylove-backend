import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./StaffEventStatsPage.css";

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
        <div className="staff-event-stats">
            <h1>{stats.eventName}</h1>

            <div className="stats-grid">
                <div className="stat-card">
                    <h3>Capacité</h3>
                    <p>{stats.capacity}</p>
                </div>

                <div className="stat-card">
                    <h3>Entrées</h3>
                    <p>{stats.scans}</p>
                </div>

                <div className="stat-card">
                    <h3>Restantes</h3>
                    <p>{stats.remaining}</p>
                </div>

                <div className="stat-card">
                    <h3>Taux</h3>
                    <p>{stats.fillRate}%</p>
                </div>
            </div>

            <h2>Tables</h2>

            <div className="tables-stats">
                {tableStats.map((table) => (
                    <div className="table-stat-card" key={table.tableId}>
                        <h3>{table.label}</h3>

                        <p>Entrées : {table.scans} / {table.capacity}</p>
                        <p>Places restantes : {table.remaining}</p>
                        <p>Taux : {table.fillRate}%</p>

                        <div className="progress-bar">
                            <div
                                className="progress-fill"
                                style={{ width: `${table.fillRate}%` }}
                            />
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}