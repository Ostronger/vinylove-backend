import { useParams } from "react-router-dom";

export default function StaffEventStatsPage() {
    const { eventId } = useParams();

    return (
        <div>
            <h1>Statistiques événement</h1>
            <p>Event ID : {eventId}</p>
        </div>
    );
}