import { Link } from "react-router-dom";

export default function StaffDashboardPage() {
    return (
        <div>
            <h1>Dashboard Staff</h1>
            <p>Accédez aux événements actifs et scannez les invitations.</p>

            <Link to="/staff/events">
                Voir les événements actifs
            </Link>

            <br />

            <Link to="/staff/scan">
                Scanner une invitation
            </Link>
        </div>
    );
}