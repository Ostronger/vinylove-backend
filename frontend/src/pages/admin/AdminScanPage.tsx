import { useState } from "react";
import "./AdminScanPage.css";
import QrScanner from "../../components/QrScanner";

const API_URL = import.meta.env.VITE_API_URL;

interface TableCheckInResponse {
    message: string;
    tableId: number;
    label: string;
    capacity: number;
    scanCount: number;
    remainingEntries: number;
    accepted: boolean;
}

export default function AdminScanPage() {

    const [qrCode, setQrCode] = useState("");
    const [result, setResult] = useState<TableCheckInResponse | null>(null);
    const [error, setError] = useState("");

    const handleCheckIn = async (code?: string) => {
        try {
            const response = await fetch(
                `${API_URL}/api/table-check-in?code=${encodeURIComponent(code ?? qrCode)}`
            );

            if (!response.ok) {
                throw new Error("Erreur lors du scan");
            }

            const data = await response.json();

            setResult(data);
            setError("");
            setQrCode("");
        } catch (err) {
            console.error(err);
            setError("Impossible de valider le QR code");
            setResult(null);
            setQrCode("");
        }
    };

    return (
        <div>
            <h1>Scan des invitations</h1>

            <QrScanner
                onScanSuccess={(decodedText) => {
                    setQrCode(decodedText);
                    handleCheckIn(decodedText);
                }}
            />

            <input

                type="text"

                placeholder="UUID du QR code"

                value={qrCode}

                onChange={(e) => setQrCode(e.target.value)}

            />

            <button onClick={() => handleCheckIn()}>

                Valider

            </button>

            {error && <p>{error}</p>}

            {result && (

                <div className = {result.accepted ? "scan-success" : "scan-error"}>

                    <h3>{result.message}</h3>

                    <p>Table : {result.label}</p>
                    <p>Capacité : {result.capacity}</p>
                    <p>Scans : {result.scanCount}</p>
                    <p>Accepté : {result.accepted ? "Oui" : "Non"}</p>
                    <p>Places restantes : {result.remainingEntries}</p>

                </div>

            )}
        </div>
    );
}