import { Link } from "react-router-dom";
import "./StaffDashboardPage.css";

export default function StaffDashboardPage() {
    return (
        <div className="staff-dashboard">
            <h1>Dashboard Staff</h1>

            <p className="staff-subtitle">
                Gérez les accès et les événements actifs.
            </p>

            <div className="staff-actions">

                <Link
                    to="/staff/events"
                    className="staff-card"
                >
                    <h2>🎉 Événements actifs</h2>

                    <p>
                        Consulter les événements disponibles et leurs statistiques.
                    </p>
                </Link>

                <Link
                    to="/staff/scan"
                    className="staff-card"
                >
                    <h2>📷 Scanner</h2>

                    <p>
                        Scanner ou saisir un QR code pour valider une entrée.
                    </p>
                </Link>

            </div>
        </div>
    );
}